package com.api.ponitservice.resp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PointRespData implements Serializable {

    private static final long serialVersionUID = -5079352212325369527L;
    private Integer id;
    private String accountId;
    private Integer point;
}
