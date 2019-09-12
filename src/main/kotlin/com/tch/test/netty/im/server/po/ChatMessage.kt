package com.tch.test.netty.im.server.po

import java.util.*

/**
 * 聊天消息对象
 */
class ChatMessage {

    /**
     * id
     */
    var id: Long? = null
    /**
     * 消息来源用户id
     */
    var sourceUserId: String? = null
    /**
     * 消息目标用户id
     */
    var targetUserId: String? = null
    /**
     * 消息内容
     */
    var messageContent: String? = null
    /**
     * 消息发送时间
     */
    var createdAt: Date? = null

}