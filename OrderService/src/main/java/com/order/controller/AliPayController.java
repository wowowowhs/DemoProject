package com.order.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

/**
 * 支付宝扫码支付demo
 */
@Slf4j
@RestController
public class AliPayController {

    @Autowired
    private Config config;


    @PostMapping("/pay")
    public String pay() throws Exception {

        //公共请求
        Factory.setOptions(config);

        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        LocalDate now = LocalDate.now();
        String tradeNo = now.toString().replace("-", "") + uuid;
        //调用支付宝接口
        AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace()
                .preCreate("iphone15 Pro Max 1T", tradeNo, "120.00");
        //解析结果
        String httpBody = response.getHttpBody();
        //转json对象
        JSONObject jsonObject = JSONObject.parseObject(httpBody);
        String qrUrl = jsonObject.getJSONObject("alipay_trade_precreate_response").get("qr_code").toString();
        //生成二维码，生成的二维码在当前项目根文件夹下
        QrCodeUtil.generate(qrUrl, 300, 300, new File("./payCode.jpg"));

        return httpBody;
    }

    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        log.info("收到付款成功回调....");
        String outTradeNo = request.getParameter("out_trade_no");
        log.info("trade no : {}", outTradeNo);
        //后续业务流程 发货...
        return "success:" + outTradeNo;
    }

}
