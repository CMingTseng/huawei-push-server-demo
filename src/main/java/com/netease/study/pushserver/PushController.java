package com.netease.study.pushserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hanpfei0306 on 18-8-23.
 */
@Controller
@RequestMapping("/mob/s2/push")
public class PushController {
    @RequestMapping("/callback/xiaomi/v{ver}")
    @ResponseBody
    public String callback(HttpServletRequest request){
        String callback = request.getParameter("data");
        System.out.println("callback data: " + callback);
        return "ok";
    }
}
