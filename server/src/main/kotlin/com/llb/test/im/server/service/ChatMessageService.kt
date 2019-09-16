package com.llb.test.im.server.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.server.common.ChatMessageAck
import com.llb.test.im.server.extension.toPO
import com.llb.test.im.server.extension.toMessage
import com.llb.test.im.server.mapper.MessageMapper
import com.llb.test.im.server.po.MessagePO
import org.mybatis.guice.transactional.Transactional

@Singleton
open class ChatMessageService {

    @Inject
    private lateinit var messageMapper: MessageMapper

    @Transactional
    open fun saveChatMessage(IMMessage: IMMessage): MessagePO {
        val chatMessage = IMMessage.toPO()
        messageMapper.saveChatMessage(chatMessage)
        return chatMessage
    }

    /**
     * 查询我的离线消息
     */
    fun listMyMessageWithNoAck(targetUserId: String): List<IMMessage> {
        return messageMapper.listMyMessageWithNoAck(targetUserId).map {
            it.toMessage()
        }
    }

    /**
     * 更新消息的ack已收到
     */
    fun markAckReceived(msg: IMMessage) {
        val requestId = msg.requestId ?: return
        messageMapper.markAckReceived(requestId, ChatMessageAck.RECEIVED.code)
    }
}