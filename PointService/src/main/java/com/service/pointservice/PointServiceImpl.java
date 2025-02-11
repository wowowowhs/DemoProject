package com.service.pointservice;

import com.api.ponitservice.service.PointService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

// dubbo提供的Service注解，用于声明对外暴露服务
// Service引入的是org.apache.dubbo.config.annotation.Service包
@Component
@DubboService
public class PointServiceImpl implements PointService {
    @Override
    public String addPoint(Integer number) {
        System.out.println("增加积分：" + number);
        String res = "新增 " + number + " 积分";
        return String.format("新增 %d 积分", number);
    }
}
