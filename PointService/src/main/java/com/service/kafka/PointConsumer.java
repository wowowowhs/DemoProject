package com.service.kafka;

import com.api.ponitservice.constant.PointConstant;
import com.api.ponitservice.req.PointReqData;
import com.google.gson.Gson;
import com.service.entity.Point;
import com.service.mapper.PointMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PointConsumer {

    @Resource
    private PointMapper pointMapper;

    @KafkaListener(topics = PointConstant.POINT_TEST)
    public void addPointTest(ConsumerRecord<?, ?> record) {
        //业务逻辑......
        log.info("addPointTest topic:{}, offset:{}, value:{} ", record.topic(), record.offset(), record.value());
    }

    @KafkaListener(topics = PointConstant.POINT_ADD_POINT)
    public void addPoint(ConsumerRecord<?, ?> record) {
        Gson gson = new Gson();
        PointReqData pointReqData = gson.fromJson(record.value().toString(), PointReqData.class);
        log.info("addPoint point info, accountId={}, point={}",
                pointReqData.getAccountId(), pointReqData.getPoint());

        //插入数据
        Point point = Point.builder()
                .accountId(pointReqData.getAccountId())
                .point(pointReqData.getPoint())
                .build();
        pointMapper.insertPoint(point);
        log.info("add point success, id:{}", point.getId());
    }

}
