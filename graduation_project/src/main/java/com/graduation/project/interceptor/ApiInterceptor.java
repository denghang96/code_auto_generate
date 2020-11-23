package com.graduation.project.interceptor;

import com.alibaba.fastjson.JSON;
import com.graduation.project.Application;
import com.graduation.project.common.SystemConst;
import com.graduation.project.common.resp.RespData;
import com.graduation.project.common.util.AuthUtil;
import com.graduation.project.common.util.TokenUtil;
import com.graduation.project.vo.resp.UserLoginVoResp;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiInterceptor extends HandlerInterceptorAdapter {

    private List<String> ignoreList = new ArrayList<>(); //以这个集合中存在的字符串开头的URL请求不进行鉴权

    private List<String> suffixList = new ArrayList<>(); //以这个集合中存在的字符串结尾的URL请求不进行鉴权

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        for (String ignore: ignoreList) {
            if (url.startsWith(ignore)) {
                return true;
            }
        }
        for (String ignore: suffixList) {
            if (url.endsWith(ignore)) {
                return true;
            }
        }
        AuthUtil authUtil = Application.context.getBean(AuthUtil.class);
        authUtil.remove(); //每次请求，先将threadLocal中的用户信息给清空掉
        String token = request.getParameter(SystemConst.TOKEN_NAME);
        UserLoginVoResp userLoginVoResp = TokenUtil.get(token);
        if (!TokenUtil.checkToken(token)) { //检查token是否有效，如果token为空，或者token过期，或者token无效，则提示未登录
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setLocale(new java.util.Locale("zh", "CN"));
            RespData respData = RespData.fail(700, "未登录，请重新登录");
            response.getWriter().println(JSON.toJSONString(respData));
            return false;
        }
        if (userLoginVoResp != null) {
            TokenUtil.token(userLoginVoResp);
            authUtil.set(userLoginVoResp);
        }
        return true;
    }

    public ApiInterceptor() {
        ignoreList.add("/error");
        suffixList.add(".anon");
    }
}
