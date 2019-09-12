package com.tch.test.netty.im.server.service

import com.tch.test.netty.im.server.common.Message
import io.netty.channel.Channel
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.ConcurrentHashMap

object UserChannelService {

    /**
     * 用于以列表的结构存储所有的用户连接
     */
    private val channels = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    /**
     * key: 用户id, value: 该用户的连接列表(可以多端登陆)
     */
    private val userChannelMap = ConcurrentHashMap<String, MutableList<Channel>>()
    /**
     * key: 用户连接, value: 该连接对应的用户id
     */
    private val channelUserMap = ConcurrentHashMap<Channel, String>()

    fun addChannel(userId: String, channel: Channel) {
        channels.add(channel)
        getUserChannels(userId).add(channel)
        channelUserMap[channel] = userId
    }

    private fun getUserChannels(userId: String): MutableList<Channel> {
        val userChannels = userChannelMap[userId] ?: mutableListOf()
        userChannelMap.putIfAbsent(userId, userChannels)
        return userChannels
    }

    fun sendMsgToAllUser(message: Message) {
        channels.writeAndFlush(message)
    }

    fun sendMsgToUser(targetUserId: String, message: Message) {
        userChannelMap[targetUserId]?.forEach { it.writeAndFlush(message) }
    }

    fun removeByChannel(channel: Channel): String? {
        channelUserMap[channel]?.let { userId ->
            channelUserMap.remove(channel)
            userChannelMap.remove(userId)
            channels.remove(channel)
            return userId
        }
        return null
    }

}