package com.tch.test.netty.im.server.handler.common

import com.alibaba.fastjson.JSON
import com.tch.test.netty.im.server.common.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class TcpMessageEncoder: MessageToByteEncoder<Message>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        val bytes = JSON.toJSONString(msg).toByteArray()
        out.writeInt(bytes.size)
        out.writeBytes(bytes)
    }

}