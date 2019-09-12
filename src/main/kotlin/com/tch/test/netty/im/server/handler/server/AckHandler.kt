package com.tch.test.netty.im.server.handler.server

import com.google.inject.Inject
import com.tch.test.netty.im.server.common.ChatMessageType
import com.tch.test.netty.im.server.common.Message
import com.tch.test.netty.im.server.common.SYSTEM_USER_ID
import com.tch.test.netty.im.server.common.TOKEN
import com.tch.test.netty.im.server.service.ChatMessageService
import com.tch.test.netty.im.server.service.UserChannelService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class AckHandler: SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Inject
    private lateinit var chatMessageService: ChatMessageService

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        if (msg.messageType != ChatMessageType.ACK) {
            ctx.fireChannelRead(msg)
            return
        }
        // 更新消息的ack已收到
        chatMessageService.markAckReceived(msg)
    }

}