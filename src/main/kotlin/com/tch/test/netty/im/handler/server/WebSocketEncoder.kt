package com.tch.test.netty.im.handler.server

import com.alibaba.fastjson.JSON
import com.tch.test.netty.im.common.Message
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

/**
 * 将server发送的Message对象转为TextWebSocketFrame发送给WebSocket端
 */
class WebSocketEncoder : MessageToMessageEncoder<Message>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: MutableList<Any>) {
        out.add(TextWebSocketFrame(JSON.toJSONString(msg)))
    }

}