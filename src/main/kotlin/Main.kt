package com.metehanbolat

fun main() {
    println("Test")
}

data class User(val id: String, val name: String, val surname: String)

interface UserRepository {
    fun findUser(userId: String): User?
    fun findUsers(): List<User>
    fun updateUser(user: User)
    fun insertUser(user: User)
}


class MongoUserRepository : UserRepository {
    override fun findUser(userId: String): User? {
        TODO("Not yet implemented")
    }

    override fun findUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun insertUser(user: User) {
        TODO("Not yet implemented")
    }
}

class FakeUserRepository : UserRepository {
    private var users = listOf<User>()

    override fun findUser(userId: String): User? {
        return users.find { it.id == userId }
    }

    override fun findUsers(): List<User> {
        return users
    }

    override fun updateUser(user: User) {
        val oldUsers = users.filter { it.id == user.id }
        users = users - oldUsers + user
    }

    override fun insertUser(user: User) {
        users = users + user
    }
}
