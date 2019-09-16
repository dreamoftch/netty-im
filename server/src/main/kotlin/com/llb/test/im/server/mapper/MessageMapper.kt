package com.llb.test.im.server.mapper

import com.llb.test.im.server.po.ChatMessage
import org.apache.ibatis.annotations.*

interface MessageMapper {

    @Insert("insert into " +
            "chat_message (source_user_id, target_user_id, message_content, created_at) " +
            "values (#{sourceUserId}, #{targetUserId}, #{messageContent}, #{createdAt})")
    @SelectKey(keyProperty = "id", before = false, resultType = Long::class,
        statement = ["select currval(pg_get_serial_sequence('chat_message', 'id'));"])
    fun saveChatMessage(message: ChatMessage)

    @Select("SELECT * FROM chat_message WHERE target_user_id = #{targetUserId} and ack = 1")
    @Results(value = [
        Result(property = "sourceUserId", column = "source_user_id"),
        Result(property="targetUserId", column = "target_user_id"),
        Result(property="messageContent", column = "message_content"),
        Result(property="createdAt", column = "created_t")
    ])
    fun listMyMessageWithNoAck(@Param("targetUserId") targetUserId: String): List<ChatMessage>

    /**
     * 更新消息的ack已收到
     */
    @Update("update chat_message set ack = 2 where id = #{msgId}")
    fun markAckReceived(@Param("msgId") msgId: Long)
}