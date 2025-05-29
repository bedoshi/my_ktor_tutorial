package com.example.test

import com.example.database.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabaseConfig {
    private var isInitialized = false
    
    fun init() {
        if (!isInitialized) {
            Database.connect(createH2DataSource())
            
            transaction {
                // Create tables
                SchemaUtils.create(Users)
            }
            
            isInitialized = true
        }
    }
    
    fun clearDatabase() {
        init() // Ensure database is initialized
        transaction {
            Users.deleteAll()
        }
    }
    
    private fun createH2DataSource(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL"
        config.username = "sa"
        config.password = ""
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        
        return HikariDataSource(config)
    }
}