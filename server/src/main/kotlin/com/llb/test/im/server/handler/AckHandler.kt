package com.llb.test.im.server.handler

import com.alibaba.fastjson.JSON
import com.google.inject.Inject
import com.llb.test.im.common.msg.MessageType
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.server.common.ChatMessageStatus
import com.llb.test.im.server.extension.toJson
import com.llb.test.im.server.service.ChatMessageService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

/**
 * 客户端发送给服务器的ack处理,表示客户端已经成功的接收到该消息了
 */
class AckHandler: SimpleChannelInboundHandler<IMMessage>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Inject
    private lateinit var chatMessageService: ChatMessageService

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        if (msg.messageType != MessageType.ACK) {
            ctx.fireChannelRead(msg)
            return
        }
        logger.debug("收到ack ${msg.toJson()}")
        // 更新消息的status为已收到
        chatMessageService.updateMsgStatus(msg.requestId, ChatMessageStatus.RECEIVED.code)
    }

}