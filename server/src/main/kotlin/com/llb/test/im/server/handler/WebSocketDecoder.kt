package com.llb.test.im.server.handler

import com.alibaba.fastjson.JSON
import com.llb.test.im.common.msg.IMMessage
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

/**
 * 将接收到的TextWebSocketFrame转为自定义的message对象
 */
class WebSocketDecoder: MessageToMessageDecoder<TextWebSocketFrame>() {

    override fun decode(ctx: ChannelHandlerContext, msg: TextWebSocketFrame, out: MutableList<Any>) {
        val webSocketMsg = msg.text()
        out.add(JSON.parseObject(webSocketMsg, IMMessage::class.java))
    }

}