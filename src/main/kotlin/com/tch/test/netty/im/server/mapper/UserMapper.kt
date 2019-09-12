package com.tch.test.netty.im.server.mapper

import com.tch.test.netty.im.server.po.User
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

interface UserMapper {

    @Select("SELECT * FROM user_test WHERE id = #{userId}")
    fun getUser(@Param("userId") userId: Long): User

}