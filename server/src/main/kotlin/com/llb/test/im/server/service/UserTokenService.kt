package com.llb.test.im.server.service

import com.alibaba.fastjson.JSON
import com.google.inject.Singleton
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.msg.MessageType
import com.llb.test.im.common.constant.TOKEN
import com.llb.test.im.common.msg.LoginMessage
import org.slf4j.LoggerFactory

@Singleton
class UserTokenService {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun checkUserToken(msg: IMMessage): Boolean {
        // TODO
        val body = msg.body ?: return false
        if (msg.messageType != MessageType.LOGIN || msg.sourceUserId.isNotBlank()) {
            return false
        }
        return try {
            val loginMessage = JSON.parseObject(body, LoginMessage::class.java)
            loginMessage.token == TOKEN
        } catch (e: Exception) {
            logger.error("解析登陆消息body异常:", e)
            false
        }
    }

}