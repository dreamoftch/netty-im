package com.tch.test.netty.im.handler.common

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

class MyIdleStateHandler: ChannelDuplexHandler() {

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        println("${ctx.channel().remoteAddress()} userEventTriggered $evt")
        if (evt is IdleStateEvent) {
            if (evt.state() === IdleState.READER_IDLE) {
                println("${ctx.channel().remoteAddress()} 很久没有发送过消息啦，服务端即将断开该客户端的连接")
                ctx.close()
            }
        }
    }
}