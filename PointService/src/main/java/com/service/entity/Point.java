package com.service.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Point {

    private Integer id;
    private String accountId;
    private Integer point;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
