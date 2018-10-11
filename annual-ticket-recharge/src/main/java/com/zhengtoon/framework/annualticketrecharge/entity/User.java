package com.zhengtoon.framework.annualticketrecharge.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhengtoon.framework.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zks
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
public class User extends BaseEntity {
    /**
     * 唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 密码
     */
    @TableField("pass_word")
    private String passWord;
    /**
     * 角色名
     */
    @TableField("person_name")
    private String personName;
}
