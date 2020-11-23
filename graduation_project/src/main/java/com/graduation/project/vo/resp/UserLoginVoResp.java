package com.graduation.project.vo.resp;

import lombok.Data;

/**
 * 这个类很重要，不但在登录时会返回给前端，还会通过threadLocal存储在内存中
 */
@Data
public class UserLoginVoResp {
    private String id;
    private String token;
    private long expireTime; //过期时刻
}
