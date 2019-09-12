package com.tch.test.netty.im.server

import com.alibaba.fastjson.JSON
import com.google.inject.Guice
import com.google.inject.Inject
import com.tch.test.netty.im.server.common.Message
import com.tch.test.netty.im.server.common.SERVER_PORT
import com.tch.test.netty.im.server.handler.server.ServerChannelInitializer
import com.tch.test.netty.im.server.module.Modules
import com.tch.test.netty.im.server.service.ChatMessageService
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    Guice.createInjector(Modules()).getInstance(IMServer::class.java).start()
//    val message = Message().apply {
//        this.userId = "张三"
//        this.targetUserId = "李四"
//        this.content = "李四, 你好啊，我是张三"
//    }
//    val chatMessage = Guice.createInjector(Modules()).getInstance(ChatMessageService::class.java).saveChatMessage(message)
//    println(JSON.toJSONString(chatMessage))
}

class IMServer {

    private val logger = LoggerFactory.getLogger(IMServer::class.java)

    @Inject
    private lateinit var serverChannelInitializer: ServerChannelInitializer

    fun start() {
        val bossGroup = NioEventLoopGroup()
        val workerGroup = NioEventLoopGroup()
        try {
            val bootstrap = ServerBootstrap()
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(serverChannelInitializer)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
            val channelFuture = bootstrap.bind(SERVER_PORT).sync()
            logger.info("服务器启动成功，SERVER_PORT: $SERVER_PORT")
            channelFuture.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }

}