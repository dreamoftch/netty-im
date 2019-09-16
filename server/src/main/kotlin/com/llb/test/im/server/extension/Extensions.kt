package com.llb.test.im.server.extension

import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.server.po.MessagePO
import java.util.*

fun IMMessage.toPO(): MessagePO {
    val message = this
    return MessagePO().apply {
        this.sourceUserId = message.sourceUserId
        this.targetUserId = message.targetUserId.firstOrNull()
        this.content = message.body
        this.requestId = message.requestId
        this.createdAt = message.createdAt
    }
}

fun MessagePO.toMessage(): IMMessage {
    val messagePO = this
    return IMMessage().apply {
        this.sourceUserId = messagePO.sourceUserId ?: ""
        this.targetUserId = listOfNotNull(messagePO.targetUserId)
        this.body = messagePO.content
        this.requestId = messagePO.requestId
        this.createdAt = messagePO.createdAt ?: Date()
    }
}