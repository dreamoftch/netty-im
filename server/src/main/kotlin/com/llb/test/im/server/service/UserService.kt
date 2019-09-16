package com.llb.test.im.server.service

import com.google.inject.Inject
import com.llb.test.im.server.mapper.UserMapper
import com.llb.test.im.server.po.User
import org.mybatis.guice.transactional.Transactional

class UserService {

    @Inject
    private lateinit var userMapper: UserMapper

    @Transactional
    fun getUser(userId: Long): User {
        return this.userMapper.getUser(userId)
    }

}