package com.llb.test.im.client.handler

import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.msg.MessageType
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class AckHandler: SimpleChannelInboundHandler<IMMessage>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        if (msg.messageType != MessageType.ACK) {
            ctx.fireChannelRead(msg)
            return
        }
        // 更新消息的ack已收到
        logger.info("收到服务器的ack ${msg.requestId}")
    }

}