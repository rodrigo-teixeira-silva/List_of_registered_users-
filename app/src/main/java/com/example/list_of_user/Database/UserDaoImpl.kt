package com.example.list_of_user.Database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.list_of_user.Database.UserDatabaseHelper
import com.example.list_of_user.Adapter.User
import com.example.list_of_user.Dao.UserDao

class UserDaoImpl(context: Context) : UserDao {
    private val dbHelper = UserDatabaseHelper(context)

    override fun insertUser(user: User): Long {
        return dbHelper.insertUser(user)
    }

    override fun getAllUsers(): List<User> {
        return dbHelper.getAllUsers()
    }

    override fun getActiveUsers(): List<User> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            UserDatabaseHelper.TABLE_NAME,
            null,
            "${UserDatabaseHelper.COLUMN_IS_ACTIVE} = ?",
            arrayOf("1"),
            null,
            null,
            null
        )
        val users = mutableListOf<User>()
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_NAME)),
                    birthDate = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_BIRTH_DATE)),
                    cpf = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_CPF)),
                    city = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_CITY)),
                    isActive = cursor.getInt(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_IS_ACTIVE)) == 1,
                    photoUri = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_PHOTO_URI))
                )
                users.add(user)
                Log.d("UserDaoImpl", "User fetched: $user")
            } while (cursor.moveToNext())
        }
        cursor.close()
        Log.d("UserDaoImpl", "Total users fetched: ${users.size}")
        return users
    }

    override fun getInactiveUsers(): List<User> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            UserDatabaseHelper.TABLE_NAME,
            null,
            "${UserDatabaseHelper.COLUMN_IS_ACTIVE} = ?",
            arrayOf("0"),
            null,
            null,
            null
        )
        val users = mutableListOf<User>()
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_NAME)),
                    birthDate = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_BIRTH_DATE)),
                    cpf = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_CPF)),
                    city = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_CITY)),
                    isActive = cursor.getInt(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_IS_ACTIVE)) == 1,
                    photoUri = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COLUMN_PHOTO_URI))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }

    override fun updateUserStatus(userId: Int, isActive: Boolean): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(UserDatabaseHelper.COLUMN_IS_ACTIVE, if (isActive) 1 else 0)
        }
        return db.update(
            UserDatabaseHelper.TABLE_NAME,
            contentValues,
            "${UserDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(userId.toString())
        )
    }

    override fun deleteUser(userId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(UserDatabaseHelper.TABLE_NAME, "${UserDatabaseHelper.COLUMN_ID} = ?", arrayOf(userId.toString()))
    }
}
