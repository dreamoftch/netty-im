package com.llb.test.im.client

import com.llb.test.im.client.handler.ClientChannelInitializer
import com.llb.test.im.common.constant.Message
import com.llb.test.im.common.constant.SERVER_PORT
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.concurrent.thread

private val logger = LoggerFactory.getLogger("IMClient")

fun main(args: Array<String>) {
    val userId = args.firstOrNull() ?: UUID.randomUUID().toString()
    logger.info("userId is $userId")
    IMClient().start(userId)
}

class IMClient {

    private var channel: Channel? = null

    private var workerGroup: NioEventLoopGroup? = null

    fun start(userId: String) {
        try {
            val reader = BufferedReader(InputStreamReader(System.`in`))
            channel = connect(
                userId
            ).channel()
            readAndSendUserInputBackend(userId, reader)
            channel?.closeFuture()?.sync()
        } finally {
            workerGroup?.shutdownGracefully()
        }
    }

    private fun readAndSendUserInputBackend(userId: String, reader: BufferedReader) {
        thread {
            while (true) {
                logger.info("请输入对方的大名(直接回车表示群聊):")
                val targetUserId = reader.readLine()
                logger.info("请输入聊天内容:")
                val content = reader.readLine()
                val msg = Message().apply {
                    this.sourceUserId = userId
                    this.targetUserId = targetUserId
                    this.messageContent = content
                }
                getChannel(userId).writeAndFlush(msg)
            }
        }
    }

    private fun getChannel(userId: String): Channel {
        val currentChannel = channel
        if (currentChannel == null || !channelAvailable(
                currentChannel
            )
        ) {
            return connect(userId).channel()
        }
        return currentChannel
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
