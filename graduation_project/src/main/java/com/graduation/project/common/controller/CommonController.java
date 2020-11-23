package com.graduation.project.common.controller;

import com.graduation.project.common.resp.RespData;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommonController implements ErrorController {

    @GetMapping("/notLogin.anon")
    public RespData notLogin() {
        return RespData.fail(700, "未登录");
    }

    /**
     * 接口由spring管理，请求的接口404时自动转发到这里来，需要实现ErrorController
     * @return
     */
    @RequestMapping("/error")
    @ResponseBody
    public RespData error() {
        return RespData.fail(404, "页面走丢了");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
