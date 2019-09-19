package com.llb.test.im.server.mapper

import com.llb.test.im.server.common.ChatMessageAck
import com.llb.test.im.server.po.MessagePO
import org.apache.ibatis.annotations.*

interface MessageMapper {

    @Insert("insert into " +
            "message (source_user_id, target_user_id, content, request_id, created_at) " +
            "values (#{sourceUserId}, #{targetUserId}, #{content}, #{requestId}, #{createdAt})")
    @SelectKey(keyProperty = "id", before = false, resultType = Long::class,
        statement = ["select currval(pg_get_serial_sequence('message', 'id'));"])
    fun saveChatMessage(message: MessagePO)

    @Select("SELECT * FROM message WHERE target_user_id = #{targetUserId} and ack = 1")
    @Results(value = [
        Result(property = "sourceUserId", column = "source_user_id"),
        Result(property="targetUserId", column = "target_user_id"),
        Result(property="content", column = "content"),
        Result(property="requestId", column = "request_id"),
        Result(property="createdAt", column = "created_t")
    ])
    fun listMyMessageWithNoAck(@Param("targetUserId") targetUserId: String): List<MessagePO>

    /**
     * 更新消息的ack已收到
     */
    @Update("update message set ack = #{ack} where request_id = #{requestId}")
    fun markAckReceived(@Param("requestId") requestId: String, @Param("ack") ack: Int)
}