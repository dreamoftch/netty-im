package com.llb.test.im.common.msg

enum class MessageType(val type: Byte, val desc: String) {

    LOGIN(1, "登陆消息"),
    LOGOUT(2, "登陆消息"),
    ACK(3, "ACK消息"),
    HEARTBEAT(4, "心跳消息"),
    CHAT(5, "聊天消息")

}