package com.tch.test.netty.im.server.common

import com.tch.test.netty.im.server.po.ChatMessage
import java.util.*

class Message {

    /**
     * id
     */
    var id: Long? = null
    /**
     * 消息内容
     */
    var messageContent: String? = null
    /**
     * 消息来源用户id
     */
    var sourceUserId: String = ""
    /**
     * 消息目标用户id
     */
    var targetUserId: String = ""
    /**
     * 登陆需要携带该token
     */
    var token: String? = null
    /**
     * 消息发送时间
     */
    var createdAt: Date = Date()
    /**
     * 消息类型
     */
    var messageType: ChatMessageType? = null

    companion object {

        fun build(content: String?, userId: String): Message {
            return Message().apply {
                this.sourceUserId = userId
                this.messageContent = content
            }
        }

        fun build(content: String?): Message {
            return Message().apply {
                this.sourceUserId = SYSTEM_USER_ID
                this.messageContent = content
            }
        }

    }

}

fun Message.toChatMessage(): ChatMessage {
    val message = this
    return ChatMessage().apply {
        this.sourceUserId = message.sourceUserId
        this.targetUserId = message.targetUserId
        this.messageContent = message.messageContent
        this.createdAt = message.createdAt
    }
}

fun ChatMessage.toMessage(): Message {
    val chatMessage = this
    return Message().apply {
        this.id = chatMessage.id
        this.sourceUserId = chatMessage.sourceUserId ?: ""
        this.targetUserId = chatMessage.targetUserId ?: ""
        this.messageContent = chatMessage.messageContent
        this.createdAt = chatMessage.createdAt ?: Date()
    }
}