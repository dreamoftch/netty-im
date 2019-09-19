package com.llb.test.im.server.handler

import com.google.inject.Inject
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.msg.MessageType
import com.llb.test.im.server.extension.toJson
import com.llb.test.im.server.service.UserChannelService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

/**
 * 客户端发送给服务器的心跳包处理，只需要返回ack即可
 */
class HeartBeatHandler: SimpleChannelInboundHandler<IMMessage>() {

    private val logger = LoggerFactory.getLogger(javaClass)
    @Inject
    private lateinit var userChannelService: UserChannelService

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        if (msg.messageType != MessageType.HEARTBEAT) {
            ctx.fireChannelRead(msg)
            return
        }
        val requestId = msg.requestId ?: return
        logger.debug("收到heartBeat ${msg.toJson()}")
        // 告诉客户端ack已收到
        userChannelService.sendAckToUser(ctx.channel(), requestId)
    }

}