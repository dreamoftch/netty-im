package com.tch.test.netty.im.handler.server

import com.tch.test.netty.im.handler.common.TcpMessageDecoder
import com.tch.test.netty.im.handler.common.TcpMessageEncoder
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.stream.ChunkedWriteHandler
import org.slf4j.LoggerFactory

/**
 * 区分WebSocket和非WebSocket
 */
class TcpWebSocketHandler: ByteToMessageDecoder() {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private val HTTP_METHOD_PREFIX = listOf(
            "GET", // GET
            "POS", // POST
            "HEA", // HEAD
            "OPT", // OPTIONS
            "PUT", // PUT
            "PAT", // PATCH
            "DEL", // DELETE
            "TRA", // TRACE
            "CON" // CONNECT
        )
    }

    override fun decode(ctx: ChannelHandlerContext, byteBuf: ByteBuf, out: List<Any>) {
        if (byteBuf.readableBytes() < 6) {
            return
        }
        val pipeline = ctx.channel().pipeline()
        if (isHttp(byteBuf)) {
            addWebSocketHandler(pipeline)
        } else {
            addTcpHandler(pipeline)
        }
        // 处理消息
        pipeline.addLast(LoginHandler()) // 需要登录
            .addLast(ServerHandler()) // 业务处理
        // 每个连接只需要第一次连接上来的时候执行TcpWebSocketHandler,后面都不需要执行,所以需要移除掉
        pipeline.remove(TcpWebSocketHandler::class.java)
    }

    /**
     * 根据前三位判断是否是http
     */
    private fun isHttp(byteBuf: ByteBuf): Boolean {
        // getChar(index)相对于readChar()的主要不同在于:
        // getChar不会修改ByteBuf的readerIndex和writerIndex
        val char1 = byteBuf.getByte(0).toChar().toString()
        val char2 = byteBuf.getByte(1).toChar().toString()
        val char3 = byteBuf.getByte(2).toChar().toString()
        return (char1 + char2 + char3) in HTTP_METHOD_PREFIX
    }

    private fun addTcpHandler(pipeline: ChannelPipeline) {
        pipeline.addLast(TcpMessageDecoder()) // 消息解码
            .addLast(TcpMessageEncoder()) // 消息编码
    }

    private fun addWebSocketHandler(pipeline: ChannelPipeline) {
        pipeline.addLast(HttpServerCodec())
            .addLast(HttpObjectAggregator(10240))
            .addLast(ChunkedWriteHandler())
            .addLast(WebSocketServerProtocolHandler("/ws"))
            .addLast(WebSocketDecoder()) // 将WebSocket消息转为自定义消息
            .addLast(WebSocketEncoder()) // 将自定义消息消息转为WebSocket消息
    }

}