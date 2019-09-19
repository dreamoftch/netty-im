package com.llb.test.im.server.handler

import com.alibaba.fastjson.JSON
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.server.extension.toJson
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

/**
 * 将server发送的Message对象转为TextWebSocketFrame发送给WebSocket端
 */
class WebSocketEncoder : MessageToMessageEncoder<IMMessage>() {

    override fun encode(ctx: ChannelHandlerContext, msg: IMMessage, out: MutableList<Any>) {
        out.add(TextWebSocketFrame(msg.toJson()))
    }

}