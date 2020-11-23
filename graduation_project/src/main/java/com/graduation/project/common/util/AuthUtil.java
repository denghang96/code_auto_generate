package com.graduation.project.common.util;

import com.graduation.project.vo.resp.UserLoginVoResp;
import org.springframework.stereotype.Component;

/**
 * 提供一个类，用来很方便的获取当前用户信息
 */
@Component
public class AuthUtil {
    private final ThreadLocal<UserLoginVoResp> threadLocal = new ThreadLocal<>();

    public UserLoginVoResp get() {
        return threadLocal.get();
    }

    public void set(UserLoginVoResp userLoginVoResp) {
        threadLocal.set(userLoginVoResp);
    }

    public void remove() {
        threadLocal.remove();
    }
}
