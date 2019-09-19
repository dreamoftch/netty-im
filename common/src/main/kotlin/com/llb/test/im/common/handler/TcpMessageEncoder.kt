package com.llb.test.im.common.handler

import com.alibaba.fastjson.JSON
import com.llb.test.im.common.msg.IMMessage
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class TcpMessageEncoder: MessageToByteEncoder<IMMessage>() {

    override fun encode(ctx: ChannelHandlerContext, msg: IMMessage, out: ByteBuf) {
        val bytes = JSON.toJSONString(msg).toByteArray()
        out.writeInt(bytes.size)
        out.writeBytes(bytes)
    }

}