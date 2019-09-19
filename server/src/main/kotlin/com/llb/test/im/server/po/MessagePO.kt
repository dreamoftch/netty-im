package com.llb.test.im.server.po

import java.util.*

/**
 * 聊天消息对象
 */
class MessagePO {

    /**
     * id
     */
    var id: Long? = null
    /**
     * 客户端生成的消息id
     */
    var requestId: String? = null
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
    var content: String? = null
    /**
     * 消息发送时间
     */
    var createdAt: Date? = null

}