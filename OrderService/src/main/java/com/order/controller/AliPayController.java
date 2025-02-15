package com.order.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
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

    private final String productName = "iphone15 Pro Max 1T";
    private final String totalAmount = "120.00";

    // 支付宝网关地址
    private final String ALIPAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    //登录自己的支付宝开放平台，补充对应的信息：app_id、application_private_key、alipay_public_key
    // 应用id
    private final String APP_ID = "app_id";
    // 应用私钥
    private final String APP_PRIVATE_KEY = "application_private_key";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "alipay_public_key";

    /**
     * alipay-easy版本 利用支付宝工具，生成二维码，供用户扫码支付
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/qrCodePayByEasy")
    public String qrCodePayByEasy() throws Exception {
        //公共请求
        Factory.setOptions(config);

        String tradeNo = genTradeNo();
        //调用支付宝接口
        AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace()
                .preCreate(productName, tradeNo, totalAmount);
        //解析结果
        String httpBody = response.getHttpBody();
        //转json对象
        JSONObject jsonObject = JSONObject.parseObject(httpBody);
        String qrUrl = jsonObject.getJSONObject("alipay_trade_precreate_response").get("qr_code").toString();
        //生成二维码，生成的二维码在当前项目根文件夹下
        QrCodeUtil.generate(qrUrl, 300, 300, new File("./payCode.jpg"));

        return httpBody;
    }

    /**
     * alipay-通用版本 利用支付宝工具，生成二维码，供用户扫码支付
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/qrCodePayByCommon")
    public String qrCodePayByCommon() throws Exception {

        //构造客户端
        //构造客户端方式1
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(ALIPAY_URL);
        alipayConfig.setAppId(APP_ID);
        alipayConfig.setPrivateKey(APP_PRIVATE_KEY);
        alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        // 初始化client
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);

        //构造客户端方式2
//        AlipayClient alipayClient = new DefaultAlipayClient(ALIPAY_URL, APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");

        // 构造请求参数以调用接口
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

        String tradeNo = genTradeNo();
        // 设置商户订单号
        model.setOutTradeNo(tradeNo);
        // 设置订单总金额
        model.setTotalAmount("15.99");
        // 设置订单标题
        model.setSubject(productName);
        // 设置订单附加信息
        model.setBody("Iphone6 16G");
        // 设置订单绝对超时时间
        model.setTimeExpire("2025-02-15 22:00:00");

        request.setBizModel(model);

        com.alipay.api.response.AlipayTradePrecreateResponse response = alipayClient.execute(request);
        //解析结果
        String httpBody = response.getBody();
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

    //todo 支付结果如何获知？有回调设置？or 定时任务查询？
    @PostMapping("/payForPC")
    public String payForPC() throws Exception {

        //构造客户端
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi-sandbox.dl.alipaydev.com/gateway.do");
        alipayConfig.setAppId(APP_ID);
        alipayConfig.setPrivateKey(APP_PRIVATE_KEY);
        alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        // 初始化client
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);

        // 构造请求参数以调用接口
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        // 设置商户订单号
        model.setOutTradeNo(genTradeNo());
        // 设置订单总金额
        model.setTotalAmount(totalAmount);
        // 设置订单标题
        model.setSubject("Iphone6 16G 1TB");
        // 设置产品码
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        // 如果需要返回GET请求，请使用
        // AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
        String pageRedirectionData = response.getBody();
        log.info("payForPC, res:{}", pageRedirectionData);
        return pageRedirectionData;

    }

    private String genTradeNo() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        LocalDate now = LocalDate.now();
        String tradeNo = now.toString().replace("-", "") + uuid;
        return tradeNo;
    }

}
