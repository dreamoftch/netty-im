package com.llb.test.im.server.mapper

import com.llb.test.im.server.po.User
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

interface UserMapper {

    @Select("SELECT * FROM user_test WHERE id = #{userId}")
    fun getUser(@Param("userId") userId: Long): User

}