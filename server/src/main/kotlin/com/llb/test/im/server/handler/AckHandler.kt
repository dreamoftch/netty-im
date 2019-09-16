package com.llb.test.im.server.handler

import com.google.inject.Inject
import com.llb.test.im.common.msg.MessageType
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.server.service.ChatMessageService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class AckHandler: SimpleChannelInboundHandler<IMMessage>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Inject
    private lateinit var chatMessageService: ChatMessageService

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        if (msg.messageType != MessageType.ACK) {
            ctx.fireChannelRead(msg)
            return
        }
        // 更新消息的ack已收到
        chatMessageService.markAckReceived(msg)
    }

}