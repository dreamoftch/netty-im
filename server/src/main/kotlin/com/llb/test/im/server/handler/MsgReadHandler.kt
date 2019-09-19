package com.llb.test.im.server.handler

import com.google.inject.Inject
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.msg.MessageType
import com.llb.test.im.server.common.ChatMessageStatus
import com.llb.test.im.server.extension.toJson
import com.llb.test.im.server.service.ChatMessageService
import com.llb.test.im.server.service.UserChannelService
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

/**
 * 消息接收者消息已读
 */
class MsgReadHandler: SimpleChannelInboundHandler<IMMessage>() {

    private val logger = LoggerFactory.getLogger(javaClass)
    @Inject
    private lateinit var userChannelService: UserChannelService
    @Inject
    private lateinit var chatMessageService: ChatMessageService

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        if (msg.messageType != MessageType.MSG_READ) {
            ctx.fireChannelRead(msg)
            return
        }
        val requestId = msg.requestId ?: return
        logger.debug("收到msgRead ${msg.toJson()}")
        // 更新消息的status为已读
        chatMessageService.updateMsgStatus(msg.requestId, ChatMessageStatus.RECEIVED.code)
        // 告诉客户端ack已收到
        userChannelService.sendAckToUser(ctx.channel(), requestId)
        // 通知消息发送人消息已读
        chatMessageService.getByRequestId(requestId)?.let {
            val msgReadMessage = IMMessage().apply {
                this.messageType = MessageType.MSG_READ
                this.requestId = requestId
            }
            userChannelService.sendMsgToUser(it.sourceUserId, msgReadMessage)
        }

    }

}