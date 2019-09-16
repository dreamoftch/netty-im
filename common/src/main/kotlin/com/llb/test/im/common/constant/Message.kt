package com.llb.test.im.common.constant

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
