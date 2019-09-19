package com.llb.test.im.server.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.llb.test.im.common.msg.IMMessage
import com.llb.test.im.server.common.AccountOnlineStatus
import com.llb.test.im.server.common.ChatMessageStatus
import com.llb.test.im.server.entity.Account
import com.llb.test.im.server.entity.Message
import com.llb.test.im.server.entity.MessageExample
import com.llb.test.im.server.extension.toMessage
import com.llb.test.im.server.extension.toPO
import com.llb.test.im.server.mapper.generate.AccountMapper
import com.llb.test.im.server.mapper.generate.MessageMapper
import org.mybatis.guice.transactional.Transactional
import java.util.*

@Singleton
open class ChatMessageService {

    @Inject
    private lateinit var messageMapper: MessageMapper
    @Inject
    private lateinit var accountMapper: AccountMapper

    @Transactional
    open fun saveChatMessage(IMMessage: IMMessage): Message {
        val chatMessage = IMMessage.toPO()
        messageMapper.insertSelective(chatMessage)
        return chatMessage
    }

    /**
     * 查询我的离线消息
     */
    fun listMyMessageWithNoAck(targetUserId: Long): List<IMMessage> {
        val example = MessageExample()
        example.createCriteria()
            .andTargetUserIdEqualTo(targetUserId)
            .andStatusEqualTo(ChatMessageStatus.NOT_RECEIVED.code)
        return messageMapper.selectByExample(example).map {
            it.toMessage()
        }
    }

    /**
     * 更新消息的status
     */
    fun updateMsgStatus(requestId: String?, status: Int) {
        requestId ?: return
        val message = Message().apply {
            this.status = status
        }
        val example = MessageExample()
        example.createCriteria().andRequestIdEqualTo(requestId)
        messageMapper.updateByExampleSelective(message, example)
    }

    /**
     * 用户登陆成功,记录登陆时间和登陆状态
     */
    fun updateAccountLogin(accountId: Long, date: Date) {
        val account = Account().apply {
            this.id = accountId
            this.lastLoginAt = date
            this.onlineStatus = AccountOnlineStatus.ONLINE.code
        }
        accountMapper.updateByPrimaryKeySelective(account)
    }

    /**
     * 判断用户id是否存在
     */
    fun userIdExist(userId: Long): Boolean {
        return accountMapper.selectByPrimaryKey(userId) != null
    }

    /**
     * 判断用户id是否存在
     */
    fun getByRequestId(requestId: String): Message? {
        val example = MessageExample().apply {
            this.createCriteria().andRequestIdEqualTo(requestId)
        }
        return messageMapper.selectByExample(example).firstOrNull()
    }
}