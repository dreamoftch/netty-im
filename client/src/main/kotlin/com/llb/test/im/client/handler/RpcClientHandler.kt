package com.llb.test.im.client.handler

import com.llb.test.im.common.constant.ChatMessageType
import com.llb.test.im.common.constant.Message
import com.llb.test.im.common.constant.SYSTEM_USER_ID
import com.llb.test.im.common.constant.TOKEN
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class RpcClientHandler(private val userId: String): SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        logger.info("${getUserId(msg)}说: ${msg.messageContent}")
        // 收到消息之后回复ack给服务器
        sendAck(ctx.channel(), msg)
    }

    private fun sendAck(channel: Channel, msg: Message) {
        msg.id?.let {
            val ack = Message().apply {
                this.id = it
                this.messageType = ChatMessageType.ACK
            }
            channel.writeAndFlush(ack)
        }
    }

    private fun getUserId(msg: Message): String {
        return if (msg.sourceUserId == SYSTEM_USER_ID) {
            "系统"
        } else {
            msg.sourceUserId
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info("连接建立成功")
        // 连接建立起来之后第一步需要登陆
        val loginMsg = Message().apply {
            this.sourceUserId = this@RpcClientHandler.userId
            this.token = TOKEN
        }
        ctx.writeAndFlush(loginMsg)
    }
}