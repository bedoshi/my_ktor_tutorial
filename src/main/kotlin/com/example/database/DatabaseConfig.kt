package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConfig {
    private val dotenv = dotenv {
        ignoreIfMissing = true
    }
    
    fun init() {
        Database.connect(hikari())
        
        transaction {
            addLogger(StdOutSqlLogger)
            
            // Create tables
            SchemaUtils.create(Users)
        }
    }
    
    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://${dotenv["DB_HOST"] ?: "localhost"}:${dotenv["DB_PORT"] ?: "5432"}/${dotenv["DB_NAME"] ?: "ktor_tutorial_db"}"
        config.username = dotenv["DB_USER"] ?: "ktor_user"
        config.password = dotenv["DB_PASSWORD"] ?: "ktor_password"
        config.maximumPoolSize = 10
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        
        return HikariDataSource(config)
    }
}