package com.zhengtoon.framework.annualticketrecharge.bean.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author 赵昆圣
 */
@Data
public class PassWordDTO {
    @NotBlank(message = "请输入原密码")
    private String oldPassWord;

    @NotBlank(message = "请输入新密码")
    private String newPassWord;

    @NotBlank(message = "请再次输入新密码")
    private String againPassWord;
}
