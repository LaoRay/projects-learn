package com.zhengtoon.framework.annualticketrecharge.bean.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author 赵昆圣
 */
@Data
public class UserDTO {

    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String passWord;
    @NotBlank(message = "校验码不能为空")
    private String securityCode;
}
