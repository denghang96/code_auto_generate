package com.graduation.project.service.impl;

import com.graduation.project.common.util.TokenUtil;
import com.graduation.project.dao.LoginMapper;
import com.graduation.project.service.LoginService;
import com.graduation.project.vo.req.UserLoginVo;
import com.graduation.project.vo.resp.UserLoginVoResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    LoginMapper loginMapper;

    @Override
    public UserLoginVoResp login(UserLoginVo userLoginVo) {
        UserLoginVoResp userLoginVoResp = loginMapper.loginQuery(userLoginVo);
        if (userLoginVoResp != null) {
            TokenUtil.token(userLoginVoResp);
        }
        return userLoginVoResp;
    }
}
