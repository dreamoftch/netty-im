package com.llb.test.im.client.handler

import com.alibaba.fastjson.JSON
import com.llb.test.im.common.msg.MessageType
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.constant.SYSTEM_USER_ID
import com.llb.test.im.common.constant.TOKEN
import com.llb.test.im.common.msg.LoginMessage
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory
import java.util.*

class RpcClientHandler(private val userId: String): SimpleChannelInboundHandler<IMMessage>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        logger.info("${getUserId(msg)}说: ${msg.body}")
        // 收到消息之后回复ack给服务器
        sendAck(ctx.channel(), msg)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info("连接建立成功")
        // 连接建立起来之后第一步需要登陆
        login(ctx)
    }

    private fun login(ctx: ChannelHandlerContext) {
        val loginMessage = LoginMessage().apply {
            this.token = TOKEN
        }
        val loginMsg = IMMessage().apply {
            this.messageType = MessageType.LOGIN
            this.sourceUserId = this@RpcClientHandler.userId
            this.requestId = UUID.randomUUID().toString()
            this.body = JSON.toJSONString(loginMessage)
        }
        ctx.writeAndFlush(loginMsg)
    }

    private fun sendAck(channel: Channel, msg: IMMessage) {
        if (msg.messageType == MessageType.CHAT) {
            // 聊天消息
            msg.requestId?.let {
                val ack = IMMessage().apply {
                    this.requestId = it
                    this.messageType = MessageType.ACK
                }
                channel.writeAndFlush(ack)
            }
        }
    }

    private fun getUserId(msg: IMMessage): String {
        return if (msg.sourceUserId == SYSTEM_USER_ID) {
            "系统"
        } else {
            msg.sourceUserId
        }
    }
}