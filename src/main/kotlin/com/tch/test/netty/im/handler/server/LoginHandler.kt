package com.tch.test.netty.im.handler.server

import com.tch.test.netty.im.common.Message
import com.tch.test.netty.im.common.TOKEN
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class LoginHandler: SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        if (msg.token != TOKEN || msg.userId == null) {
            logger.error("用户${msg.userId}登录失败")
            ctx.close()
            return
        }
        logger.error("用户${msg.userId}登录成功")
        ctx.pipeline().remove(LoginHandler::class.java)
    }

}