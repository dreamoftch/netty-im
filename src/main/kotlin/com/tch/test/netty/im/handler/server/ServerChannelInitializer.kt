package com.tch.test.netty.im.handler.server

import com.google.inject.Inject
import com.tch.test.netty.im.handler.common.MyIdleStateHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler

class ServerChannelInitializer : ChannelInitializer<SocketChannel>() {

    @Inject
    private lateinit var myIdleStateHandler: MyIdleStateHandler
    @Inject
    private lateinit var tcpWebSocketHandler: TcpWebSocketHandler

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline()
            .addLast(IdleStateHandler(300, 0, 0)) // 30秒没有收到客户端的消息就触发IdleStateEvent
            .addLast(myIdleStateHandler) // 处理IdleStateEvent
            .addLast(tcpWebSocketHandler) // 区分处理WebSocket和非WebSocket的handler
    }

}