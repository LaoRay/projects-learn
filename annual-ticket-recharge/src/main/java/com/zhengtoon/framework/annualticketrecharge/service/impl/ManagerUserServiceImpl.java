package com.zhengtoon.framework.annualticketrecharge.service.impl;

import com.baomidou.kaptcha.Kaptcha;
import com.baomidou.kaptcha.exception.KaptchaIncorrectException;
import com.baomidou.kaptcha.exception.KaptchaNotFoundException;
import com.baomidou.kaptcha.exception.KaptchaTimeoutException;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.PassWordDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.RegisterDTO;
import com.zhengtoon.framework.annualticketrecharge.bean.dto.UserDTO;
import com.zhengtoon.framework.annualticketrecharge.common.exception.ExceptionCode;
import com.zhengtoon.framework.annualticketrecharge.entity.User;
import com.zhengtoon.framework.annualticketrecharge.mapper.UserMapper;
import com.zhengtoon.framework.annualticketrecharge.service.ManagerUserService;
import com.zhengtoon.framework.exception.BusinessException;
import com.zhengtoon.framework.utils.codec.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class ManagerUserServiceImpl implements ManagerUserService {

    @Autowired
    private Kaptcha kaptcha;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserDTO userDTO, HttpServletRequest request) {
        String userName = userDTO.getUserName();
        String passWord = userDTO.getPassWord();
        String securityCode = userDTO.getSecurityCode();
        //账号密码
        User select = new User();
        select.setUserName(userName);
        User dbUser = userMapper.selectOne(select);
        if (dbUser == null) {
            throw new BusinessException(ExceptionCode.LOGIN_ERROR);
        } else {
            if (!passWord.equals(dbUser.getPassWord())) {
                throw new BusinessException(ExceptionCode.PASSWORD_ERROR);
            }
        }
        //验证码
        validateCode(securityCode);
        //将登陆用户放入session
        User user = userMapper.selectByUserName(userName);
        request.getSession().setAttribute("user", user);
        user.setPassWord(null);
        return user;
    }

    @Override
    public void changePassword(PassWordDTO password, HttpServletRequest request) {
        //取出登陆用户
        User select = (User) request.getSession().getAttribute("user");
        User user = userMapper.selectByUserName(select.getUserName());
        if (!user.getPassWord().equals(password.getOldPassWord())) {
            throw new BusinessException(ExceptionCode.PASSWORD_ERROR);
        } else {
            String newPassWord = password.getNewPassWord();
            String againPassWord = password.getAgainPassWord();
            if (!newPassWord.equals(againPassWord)) {
                throw new BusinessException(ExceptionCode.COMPARE_PASSWORD);
            } else {
                user.setPassWord(newPassWord);
                Integer result = userMapper.updateById(user);
                if (result == 0) {
                    throw new BusinessException(ExceptionCode.UPDATE_PASSWORD);
                } else {
                    request.getSession().setAttribute("user", user);
                }
            }
        }
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        String password = Md5Utils.md5(registerDTO.getPassWord());
        User user = new User();
        user.setPassWord(password);
        user.setUserName(registerDTO.getUserName());
        user.setPersonName(registerDTO.getPersonName());
        Integer result = userMapper.insert(user);
        if (result == 0) {
            throw new BusinessException(ExceptionCode.REGISTER_ERROR);
        }
    }

    /**
     * 验证码
     *
     * @param securityCode
     */
    private void validateCode(String securityCode) {
        try {
            kaptcha.validate(securityCode, 300);
        } catch (KaptchaTimeoutException a) {
            throw new BusinessException(ExceptionCode.SECURITYCODE_OVERTIME);
        } catch (KaptchaIncorrectException b) {
            throw new BusinessException(ExceptionCode.SECURITYCODE_ERROR);
        } catch (KaptchaNotFoundException c) {
            throw new BusinessException(ExceptionCode.SECURITYCODE_NONENTITY);
        }
    }
}
