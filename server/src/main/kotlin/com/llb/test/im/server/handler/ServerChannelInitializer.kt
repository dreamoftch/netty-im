package com.llb.test.im.server.handler

import com.google.inject.Inject
import com.google.inject.Injector
import com.llb.test.im.common.handler.MyIdleStateHandler
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
            .addLast(IdleStateHandler(30, 0, 0)) // 20秒没有收到客户端的消息就触发IdleStateEvent
            .addLast(myIdleStateHandler) // 处理IdleStateEvent
            .addLast(injector.getInstance(TcpWebSocketHandler::class.java)) // 区分处理WebSocket和非WebSocket的handler
    }

}