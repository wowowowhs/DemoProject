package com.api.orderservice.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderReqData implements Serializable {
    private static final long serialVersionUID = -6544282806103846383L;

    private Integer orderId;
    private String accountId;
    private Integer productId;
    private Integer productCount;
}
