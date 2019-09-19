package com.llb.test.im.server.extension

import com.alibaba.fastjson.JSON
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.common.msg.MessageType
import com.llb.test.im.server.entity.Message
import java.util.*

fun IMMessage.toPO(): Message {
    val message = this
    return Message().apply {
        this.sourceUserId = message.sourceUserId
        this.targetUserId = message.targetUserId.firstOrNull()
        this.content = message.body
        this.requestId = message.requestId
        this.createdAt = message.createdAt
    }
}

fun Message.toMessage(): IMMessage {
    val messagePO = this
    return IMMessage().apply {
        this.messageType = MessageType.CHAT
        this.sourceUserId = messagePO.sourceUserId ?: 0
        this.targetUserId = listOfNotNull(messagePO.targetUserId)
        this.body = messagePO.content
        this.requestId = messagePO.requestId
        this.createdAt = messagePO.createdAt ?: Date()
    }
}

fun Any?.toJson(): String {
    this ?: return ""
    return JSON.toJSONString(this)
}