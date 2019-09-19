package com.llb.test.im.server.handler

import com.google.inject.Inject
import com.llb.test.im.common.handler.TcpMessageDecoder
import com.llb.test.im.common.handler.TcpMessageEncoder
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.stream.ChunkedWriteHandler

/**
 * 区分WebSocket和非WebSocket的handler
 */
class TcpWebSocketHandler: ByteToMessageDecoder() {

    @Inject
    private lateinit var webSocketDecoder: WebSocketDecoder
    @Inject
    private lateinit var webSocketEncoder: WebSocketEncoder
    @Inject
    private lateinit var tcpMessageDecoder: TcpMessageDecoder
    @Inject
    private lateinit var tcpMessageEncoder: TcpMessageEncoder
    @Inject
    private lateinit var loginHandler: LoginHandler
    @Inject
    private lateinit var chatHandler: ChatHandler
    @Inject
    private lateinit var ackHandler: AckHandler
    @Inject
    private lateinit var heartBeatHandler: HeartBeatHandler
    @Inject
    private lateinit var msgReadHandler:  MsgReadHandler

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
            // 添加WebSocket的handler
            addWebSocketHandler(pipeline)
        } else {
            // 添加非WebSocket的handler
            addTcpHandler(pipeline)
        }
        // 处理消息
        pipeline.addLast(loginHandler) // 需要登录
            .addLast(ackHandler)
            .addLast(heartBeatHandler)
            .addLast(msgReadHandler)
            .addLast(chatHandler) // 业务处理
        // 每个连接只需要第一次连接上来的时候执行TcpWebSocketHandler,后面都不需要执行,所以需要移除掉
        pipeline.remove(TcpWebSocketHandler::class.java)
    }

    /**
     * 根据前三位判断是否是http
     */
    private fun isHttp(byteBuf: ByteBuf): Boolean {
        // getChar(index)相对于readChar()的主要不同在于:
        // getChar不会修改ByteBuf的readerIndex和writerIndex
        val readerIndex = byteBuf.readerIndex()
        val char1 = byteBuf.getByte(readerIndex).toChar().toString()
        val char2 = byteBuf.getByte(readerIndex + 1).toChar().toString()
        val char3 = byteBuf.getByte(readerIndex + 2).toChar().toString()
        return (char1 + char2 + char3) in HTTP_METHOD_PREFIX
    }

    private fun addTcpHandler(pipeline: ChannelPipeline) {
        pipeline.addLast(tcpMessageDecoder) // 消息解码
            .addLast(tcpMessageEncoder) // 消息编码
    }

    private fun addWebSocketHandler(pipeline: ChannelPipeline) {
        pipeline.addLast(HttpServerCodec())
            .addLast(HttpObjectAggregator(10240))
            .addLast(ChunkedWriteHandler())
            .addLast(WebSocketServerProtocolHandler("/ws"))
            .addLast(webSocketDecoder) // 将WebSocket消息转为自定义消息
            .addLast(webSocketEncoder) // 将自定义消息消息转为WebSocket消息
    }

}