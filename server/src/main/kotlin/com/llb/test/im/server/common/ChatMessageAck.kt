package com.llb.test.im.server.common

enum class ChatMessageAck(val code: Int, val desc: String) {

    NOT_RECEIVED(1, "未收到"),
    RECEIVED(2, "收到"),

}