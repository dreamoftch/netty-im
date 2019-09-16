package com.llb.test.im.server.handler

import com.google.inject.Inject
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.msg.MessageType
import com.llb.test.im.server.service.UserChannelService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class HeartBeatHandler: SimpleChannelInboundHandler<IMMessage>() {

    @Inject
    private lateinit var userChannelService: UserChannelService

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        if (msg.messageType != MessageType.HEARTBEAT) {
            ctx.fireChannelRead(msg)
            return
        }
        val requestId = msg.requestId ?: return
        // 告诉客户端ack已收到
        userChannelService.sendAckToUser(ctx.channel(), requestId)
    }

}