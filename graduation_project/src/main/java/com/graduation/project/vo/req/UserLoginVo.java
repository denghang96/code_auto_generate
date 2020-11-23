package com.graduation.project.vo.req;

import lombok.Data;

@Data
public class UserLoginVo {
    private String userName;
    private String password;
    private String userType;
}
