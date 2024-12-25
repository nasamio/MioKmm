package com.mio

import bean.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.text.insert

class UserService(private val db: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 50)
        val pwd = varchar("pwd", 50)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(db) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun queryAll(): List<User> {
        return transaction(db) {
            Users.selectAll().map {
                User(
                    id = it[Users.id],
                    name = it[Users.name],
                    pwd = it[Users.pwd]
                )
            }
        }
    }

    suspend fun insert(user: User) {
        transaction(db) {
            Users.insert {
                it[name] = user.name
                it[pwd] = user.pwd
            }
        }
    }
}