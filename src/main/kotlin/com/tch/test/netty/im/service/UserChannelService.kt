package com.tch.test.netty.im.service

import com.tch.test.netty.im.common.Message
import io.netty.channel.Channel
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.ConcurrentHashMap

object UserChannelService {

    private val channels = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    private val userChannelMap = ConcurrentHashMap<String, Channel>()
    private val channelUserMap = ConcurrentHashMap<Channel, String>()

    fun addChannel(userId: String, channel: Channel) {
        channels.add(channel)
        userChannelMap[userId] = channel
        channelUserMap[channel] = userId
    }

    fun sendMsgToAllUser(message: Message) {
        channels.writeAndFlush(message)
    }

    fun sendMsgToUser(targetUserId: String, message: Message) {
        userChannelMap[targetUserId]?.let {
            it.writeAndFlush(message)
        }
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