package com.llb.test.im.common.msg

enum class MessageType(val type: Byte, val desc: String) {

    LOGIN(1, "登陆消息"),
    LOGOUT(2, "登出消息"),
    ACK(3, "心跳等的ACK消息"),
    HEARTBEAT(4, "心跳消息"),
    CHAT(5, "聊天消息"),
    MSG_SEND_ACK(6, "消息成功发送到服务器的ACK"),
    MSG_READ(7, "消息已读"),

}