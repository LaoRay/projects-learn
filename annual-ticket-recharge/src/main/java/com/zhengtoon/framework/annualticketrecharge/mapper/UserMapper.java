package com.zhengtoon.framework.annualticketrecharge.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zhengtoon.framework.annualticketrecharge.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    /**根据用户名查询记录
     * @param userName 用户名
     * @return 用户
     */
    User selectByUserName(@Param("userName") String userName);
}
