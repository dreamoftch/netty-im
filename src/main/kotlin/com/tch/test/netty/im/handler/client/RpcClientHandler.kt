package com.tch.test.netty.im.handler.client

import com.alibaba.fastjson.JSON
import com.tch.test.netty.im.common.Message
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class RpcClientHandler: SimpleChannelInboundHandler<Message>() {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        println("客户端收到消息: ${JSON.toJSONString(msg)}")
    }

}