package com.tch.test.netty.im.client

import com.tch.test.netty.im.common.Message
import com.tch.test.netty.im.common.SERVER_PORT
import com.tch.test.netty.im.handler.client.ClientChannelInitializer
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.concurrent.thread


class RpcClient {

    companion object {

        private lateinit var workerGroup: NioEventLoopGroup

        private fun connect(): ChannelFuture {
            workerGroup = NioEventLoopGroup()
            return Bootstrap()
                .group(workerGroup)
                .handler(LoggingHandler(LogLevel.INFO))
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(ClientChannelInitializer())
                .connect("localhost", SERVER_PORT)
                .sync()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val f = connect()
                thread {
                    var channel = f.channel()
                    val input = BufferedReader(InputStreamReader(System.`in`))
                    while (true) {
                        val content = input.readLine()
                        val msg = Message().apply {
                            this.userId = UUID.randomUUID().toString()
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

}
