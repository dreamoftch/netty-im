package com.tch.test.netty.im.handler.client

import com.tch.test.netty.im.common.Message
import com.tch.test.netty.im.common.TOKEN
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class RpcClientHandler(private val userId: String): SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        logger.info("${msg.userId}说: ${msg.content}")
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        logger.info("连接建立成功")
        // 连接建立起来之后第一步需要登陆
        val loginMsg = Message().apply {
            this.userId = this@RpcClientHandler.userId
            this.token = TOKEN
        }
        ctx.writeAndFlush(loginMsg)
    }
}