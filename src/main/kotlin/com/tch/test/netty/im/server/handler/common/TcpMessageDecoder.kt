package com.tch.test.netty.im.server.handler.common

import com.alibaba.fastjson.JSON
import com.tch.test.netty.im.server.common.Message
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.LengthFieldBasedFrameDecoder

/**
 * LengthFieldBasedFrameDecoder 参数设置：
 * maxFrameLength：单个frame消息最大长度
 * lengthFieldOffset：lengthField的偏移量
 * lengthFieldLength：lengthField的长度
 * lengthAdjustment：如果lengthField表示的长度是lengthField+消息内容的长度，那就需要指定lengthAdjustment为负的lengthFieldLength
 * initialBytesToStrip：其实就是经过LengthFieldBasedFrameDecoder本身decode之后，返回给调用者之前，去除多少个Bytes
 * 通过 https://netty.io/4.0/api/io/netty/handler/codec/LengthFieldBasedFrameDecoder.html 的例子更容易理解。
 * 我们这里把initialBytesToStrip设置成和lengthFieldLength一样，就表示decode返回结果只包含消息内容本身，不包含lengthField，
 * 否则的话我们还需要自己读取一下lengthField，但是我们业务其实已经不关心lengthField了，只关心消息内容本身。
 * 因为协议定义的格式是直接以内容的length开头,所以这里lengthFieldOffset偏移量是0
 * lengthFieldLength的长度是4个byte
 * 而且lengthFieldLength表示的只是消息体本身的长度，没有保护其他信息，所以不需要调整,lengthAdjustment=0
 * 我们只关心具体的消息内容，所以initialBytesToStrip=4
 */
class TcpMessageDecoder: LengthFieldBasedFrameDecoder(Int.MAX_VALUE, 0, 4, 0, 4) {

    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf): Message {
        val msg = super.decode(ctx, `in`) as ByteBuf
        val bytes = ByteArray(msg.readableBytes())
        msg.readBytes(bytes)
        val string = String(bytes)
        return JSON.parseObject(string, Message::class.java)
    }

}