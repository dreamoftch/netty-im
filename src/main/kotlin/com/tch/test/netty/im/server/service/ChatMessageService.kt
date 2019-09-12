package com.tch.test.netty.im.server.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.tch.test.netty.im.server.common.Message
import com.tch.test.netty.im.server.common.toChatMessage
import com.tch.test.netty.im.server.common.toMessage
import com.tch.test.netty.im.server.mapper.MessageMapper
import com.tch.test.netty.im.server.po.ChatMessage
import org.mybatis.guice.transactional.Transactional

@Singleton
open class ChatMessageService {

    @Inject
    private lateinit var messageMapper: MessageMapper

    @Transactional
    open fun saveChatMessage(message: Message): ChatMessage {
        val chatMessage = message.toChatMessage()
        messageMapper.saveChatMessage(chatMessage)
        return chatMessage
    }

    /**
     * 查询我的离线消息
     */
    fun listMyMessageWithNoAck(targetUserId: String): List<Message> {
        return messageMapper.listMyMessageWithNoAck(targetUserId).map {
            it.toMessage()
        }
    }

    /**
     * 更新消息的ack已收到
     */
    fun markAckReceived(msg: Message) {
        val msgId = msg.id ?: return
        messageMapper.markAckReceived(msgId)
    }
}