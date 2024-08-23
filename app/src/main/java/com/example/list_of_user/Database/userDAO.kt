package com.example.list_of_user.Dao

import com.example.list_of_user.Adapter.User

interface UserDao {
    fun insertUser(user: User): Long
    fun getAllUsers(): List<User>
    fun getActiveUsers(): List<User>
    fun getInactiveUsers(): List<User>
    fun updateUserStatus(userId: Int, isActive: Boolean): Int
    fun deleteUser(userId: Int)
}
