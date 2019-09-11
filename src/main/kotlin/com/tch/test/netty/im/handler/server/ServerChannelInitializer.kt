package com.tch.test.netty.im.handler.server

import com.google.inject.Inject
import com.google.inject.Injector
import com.tch.test.netty.im.handler.common.MyIdleStateHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler

class ServerChannelInitializer : ChannelInitializer<SocketChannel>() {

    @Inject
    private lateinit var myIdleStateHandler: MyIdleStateHandler
    @Inject
    private lateinit var injector: Injector

    override fun initChannel(ch: SocketChannel) {
        // first of all, current ServerChannelInitializer is singleton,since we inject the instance at IMServer
        // thus lead to TcpWebSocketHandler is not Sharable exception,
        // but TcpWebSocketHandler is subclass of ByteToMessageDecoder,because ByteToMessageDecoder cannot annotated with Sharable
        // so, we have to use injector to get a new TcpWebSocketHandler instance every time here.
        ch.pipeline()
            .addLast(IdleStateHandler(300, 0, 0)) // 30秒没有收到客户端的消息就触发IdleStateEvent
            .addLast(myIdleStateHandler) // 处理IdleStateEvent
            .addLast(injector.getInstance(TcpWebSocketHandler::class.java)) // 区分处理WebSocket和非WebSocket的handler
    }

}