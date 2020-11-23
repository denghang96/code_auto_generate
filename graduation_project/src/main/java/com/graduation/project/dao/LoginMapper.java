package com.graduation.project.dao;

import com.graduation.project.vo.req.UserLoginVo;
import com.graduation.project.vo.resp.UserLoginVoResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {
    UserLoginVoResp loginQuery(@Param("userLoginVo") UserLoginVo userLoginVo);
}
