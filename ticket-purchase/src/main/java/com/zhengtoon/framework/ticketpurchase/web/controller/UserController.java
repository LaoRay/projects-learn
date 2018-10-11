package com.zhengtoon.framework.ticketpurchase.web.controller;

import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.jwt.common.SessionUtils;
import com.zhengtoon.framework.ticketpurchase.bean.vo.UserVO;
import com.zhengtoon.framework.web.common.WebResCallback;
import com.zhengtoon.framework.web.common.WebResCriteria;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Leiqiyun
 * @date 2018/8/10 14:56
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户管理", description = "用户信息管理")
public class UserController {

    @GetMapping("/info")
    public ResponseResult getUserInfo() {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                UserVO user = new UserVO();
                BeanUtils.copyProperties(SessionUtils.getUserInfo(), user);
                criteria.addSingleResult(user);
            }
        }.sendRequest();
    }
}
