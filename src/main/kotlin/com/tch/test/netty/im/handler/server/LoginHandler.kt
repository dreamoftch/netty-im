package com.tch.test.netty.im.handler.server

import com.tch.test.netty.im.common.Message
import com.tch.test.netty.im.common.SYSTEM_USER_ID
import com.tch.test.netty.im.common.TOKEN
import com.tch.test.netty.im.service.UserChannelService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class LoginHandler: SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        if (msg.token != TOKEN || msg.userId.isBlank()) {
            logger.error("用户${msg.userId}登录失败")
            ctx.close()
            return
        }
        logger.info("用户${msg.userId}登录成功")
        UserChannelService.addChannel(msg.userId, ctx.channel())
        UserChannelService.sendMsgToAllUser(Message.build("用户${msg.userId}登陆", SYSTEM_USER_ID))
        ctx.pipeline().remove(LoginHandler::class.java)
    }

}