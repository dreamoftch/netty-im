package com.tch.test.netty.im.handler.server

import com.alibaba.fastjson.JSON
import com.tch.test.netty.im.common.Message
import com.tch.test.netty.im.service.UserChannelService
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class ServerHandler: SimpleChannelInboundHandler<Message>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        val remoteAddress = ctx.channel().remoteAddress().toString()
        logger.info("[SERVER] - " + remoteAddress + "加入")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        val remoteAddress = ctx.channel().remoteAddress().toString()
        logger.info("[SERVER] - " + remoteAddress + "离开")
        userOffline(ctx.channel())
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val remoteAddress = ctx.channel().remoteAddress().toString()
        logger.info("[SERVER] - " + remoteAddress + "掉线")
        userOffline(ctx.channel())
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        val remoteAddress = ctx.channel().remoteAddress().toString()
        logger.info("[SERVER] - " + remoteAddress + "在线")
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        val message = JSON.toJSONString(msg)
        logger.info("服务器收到消息:$message")
        if (msg.targetUserId.isBlank()) {
            UserChannelService.sendMsgToAllUser(msg)
        } else {
            UserChannelService.sendMsgToUser(msg.targetUserId, msg)
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.info("[SERVER] - " + ctx.channel().remoteAddress() + "异常")
        userOffline(ctx.channel())
        cause.printStackTrace()
    }

    private fun userOffline(channel: Channel) {
        UserChannelService.removeByChannel(channel)?.let {
            UserChannelService.sendMsgToAllUser(Message.build("用户${it}下线"))
        }
    }
}