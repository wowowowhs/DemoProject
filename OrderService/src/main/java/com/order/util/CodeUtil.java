package com.order.util;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;

/**
 * 二维码生成工具类
 */
public class CodeUtil {

    public static void main(String[] args) {
        QrConfig config = new QrConfig();
        /**
         * L、M、Q、H 由低到高。
         * 低级别的像素块更大，可以远距离识别，但是遮挡就会造成无法识别。
         * 高级别则相反，像素块小，允许遮挡一定范围，但是像素块更密集。
         */
        config.setErrorCorrection(ErrorCorrectionLevel.L);
        config.setWidth(500);
        config.setHeight(500);
        //最后一个参数：制定生成的二维码文件路径
        QrCodeUtil.generate("demo project", config, new File("./test.jpg"));
    }

}
