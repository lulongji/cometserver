package com.yuntongxun.comet.controller;


import com.yuntongxun.base.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class HelloController {

    @RequestMapping("/hi")
    @ResponseBody
    public Result hello(HttpServletRequest request) throws Exception {
        log.error("error ：hello，你请求的地址为：" + request.getRequestURL());
        log.info("info ： hello，你请求的地址为：" + request.getRequestURL());
        return Result.success("hello，你请求的地址为：" + request.getRequestURL());
    }


}
