package com.llb.test.im.server.handler

import com.alibaba.fastjson.JSON
import com.google.inject.Inject
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.server.extension.toJson
import com.llb.test.im.server.service.ChatMessageService
import com.llb.test.im.server.service.UserChannelService
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

class ChatHandler: SimpleChannelInboundHandler<IMMessage>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Inject
    private lateinit var chatMessageService: ChatMessageService
    @Inject
    private lateinit var userChannelService: UserChannelService

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

    override fun channelRead0(ctx: ChannelHandlerContext, msg: IMMessage) {
        val message = msg.toJson()
        logger.info("服务器收到消息:$message")
        chatMessageService.saveChatMessage(msg)
        if (msg.targetUserId.isEmpty()) {
            userChannelService.sendMsgToAllUser(msg)
        } else {
            msg.targetUserId.forEach {
                userChannelService.sendMsgToUser(it, msg)
            }
        }
        userChannelService.sendAckToUser(ctx.channel(), msg.requestId)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.info("[SERVER] - " + ctx.channel().remoteAddress() + "异常")
        userOffline(ctx.channel())
        cause.printStackTrace()
    }

    private fun userOffline(channel: Channel) {
        userChannelService.removeByChannel(channel)?.let {
            userChannelService.sendMsgToAllUser(IMMessage.build("用户${it}下线"))
        }
    }
}