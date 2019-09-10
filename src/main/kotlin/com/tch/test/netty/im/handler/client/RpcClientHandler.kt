package com.tch.test.netty.im.handler.client

import com.tch.test.netty.im.common.Message
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class RpcClientHandler: SimpleChannelInboundHandler<Message>() {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        println("${msg.userId}说: ${msg.content}")
    }

}