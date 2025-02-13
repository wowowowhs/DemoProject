package com.order.config;


import com.alipay.easysdk.kernel.Config;
import com.order.config.prop.AliPayProperties;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AlipayConfig {

    @Bean
    public Config Config(AliPayProperties aliPayProperties) {
        Config config = new Config();
        config.protocol = aliPayProperties.getProtocol();
        config.gatewayHost = aliPayProperties.getGatewayHost();
        config.signType = aliPayProperties.getSignType();
        config.appId = aliPayProperties.getAppId();
        config.merchantPrivateKey = aliPayProperties.getMerchantPrivateKey();
        config.alipayPublicKey = aliPayProperties.getAlipayPublicKey();
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = aliPayProperties.getNotifyUrl();
        config.encryptKey = "";
        return config;
    }

}
