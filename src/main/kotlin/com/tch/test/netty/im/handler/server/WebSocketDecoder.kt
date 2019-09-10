package com.tch.test.netty.im.handler.server

import com.alibaba.fastjson.JSON
import com.tch.test.netty.im.common.Message
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

/**
 * 将接收到的TextWebSocketFrame转为自定义的message对象
 */
class WebSocketDecoder: MessageToMessageDecoder<TextWebSocketFrame>() {

    override fun decode(ctx: ChannelHandlerContext, msg: TextWebSocketFrame, out: MutableList<Any>) {
        val webSocketMsg = msg.text()
        out.add(JSON.parseObject(webSocketMsg, Message::class.java))
    }

}