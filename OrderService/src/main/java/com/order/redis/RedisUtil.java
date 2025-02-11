package com.order.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class RedisUtil {

    private DefaultRedisScript<Integer> productStockLuaScript;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        productStockLuaScript = new DefaultRedisScript<>();
        productStockLuaScript.setResultType(Integer.class);
        productStockLuaScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/product_deduct.lua")));
    }

    //库存扣减
    public Integer deductStock(String key, Integer deductStock) {

        Integer number = (Integer) redisTemplate.execute(productStockLuaScript, Collections.singletonList(key), deductStock);
        if (number != null && number.intValue() > 0 ) {
            System.out.println("扣减库存成功。。。。");
        }else {
            System.out.println("扣减库存失败。。。");
        }
        return number;

    }
}