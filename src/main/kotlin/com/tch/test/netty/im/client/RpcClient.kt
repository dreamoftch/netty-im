package com.tch.test.netty.im.client

import com.tch.test.netty.im.common.Message
import com.tch.test.netty.im.common.SERVER_PORT
import com.tch.test.netty.im.handler.client.ClientChannelInitializer
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread


class RpcClient {

    companion object {

        var channel: Channel? = null

        private lateinit var workerGroup: NioEventLoopGroup

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                println("请输入您的大名:")
                val input = BufferedReader(InputStreamReader(System.`in`))
                val userId = input.readLine()
                val f = connect(userId)
                thread {
                    channel = f.channel()
                    while (true) {
                        println("请输入对方的大名:")
                        val targetUserId = input.readLine()
                        println("请输入聊天内容:")
                        val content = input.readLine()
                        val msg = Message().apply {
                            this.userId = userId
                            this.targetUserId = targetUserId
                            this.content = content
                        }
                        sendMsg(userId, msg)
                    }
                }
                f.channel().closeFuture().sync()
            } finally {
                workerGroup.shutdownGracefully()
            }
        }

        private fun getChannel(userId: String): Channel {
            val currentChannel = channel
            if (currentChannel == null || !channelAvailable(currentChannel)) {
                return connect(userId).channel()
            }
            return currentChannel
        }

        private fun sendMsg(userId: String, msg: Message) {
            getChannel(userId).writeAndFlush(msg)
        }

        private fun connect(userId: String): ChannelFuture {
            workerGroup = NioEventLoopGroup()
            return Bootstrap()
                .group(workerGroup)
                .handler(LoggingHandler(LogLevel.INFO))
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(ClientChannelInitializer(userId))
                .connect("localhost", SERVER_PORT)
                .sync()
        }

        private fun channelAvailable(channel: Channel): Boolean {
            return channel.isActive && channel.isOpen
        }

    }

}
