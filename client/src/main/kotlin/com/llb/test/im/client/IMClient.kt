package com.llb.test.im.client

import com.llb.test.im.client.handler.ClientChannelInitializer
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.constant.SERVER_PORT
import com.llb.test.im.common.msg.MessageType
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
import java.util.concurrent.TimeUnit
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
            channel = connect(userId).channel()
            // 读取用户输入聊天消息并发送到服务器
            readAndSendUserInputBackend(userId, reader)
            // 后台发心跳包
            doHeartBeatBackend(userId)
            channel?.closeFuture()?.sync()
        } finally {
            workerGroup?.shutdownGracefully()
        }
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

    private fun readAndSendUserInputBackend(userId: String, reader: BufferedReader) {
        thread {
            while (true) {
                logger.info("请输入对方的大名(直接回车表示群聊):")
                val targetUserId = reader.readLine()
                val targetUserIds = if (targetUserId.isNullOrBlank()) {
                    emptyList()
                } else {
                    listOf(targetUserId)
                }
                logger.info("请输入聊天内容:")
                val content = reader.readLine()
                val msg = IMMessage().apply {
                    this.messageType = MessageType.CHAT
                    this.sourceUserId = userId
                    this.targetUserId = targetUserIds
                    this.requestId = UUID.randomUUID().toString()
                    this.body = content
                }
                getChannel(userId).writeAndFlush(msg)
            }
        }
    }

    private fun doHeartBeatBackend(userId: String) {
        thread {
            while (true) {
                val msg = IMMessage().apply {
                    this.messageType = MessageType.HEARTBEAT
                    this.sourceUserId = userId
                    this.requestId = UUID.randomUUID().toString()
                }
                getChannel(userId).writeAndFlush(msg)
                // 每隔5秒钟发一次心跳
                TimeUnit.SECONDS.sleep(5)
            }
        }
    }

    private fun getChannel(userId: String): Channel {
        val currentChannel = channel
        if (currentChannel == null || !channelAvailable(currentChannel)) {
            return connect(userId).channel()
        }
        return currentChannel
    }

    private fun channelAvailable(channel: Channel): Boolean {
        return channel.isActive && channel.isOpen
    }

}
