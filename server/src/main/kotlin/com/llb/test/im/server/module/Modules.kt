package com.llb.test.im.server.module

import com.google.inject.AbstractModule

class Modules: AbstractModule() {
    override fun configure() {
        install(DatabaseModule())
    }
}