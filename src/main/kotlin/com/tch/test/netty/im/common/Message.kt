package com.tch.test.netty.im.common

class Message {

    var content: Any? = null

    var userId: String = ""
    var targetUserId: String = ""

    /**
     * 登陆需要携带该token
     */
    var token: String? = null

    companion object {

        fun build(content: Any?, userId: String): Message {
            return Message().apply {
                this.userId = userId
                this.content = content
            }
        }

        fun build(content: Any?): Message {
            return Message().apply {
                this.userId = SYSTEM_USER_ID
                this.content = content
            }
        }

    }

}