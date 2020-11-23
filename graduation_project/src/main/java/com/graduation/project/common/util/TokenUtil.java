package com.graduation.project.common.util;

import com.graduation.project.Application;
import com.graduation.project.config.SystemProperties;
import com.graduation.project.vo.resp.UserLoginVoResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TokenUtil {

    //用来存放用户登录信息的Map
    private static Map<String, UserLoginVoResp> tokenMap = new HashMap<>();

    /**
     * 设置token
     * @param token
     * @param userLoginVoResp
     */
    public static void set(String token, UserLoginVoResp userLoginVoResp){
        tokenMap.put(token, userLoginVoResp);
    }

    public static void remove(String token) {
        tokenMap.remove(token);
    }
    /**
     * 根据token获取user
     * @param token
     * @return
     */
    public static UserLoginVoResp get(String token){
        return tokenMap.get(token);
    }

    //检查token是否有效
    public static boolean checkToken(String token) {
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis(); //当前时刻

        UserLoginVoResp userLoginVoResp = tokenMap.get(token);
        if (StringUtils.isEmpty(token) || userLoginVoResp == null) {
            return false;
        }

        long expireTime = userLoginVoResp.getExpireTime(); //token的过期时刻
        if (expireTime < timeInMillis) { //token过期
            tokenMap.remove(token);
            return false;
        }
        return true;
    }

    public static void token(UserLoginVoResp userLoginVoResp) {
        SystemProperties systemProperties =  Application.context.getBean(SystemProperties.class);
        Integer tokenExpireTime = systemProperties.getTokenExpireTime();
        log.info("token: {} ,过期时长：{}",userLoginVoResp.getToken(),tokenExpireTime);
        Calendar calendar = Calendar.getInstance();
        long expireTime = calendar.getTimeInMillis() + tokenExpireTime*1000;
        calendar.setTimeInMillis(expireTime);
        Date time = calendar.getTime();
        log.info("token: {} ,过期时刻：{}",userLoginVoResp.getToken(),time);
        String token = userLoginVoResp.getId();
        userLoginVoResp.setToken(token);
        userLoginVoResp.setExpireTime(expireTime);
        set(token, userLoginVoResp);
    }
}
