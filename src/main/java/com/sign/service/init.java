package com.sign.service;

import com.sign.tool.Param;
import com.sign.tool.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class init {

    Logger log= LoggerFactory.getLogger(init.class);

    //启动是初始化参数

    @PostConstruct
    public void demo(){
        //初始化参数
        Param.init();
        Request.resetToken();
        log.info("参数初始化成功");
    }
}
