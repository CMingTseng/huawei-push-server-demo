package com.netease.study.pushserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Created by hanpfei0306 on 18-8-23.
 */
@ComponentScan(basePackages = {"com.netease.study.pushserver"})
@SpringBootApplication
@Import(WebConfiguration.class)
public class PushServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PushServerApplication.class, args);
    }

}
