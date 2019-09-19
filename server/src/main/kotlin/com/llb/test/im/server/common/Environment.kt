package com.llb.test.im.server.common

object Environment {

    val JDBC_URL = getOrDefault("JDBC_URL", "jdbc:postgresql://localhost:5432/postgres")
    val JDBC_USERNAME = getOrDefault("JDBC_USERNAME", "postgres")
    val JDBC_PASSWORD = getOrDefault("JDBC_PASSWORD", "postgres")
    val JDBC_DRIVER_CLASS_NAME = getOrDefault("JDBC_DRIVER_CLASS_NAME", "org.postgresql.Driver")
    val JDBC_CONNECTION_TIMEOUT = getOrDefault("JDBC_CONNECTION_TIMEOUT", 1000L)
    val MYBATIS_ENVIRONMENT_ID = getOrDefault("MYBATIS_ENVIRONMENT_ID", "postgres")

    private fun getOrDefault(key: String, defaultValue: String): String {
        return System.getenv(key) ?: defaultValue
    }

    private fun getOrDefault(key: String, defaultValue: Int): Int {
        return System.getenv(key)?.toIntOrNull() ?: defaultValue
    }

    private fun getOrDefault(key: String, defaultValue: Long): Long {
        return System.getenv(key)?.toLongOrNull() ?: defaultValue
    }

}