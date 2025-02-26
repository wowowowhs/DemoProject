package com.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

/**
 * paypal支付demo
 */
@Slf4j
@RestController
public class PayPalController {

    //todo
    /**
     * PayPal支付
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/payPal/createOrder")
    public String qrCodePay() throws Exception {
        //

        return "ok";
    }

    private String genTradeNo() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        LocalDate now = LocalDate.now();
        String tradeNo = now.toString().replace("-", "") + uuid;
        return tradeNo;
    }

}
