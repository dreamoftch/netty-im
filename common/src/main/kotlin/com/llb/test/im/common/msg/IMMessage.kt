package com.llb.test.im.common.msg

import com.llb.test.im.common.constant.SYSTEM_USER_ID
import java.util.*

class IMMessage {

    /**
     * 协议版本
     */
    var version: Byte = 1
    /**
     * 请求Id，由客户端生成
     */
    var requestId: String? = null
    /**
     * 消息类型
     */
    var messageType: MessageType? = null
    /**
     * 消息内容
     */
    var body: String? = null
    /**
     * 消息来源用户id
     */
    var sourceUserId: String = ""
    /**
     * 消息目标用户id
     */
    var targetUserId: List<String> = emptyList()
    /**
     * 消息发送时间
     */
    var createdAt: Date = Date()

    companion object {

        fun build(content: String?, userId: String): IMMessage {
            return IMMessage().apply {
                this.sourceUserId = userId
                this.body = content
            }
        }

        fun build(content: String?): IMMessage {
            return IMMessage().apply {
                this.sourceUserId = SYSTEM_USER_ID
                this.body = content
            }
        }

    }

}
