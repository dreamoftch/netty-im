package com.tch.test.netty.im.common

class Message {

    var type: Int? = null

    var content: Any? = null

    var user: String? = null

    companion object {

        fun build(content: Any?, user: String?): Message {
            return Message().apply {
                this.user = user
                this.content = content
            }
        }

    }

}