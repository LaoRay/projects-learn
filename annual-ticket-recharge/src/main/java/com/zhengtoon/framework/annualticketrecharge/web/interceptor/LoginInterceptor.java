package com.zhengtoon.framework.annualticketrecharge.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.zhengtoon.framework.annualticketrecharge.common.exception.ExceptionCode;
import com.zhengtoon.framework.annualticketrecharge.entity.User;
import com.zhengtoon.framework.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zks
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            returnFailResponse(response);
            return false;
        }
        return true;
    }

    /**
     * 返回失败的信息
     *
     * @param response 返回对象
     * @throws IOException IOException
     */
    private void returnFailResponse(HttpServletResponse response) throws IOException {
        response.setHeader("Content-type", "text/json;charset=UTF-8");
        response.getWriter().print(JSON.toJSON(new ResponseResult<>(ExceptionCode.LOGIN_FAIL)));
    }
}
