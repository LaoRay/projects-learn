package com.zhengtoon.framework.annualticketrecharge.service;

import com.zhengtoon.framework.annualticketrecharge.bean.dto.PassWordDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.RegisterDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.UserDTO;
import com.zhengtoon.framework.annualticketrecharge.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface ManagerUserService {
    /**
     * 登陆验证
     *
     * @param userDTO
     * @param request
     * @return
     */
    User login(UserDTO userDTO, HttpServletRequest request);

    /**
     * 修改密码
     *
     * @param password
     * @param request
     */
    void changePassword(PassWordDTO password, HttpServletRequest request);

    /**
     * 注册
     *
     * @param registerDTO 注册信息
     */
    void register(RegisterDTO registerDTO);
}
