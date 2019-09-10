package com.tch.test.netty.im.handler.client

import com.tch.test.netty.im.handler.common.TcpMessageDecoder
import com.tch.test.netty.im.handler.common.TcpMessageEncoder
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

class ClientChannelInitializer: ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline()
            .addLast(TcpMessageDecoder())
            .addLast(TcpMessageEncoder())
            .addLast(RpcClientHandler())
    }

}