package com.llb.test.im.server.extension

import com.llb.test.im.common.constant.Message
import com.llb.test.im.server.po.ChatMessage
import java.util.*

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