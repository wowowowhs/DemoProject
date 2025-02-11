package com.service.mapper;

import com.service.entity.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointMapper {

    List<Point> getAllPoint();

    /**
     * 插入积分
     *
     * @param point
     */
    void insertPoint(Point point);

    /**
     * 更新积分
     *
     * @param point
     */
    void updatePointById(Point point);

    /**
     * 根据id查询积分信息
     *
     * @param id
     * @return
     */
    Point selectPointById(Integer id);

    /**
     * 根据id删除积分信息
     *
     * @param id
     */
    void deletePointById(@Param("id") Integer id);

}
