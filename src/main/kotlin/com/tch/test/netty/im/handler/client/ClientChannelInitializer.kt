package com.tch.test.netty.im.handler.client

import com.tch.test.netty.im.handler.common.MessageDecoder
import com.tch.test.netty.im.handler.common.MessageEncoder
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel

class ClientChannelInitializer: ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        ch.pipeline()
            .addLast(MessageDecoder())
            .addLast(MessageEncoder())
            .addLast(RpcClientHandler())
    }

}