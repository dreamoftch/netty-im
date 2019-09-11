package com.tch.test.netty.im.handler.common

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import org.slf4j.LoggerFactory

class MyIdleStateHandler: ChannelDuplexHandler() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent) {
            logger.info("${ctx.channel().remoteAddress()} userEventTriggered ${evt.state()}")
            if (evt.state() === IdleState.READER_IDLE) {
                logger.info("${ctx.channel().remoteAddress()} 很久没有发送过消息啦，服务端即将断开该客户端的连接")
                ctx.close()
            }
        }
    }
}