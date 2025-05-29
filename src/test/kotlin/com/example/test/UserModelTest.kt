package com.example.test

import com.example.models.User
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserModelTest {

    @Test
    fun testUserCreationWithNullId() {
        // Test creating user with null ID (default constructor behavior)
        val user = User(
            id = null,
            name = "Test User",
            email = "test@example.com",
            age = 25
        )
        
        assertNull(user.id)
        assertEquals("Test User", user.name)
        assertEquals("test@example.com", user.email)
        assertEquals(25, user.age)
    }

    @Test
    fun testUserCreationWithId() {
        val user = User(
            id = 123,
            name = "Test User",
            email = "test@example.com",
            age = 30
        )
        
        assertEquals(123, user.id)
        assertEquals("Test User", user.name)
        assertEquals("test@example.com", user.email)
        assertEquals(30, user.age)
    }

    @Test
    fun testUserCopy() {
        val original = User(
            id = 1,
            name = "Original",
            email = "original@example.com",
            age = 25
        )
        
        val copied = original.copy(name = "Copied")
        
        assertEquals(1, copied.id)
        assertEquals("Copied", copied.name)
        assertEquals("original@example.com", copied.email)
        assertEquals(25, copied.age)
    }

    @Test
    fun testUserEquals() {
        val user1 = User(id = 1, name = "Test", email = "test@example.com", age = 25)
        val user2 = User(id = 1, name = "Test", email = "test@example.com", age = 25)
        val user3 = User(id = 2, name = "Test", email = "test@example.com", age = 25)
        
        assertEquals(user1, user2)
        assert(user1 != user3)
    }
}