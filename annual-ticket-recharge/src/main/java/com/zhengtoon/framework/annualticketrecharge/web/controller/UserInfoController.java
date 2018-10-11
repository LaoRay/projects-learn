package com.zhengtoon.framework.annualticketrecharge.web.controller;

import com.zhengtoon.framework.annualticketrecharge.bean.vo.UserInfoVO;
import com.zhengtoon.framework.annualticketrecharge.common.utils.SecurityUtil;
import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.jwt.bean.dto.UserInfo;
import com.zhengtoon.framework.jwt.common.SessionUtils;
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
@Api(value = "用户信息管理", description = "用户信息管理")
public class UserInfoController {

    @GetMapping("/info")
    public ResponseResult getUserInfo() {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                UserInfoVO user = new UserInfoVO();
                UserInfo userInfo = SessionUtils.getUserInfo();
                // TODO 测试用，待删除
                userInfo.setCertName("张一");
                userInfo.setCertNo("130821198102081111");

                BeanUtils.copyProperties(userInfo, user);
                user.setIdCard(SecurityUtil.maskSensitiveInfo(SessionUtils.getUserInfo().getCertNo(), 6, 14));
                criteria.addSingleResult(user);
            }
        }.sendRequest();
    }
}
