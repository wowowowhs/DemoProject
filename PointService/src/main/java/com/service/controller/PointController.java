package com.service.controller;

import com.api.ponitservice.req.PointReqData;
import com.api.ponitservice.resp.PointRespData;
import com.service.entity.Point;
import com.service.mapper.PointMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PointController {

    @Resource
    private PointMapper pointMapper;

    @PostMapping("/getAllPoints")
    public List<PointRespData> getAllPoints() {

        List<Point> allPoint = pointMapper.getAllPoint();
        List<PointRespData> resp = allPoint.stream().map(e -> {
            return PointRespData.builder()
                    .id(e.getId())
                    .point(e.getPoint())
                    .accountId(e.getAccountId())
                    .build();
        }).collect(Collectors.toList());
        return resp;
    }

    @PostMapping("/addPoint")
    public Integer addPoint(@RequestBody PointReqData pointReqData) {

        Point point = Point.builder()
                .accountId(pointReqData.getAccountId())
                .point(pointReqData.getPoint())
                .build();
        pointMapper.insertPoint(point);
        return point.getId();
    }

    @PostMapping("/updatePoint")
    public PointRespData updatePoint(@RequestBody PointReqData pointReqData) {

        Point point = Point.builder()
                .id(pointReqData.getId())
                .point(pointReqData.getPoint())
                .build();
        pointMapper.updatePointById(point);

        Point updatePoint = pointMapper.selectPointById(pointReqData.getId());
        return PointRespData.builder()
                .id(updatePoint.getId())
                .accountId(updatePoint.getAccountId())
                .point(updatePoint.getPoint())
                .build();
    }

    @PostMapping("/deletePointById")
    public Integer getPointById(@RequestBody PointReqData pointReqData) {
        pointMapper.deletePointById(pointReqData.getId());
        return 200;
    }

}
