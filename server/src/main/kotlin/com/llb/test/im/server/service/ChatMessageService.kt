package com.llb.test.im.server.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.llb.test.im.common.constant.Message
import com.llb.test.im.server.extension.toChatMessage
import com.llb.test.im.server.extension.toMessage
import com.llb.test.im.server.mapper.MessageMapper
import com.llb.test.im.server.po.ChatMessage
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