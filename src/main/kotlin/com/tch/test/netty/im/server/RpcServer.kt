package com.tch.test.netty.im.server

import com.tch.test.netty.im.common.SERVER_PORT
import com.tch.test.netty.im.handler.server.ServerChannelInitializer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.slf4j.LoggerFactory

class RpcServer {

    companion object {

        private val logger = LoggerFactory.getLogger(RpcServer::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            val bossGroup = NioEventLoopGroup()
            val workerGroup = NioEventLoopGroup()
            try {
                val bootstrap = ServerBootstrap()
                bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .handler(LoggingHandler(LogLevel.INFO))
                    .childHandler(ServerChannelInitializer())
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

}