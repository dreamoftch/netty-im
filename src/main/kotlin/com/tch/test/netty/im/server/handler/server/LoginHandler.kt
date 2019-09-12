package com.tch.test.netty.im.server.handler.server

import com.google.inject.Inject
import com.tch.test.netty.im.server.common.Message
import com.tch.test.netty.im.server.common.SYSTEM_USER_ID
import com.tch.test.netty.im.server.common.TOKEN
import com.tch.test.netty.im.server.service.ChatMessageService
import com.tch.test.netty.im.server.service.UserChannelService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class LoginHandler: SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Inject
    private lateinit var chatMessageService: ChatMessageService

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        if (msg.token != TOKEN || msg.sourceUserId.isBlank()) {
            logger.error("用户${msg.sourceUserId}登录失败")
            ctx.close()
            return
        }
        logger.info("用户${msg.sourceUserId}登录成功")
        // 把用户channel加入到channel列表中
        UserChannelService.addChannel(msg.sourceUserId, ctx.channel())
        // 给其他用户发送登陆提醒
        UserChannelService.sendMsgToAllUser(
            Message.build("用户${msg.sourceUserId}登陆",
                SYSTEM_USER_ID
            ))
        // 给该用户发送离线消息
        chatMessageService.listMyMessageWithNoAck(msg.sourceUserId).forEach {
            UserChannelService.sendMsgToUser(msg.sourceUserId, it)
        }
        ctx.pipeline().remove(LoginHandler::class.java)
    }

}