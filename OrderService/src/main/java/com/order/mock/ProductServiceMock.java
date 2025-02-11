package com.order.mock;

import com.order.constant.OrderConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * 模拟库存，将库存初始化到redis中
 */
@Component
public class ProductServiceMock {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        //模拟产品id为1,2,3，将三个商品库存设为10000放到redis中
        List<Integer> productIds = Arrays.asList(1, 2, 3);
        for (Integer productId : productIds) {
            String key = String.format(OrderConstant.PRODUCT_KEY_TEMPLATE, productId);
            ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
            operations.set(key, 10000);
        }

    }

}
