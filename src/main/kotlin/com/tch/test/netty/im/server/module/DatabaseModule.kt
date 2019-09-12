package com.tch.test.netty.im.server.module

import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Names
import com.tch.test.netty.im.server.common.Environment
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.mybatis.guice.MyBatisModule
import org.slf4j.LoggerFactory
import java.util.*
import javax.sql.DataSource

class DatabaseModule : MyBatisModule() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun initialize() {
        bindProperties()
        bindTransactionFactoryType(JdbcTransactionFactory::class.java)
        addMapperClasses("com.tch.test.netty.im.server.mapper")
    }

    private fun bindProperties() {
        val connectionProps = Properties()
        connectionProps.setProperty("mybatis.environment.id", Environment.MYBATIS_ENVIRONMENT_ID)
        Names.bindProperties(binder(), connectionProps)
    }

    @Provides
    @Singleton
    fun buildDataSource(): DataSource {
        logger.info("开始构建DataSource")
        val config = HikariConfig().apply {
            this.jdbcUrl = Environment.JDBC_URL
            this.username = Environment.JDBC_USERNAME
            this.password = Environment.JDBC_PASSWORD
            this.driverClassName = Environment.JDBC_DRIVER_CLASS_NAME
            this.connectionTimeout = Environment.JDBC_CONNECTION_TIMEOUT // 1000毫秒
        }
        return HikariDataSource(config)
    }

}