package com.llb.test.im.server.common

enum class ChatMessageAck(val code: Int, val desc: String) {

    NOT_RECEIVED(1, "未收到"),
    RECEIVED(2, "收到"),

}

enum class AccountOnlineStatus(val code: Int, val desc: String) {

    NOT_ONLINE(1, "不在线"),
    ONLINE(2, "在线"),

}