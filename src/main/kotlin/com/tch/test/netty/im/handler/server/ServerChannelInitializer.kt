package com.tch.test.netty.im.handler.server

import com.tch.test.netty.im.handler.common.MessageDecoder
import com.tch.test.netty.im.handler.common.MessageEncoder
import com.tch.test.netty.im.handler.common.MyIdleStateHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler

class ServerChannelInitializer: ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline()
            .addLast(IdleStateHandler(30, 0, 0)) // 30秒没有收到客户端的消息就触发IdleStateEvent
            .addLast(MyIdleStateHandler()) // 处理IdleStateEvent
            .addLast(MessageDecoder()) // 消息解码
            .addLast(MessageEncoder()) // 消息编码
            .addLast(ServerHandler()) // 处理消息
    }

}