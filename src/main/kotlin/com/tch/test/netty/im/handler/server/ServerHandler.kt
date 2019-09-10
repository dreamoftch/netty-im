package com.tch.test.netty.im.handler.server

import com.alibaba.fastjson.JSON
import com.tch.test.netty.im.common.Message
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor

class ServerHandler: SimpleChannelInboundHandler<Message>() {

    companion object {
        val channels = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    }

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        val remoteAddress = ctx.channel().remoteAddress().toString()
        println("[SERVER] - " + remoteAddress + "加入")
        channels.writeAndFlush(Message.build("[SERVER] - " + ctx.channel().remoteAddress() + "加入", remoteAddress))
        channels.add(ctx.channel())
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        val remoteAddress = ctx.channel().remoteAddress().toString()
        println("[SERVER] - " + remoteAddress + "离开")
        channels.writeAndFlush(Message.build("[SERVER] - " + remoteAddress + "离开", remoteAddress))
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val remoteAddress = ctx.channel().remoteAddress().toString()
        println("[SERVER] - " + remoteAddress + "掉线")
        channels.writeAndFlush(Message.build("[SERVER] - " + remoteAddress + "掉线", remoteAddress))
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        val remoteAddress = ctx.channel().remoteAddress().toString()
        println("[SERVER] - " + remoteAddress + "在线")
        channels.writeAndFlush(Message.build("[SERVER] - " + ctx.channel().remoteAddress() + "在线", remoteAddress))
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        val incoming = ctx.channel()
        val remoteAddress = incoming.remoteAddress().toString()
        val message = JSON.toJSONString(msg)
        println("服务器收到消息:$message")
        channels.writeAndFlush(Message.build(msg.content, remoteAddress))
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        println("[SERVER] - " + ctx.channel().remoteAddress() + "异常")
        cause.printStackTrace()
    }
}