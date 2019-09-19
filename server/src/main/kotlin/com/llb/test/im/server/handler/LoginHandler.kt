package com.llb.test.im.server.handler

import com.alibaba.fastjson.JSON
import com.google.inject.Inject
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.constant.SYSTEM_USER_ID
import com.llb.test.im.server.service.ChatMessageService
import com.llb.test.im.server.service.UserChannelService
import com.llb.test.im.server.service.UserTokenService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class LoginHandler: SimpleChannelInboundHandler<IMMessage>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Inject
    private lateinit var chatMessageService: ChatMessageService
    @Inject
    private lateinit var userTokenService: UserTokenService
    @Inject
    private lateinit var userChannelService: UserChannelService

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        if (!userTokenService.checkUserToken(msg)) {
            logger.error("用户${msg.sourceUserId}登录失败: ${JSON.toJSONString(msg)}")
            ctx.close()
            return
        }
        logger.debug("用户${msg.sourceUserId}登录成功")
        processLoginSuccessUser(ctx, msg)
        ctx.pipeline().remove(LoginHandler::class.java)
    }

    private fun processLoginSuccessUser(ctx: ChannelHandlerContext, msg: IMMessage) {
        // 把用户channel加入到channel列表中
        userChannelService.addChannel(msg.sourceUserId, ctx.channel())
        // 给其他用户发送登陆提醒
        userChannelService.sendMsgToAllUser(
            IMMessage.build("用户${msg.sourceUserId}登陆",
                SYSTEM_USER_ID
            ))
        // 给该用户发送离线消息
        chatMessageService.listMyMessageWithNoAck(msg.sourceUserId).forEach {
            userChannelService.sendMsgToUser(msg.sourceUserId, it)
        }
    }

}