package com.llb.test.im.server.service

import com.google.inject.Singleton
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.msg.MessageType
import io.netty.channel.Channel
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.ConcurrentHashMap

@Singleton
class UserChannelService {

    /**
     * 用于以列表的结构存储所有的用户连接
     */
    private val channels = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    /**
     * key: 用户id, value: 该用户的连接列表(可以多端登陆)
     */
    private val userChannelMap = ConcurrentHashMap<Long, MutableSet<Channel>>()
    /**
     * key: 用户连接, value: 该连接对应的用户id
     */
    private val channelUserMap = ConcurrentHashMap<Channel, Long>()

    fun addChannel(userId: Long, channel: Channel) {
        channels.add(channel)
        getUserChannels(userId).add(channel)
        channelUserMap[channel] = userId
    }

    private fun getUserChannels(userId: Long): MutableSet<Channel> {
        val userChannels = userChannelMap[userId] ?: mutableSetOf()
        userChannelMap.putIfAbsent(userId, userChannels)
        return userChannels
    }

    fun sendMsgToAllUser(IMMessage: IMMessage) {
        channels.writeAndFlush(IMMessage)
    }

    fun sendMsgToAllUserExclude(IMMessage: IMMessage, excludeChannel: Channel) {
        channels.filter { it != excludeChannel }.forEach { it.writeAndFlush(IMMessage) }
    }

    fun sendMsgToUser(targetUserId: Long, IMMessage: IMMessage) {
        userChannelMap[targetUserId]?.forEach { it.writeAndFlush(IMMessage) }
    }

    /**
     * 给指定用户发送ack消息,表示收到了客户端的消息
     */
    fun sendAckToUser(channel: Channel, requestId: String?) {
        requestId ?: throw IllegalArgumentException("requestId can not be null")
        val ackMessage = IMMessage().apply {
            this.messageType = MessageType.ACK
            this.requestId = requestId
        }
        channel.writeAndFlush(ackMessage)
    }

    /**
     * 给指定用户发送ack消息,表示客户端发送的聊天消息成功发送到服务器
     */
    fun sendMsgSendAckToUser(channel: Channel, requestId: String?) {
        requestId ?: throw IllegalArgumentException("requestId can not be null")
        val ackMessage = IMMessage().apply {
            this.messageType = MessageType.MSG_SEND_ACK
            this.requestId = requestId
        }
        channel.writeAndFlush(ackMessage)
    }

    fun removeByChannel(channel: Channel): Long? {
        channelUserMap[channel]?.let { userId ->
            channelUserMap.remove(channel)
            userChannelMap.remove(userId)
            channels.remove(channel)
            return userId
        }
        return null
    }

}