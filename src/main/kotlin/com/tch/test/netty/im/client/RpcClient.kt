package com.tch.test.netty.im.client

import com.tch.test.netty.im.handler.client.ClientChannelInitializer
import com.tch.test.netty.im.common.SERVER_PORT
import com.tch.test.netty.im.common.Message
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread


class RpcClient {

    private lateinit var workerGroup: NioEventLoopGroup

    private fun connect(): ChannelFuture {
        val b = Bootstrap()
        workerGroup = NioEventLoopGroup()
        b.group(workerGroup)
        b.channel(NioSocketChannel::class.java)
        b.option(ChannelOption.SO_KEEPALIVE, true)
        b.handler(ClientChannelInitializer())
        return b.connect("localhost", SERVER_PORT).sync()
    }

    fun start() {
        try {
            val f = connect()
            thread {
                var channel = f.channel()
                val input = BufferedReader(InputStreamReader(System.`in`))
                while (true) {
                    val content = input.readLine()
                    val msg = Message().apply {
                        this.type = 1
                        this.content = content
                    }
                    if (!channel.isActive || !channel.isOpen) {
                        // reconnect
                        channel = connect().channel()
                    }
                    channel.writeAndFlush(msg)
                }
            }
            f.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
        }
    }

}



fun main(args: Array<String>) {
    RpcClient().start()
}