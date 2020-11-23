package com.graduation.project.service;

import com.graduation.project.vo.req.UserLoginVo;
import com.graduation.project.vo.resp.UserLoginVoResp;

public interface LoginService {
    UserLoginVoResp login(UserLoginVo userLoginVo);
}
