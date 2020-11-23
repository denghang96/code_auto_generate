package com.graduation.project.controller;

import com.graduation.project.common.resp.RespData;
import com.graduation.project.common.util.AuthUtil;
import com.graduation.project.common.util.TokenUtil;
import com.graduation.project.service.LoginService;
import com.graduation.project.vo.req.UserLoginVo;
import com.graduation.project.vo.resp.UserLoginVoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    LoginService loginService;

    @Autowired
    AuthUtil authUtil;

    @GetMapping("/login.anon")
    public RespData login(UserLoginVo userLoginVo) {
        UserLoginVoResp sysUser = loginService.login(userLoginVo);
        if (sysUser != null) {
            return RespData.success(sysUser);
        }
        return RespData.fail("密码错误");
    }

    @GetMapping("/loginOut")
    public RespData loginOut() {
        String token = authUtil.get().getToken();
        TokenUtil.remove(token);
        return RespData.success();
    }
}
