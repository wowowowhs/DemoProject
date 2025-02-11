package com.service.kafka;

import com.api.ponitservice.constant.PointConstant;
import com.api.ponitservice.req.PointReqData;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PointConsumer {

    @KafkaListener(topics = PointConstant.POINT_TEST)
    public void addPointTest(ConsumerRecord<?, ?> record) {
        System.out.println(String.format("addPointTest topic:%s, offset:%d, value:%s ", record.topic(), record.offset(), record.value()));
    }

    @KafkaListener(topics = PointConstant.POINT_ADD_POINT)
    public void addPoint(ConsumerRecord<?, ?> record) {
        Gson gson = new Gson();
        PointReqData pointReqData = gson.fromJson(record.value().toString(), PointReqData.class);
        System.out.println(String.format("addPoint point info, accountId=%s, point=%d", pointReqData.getAccountId(), pointReqData.getPoint()));
    }

}
