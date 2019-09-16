package com.llb.test.im.server

import com.google.inject.Guice
import com.google.inject.Inject
import com.llb.test.im.common.constant.SERVER_PORT
import com.llb.test.im.server.handler.ServerChannelInitializer
import com.llb.test.im.server.module.Modules
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    Guice.createInjector(Modules()).getInstance(IMServer::class.java).start()
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