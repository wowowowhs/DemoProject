package com.order.controller;

import com.api.orderservice.req.CreateOrderReqData;
import com.api.ponitservice.constant.PointConstant;
import com.api.ponitservice.req.PointReqData;
import com.api.ponitservice.service.PointService;
import com.google.gson.Gson;
import com.order.constant.OrderConstant;
import com.order.redis.RedisUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    //dumbo提供的Reference注解，用于调用远程服务
    @DubboReference(check = false)
    private PointService pointService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    //todo 数据一致性
    @PostMapping("/createOrderV1")
    public String createOrderV1(@RequestBody CreateOrderReqData createOrderReqData) {
        //创建订单逻辑......

        //增加积分，rpc方式直接从调用远程服务从数据库增加积分
        String res = pointService.addPoint(createOrderReqData.getProductCount());
        return res;
    }

    //todo 消息丢失、重复消费、异步消费
    @PostMapping("/createOrderV2")
    public String createOrderV2(@RequestBody CreateOrderReqData createOrderReqData) {
        //创建订单逻辑......

        //发送积分消息，由pointService消费(发送单个属性示例)
        kafkaTemplate.send(PointConstant.POINT_TEST, createOrderReqData.getProductCount().toString());

        //发送积分消息，由pointService消费(发送对象示例)
        Gson gson = new Gson();
        PointReqData pointReqData = PointReqData.builder()
                .accountId(createOrderReqData.getAccountId())
                .point(createOrderReqData.getProductCount())
                .build();
        kafkaTemplate.send(PointConstant.POINT_ADD_POINT, gson.toJson(pointReqData));

        return "res";
    }

    //redis 操作扣减库存
    //todo 缓存和数据库数据一致？
    @PostMapping("/createOrderV3")
    public String createOrderV3(@RequestBody CreateOrderReqData createOrderReqData) {
        //创建订单逻辑......

        Integer productId = createOrderReqData.getProductId();
        //扣除库存
        String key = String.format(OrderConstant.PRODUCT_KEY_TEMPLATE, productId);
        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
        Integer stock = operations.get(key);
        if (stock == null || stock.equals(0)) {
            //todo 全局异常/自定义异常
            return String.format("参数错误：productId = %d商品不存在/库存不足", productId);
        }
        Integer restStock = stock - createOrderReqData.getProductCount();
        operations.set(key, restStock);
        return String.format("productId=%d,剩余库存:%d", productId, restStock);
    }

    //通过lua脚本扣减库存
    @PostMapping("/createOrderV4")
    public String createOrderV4(@RequestBody CreateOrderReqData createOrderReqData) {
        //创建订单逻辑......

        Integer productId = createOrderReqData.getProductId();
        Integer deductStock = createOrderReqData.getProductCount();

        //扣除库存
        String key = String.format(OrderConstant.PRODUCT_KEY_TEMPLATE, productId);
        Integer restStock = redisUtil.deductStock(key, deductStock);
        if (restStock == null || restStock < 0) {
            return String.format("productId:%d 库存不足，下单失败", productId);
        }
        return String.format("productId=%d,剩余库存:%d", productId, restStock);
    }

}
