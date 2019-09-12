package com.tch.test.netty.im.server.handler.client

import com.tch.test.netty.im.server.handler.common.TcpMessageDecoder
import com.tch.test.netty.im.server.handler.common.TcpMessageEncoder
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

class ClientChannelInitializer(private val userId: String): ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline()
            .addLast(TcpMessageDecoder())
            .addLast(TcpMessageEncoder())
            .addLast(RpcClientHandler(userId))
    }

}