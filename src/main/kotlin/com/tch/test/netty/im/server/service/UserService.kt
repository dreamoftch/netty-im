package com.tch.test.netty.im.server.service

import com.google.inject.Inject
import com.tch.test.netty.im.server.mapper.UserMapper
import com.tch.test.netty.im.server.po.User
import org.mybatis.guice.transactional.Transactional

class UserService {

    @Inject
    private lateinit var userMapper: UserMapper

    @Transactional
    fun getUser(userId: Long): User {
        return this.userMapper.getUser(userId)
    }

}