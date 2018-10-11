package com.zhengtoon.framework.annualticketrecharge.web.controller;

import com.zhengtoon.framework.annualticketrecharge.bean.dto.PassWordDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.RegisterDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.UserDTO;
import com.zhengtoon.framework.annualticketrecharge.service.ManagerUserService;
import com.zhengtoon.framework.entity.ResponseResult;
import com.zhengtoon.framework.web.common.WebResCallback;
import com.zhengtoon.framework.web.common.WebResCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author zks
 */
@RestController
@RequestMapping("/manager/user")
@Api(value = "后台用户管理", description = "后台用户信息管理")
public class ManagerUserController {

    @Autowired
    private ManagerUserService userService;

    /**
     * 登陆
     *
     * @param request request
     * @param userDTO userDTO
     * @return user
     */
    @PostMapping("/login")
    @ApiOperation(value = "后台登陆", httpMethod = "POST", notes = "后台登陆")
    public ResponseResult login(final HttpServletRequest request, @RequestBody final UserDTO userDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                criteria.addSingleResult(userService.login(userDTO, request));
            }
        }.sendRequest(userDTO);
    }

    /**
     * 登出
     *
     * @param request request
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "后台登陆注销", httpMethod = "POST", notes = "后台登陆注销")
    public ResponseResult logout(final HttpServletRequest request) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                request.getSession().removeAttribute("user");
            }
        }.sendRequest();
    }

    /**
     * 修改密码
     *
     * @param password password
     * @return
     */
    @PostMapping("/changePassword")
    @ApiOperation(value = "修改密码", httpMethod = "POST", notes = "修改密码")
    public ResponseResult changePassword(final HttpServletRequest request, @RequestBody final PassWordDTO password) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                userService.changePassword(password, request);
                criteria.addSingleResult("修改成功");
            }
        }.sendRequest(password);
    }

    /**
     * 注册
     *
     * @param registerDTO registerDTO
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "注册", httpMethod = "POST", notes = "注册")
    public ResponseResult register(@Valid @RequestBody final RegisterDTO registerDTO) {
        return new WebResCallback() {
            @Override
            public void execute(WebResCriteria criteria, Object... params) {
                userService.register(registerDTO);
                criteria.addSingleResult("注册成功");
            }
        }.sendRequest(registerDTO);
    }
}
