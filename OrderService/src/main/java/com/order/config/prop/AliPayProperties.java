package com.order.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "alipay.easy")
public class AliPayProperties {

    //请求协议
    private String protocol;

    //请求网关
    private String gatewayHost;

    //签名类型 RSA2
    private String signType;

    //应用appId
    private String appId;

    //应用私钥
    private String merchantPrivateKey;

    //支付宝公钥
    private String alipayPublicKey;

    //异步通知接收服务的地址
    private String notifyUrl;

    //设置AES秘钥
    private String encryptKey;

}
