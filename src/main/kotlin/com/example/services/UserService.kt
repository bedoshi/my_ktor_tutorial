package com.example.services

import com.example.database.Users
import com.example.models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class UserService {
    
    fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map { rowToUser(it) }
    }
    
    fun getUserById(id: Int): User? = transaction {
        Users.select { Users.id eq id }
            .map { rowToUser(it) }
            .singleOrNull()
    }
    
    fun createUser(user: User): User = transaction {
        val id = Users.insert {
            it[name] = user.name
            it[email] = user.email
            it[age] = user.age
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        } get Users.id
        
        user.copy(id = id)
    }
    
    fun updateUser(id: Int, user: User): Boolean = transaction {
        val updatedRows = Users.update({ Users.id eq id }) {
            it[name] = user.name
            it[email] = user.email
            it[age] = user.age
            it[updatedAt] = Instant.now()
        }
        updatedRows > 0
    }
    
    fun deleteUser(id: Int): Boolean = transaction {
        val deletedRows = Users.deleteWhere { Users.id eq id }
        deletedRows > 0
    }
    
    private fun rowToUser(row: ResultRow): User = User(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email],
        age = row[Users.age]
    )
}