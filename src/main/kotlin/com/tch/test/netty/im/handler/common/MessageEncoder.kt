package com.tch.test.netty.im.handler.common

import com.alibaba.fastjson.JSON
import com.tch.test.netty.im.common.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class MessageEncoder: MessageToByteEncoder<Message>() {

    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        val bytes = JSON.toJSONString(msg).toByteArray()
        out.writeInt(bytes.size)
        out.writeBytes(bytes)
    }

}