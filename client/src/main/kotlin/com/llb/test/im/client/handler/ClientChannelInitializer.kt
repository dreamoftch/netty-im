package com.llb.test.im.client.handler

import com.llb.test.im.common.handler.TcpMessageDecoder
import com.llb.test.im.common.handler.TcpMessageEncoder
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

class ClientChannelInitializer(private val userId: String): ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline()
            .addLast(TcpMessageDecoder())
            .addLast(TcpMessageEncoder())
            .addLast(AckHandler())
            .addLast(RpcClientHandler(userId))
    }

}