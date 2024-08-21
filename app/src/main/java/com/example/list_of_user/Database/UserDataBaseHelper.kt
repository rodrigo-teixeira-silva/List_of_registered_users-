package com.example.list_of_user.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.list_of_user.Adapter.User

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "user_database.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_BIRTH_DATE = "birth_date"
        const val COLUMN_CPF = "cpf"
        const val COLUMN_CITY = "city"
        const val COLUMN_IS_ACTIVE = "is_active"
        const val COLUMN_PHOTO_URI = "photo_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_BIRTH_DATE TEXT,
                $COLUMN_CPF TEXT,
                $COLUMN_CITY TEXT,
                $COLUMN_IS_ACTIVE INTEGER,
                $COLUMN_PHOTO_URI TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(user: User): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_BIRTH_DATE, user.birthDate)
            put(COLUMN_CPF, user.cpf)
            put(COLUMN_CITY, user.city)
            put(COLUMN_IS_ACTIVE, if (user.isActive) 1 else 0)
            put(COLUMN_PHOTO_URI, user.photoUri)
        }
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllUsers(): List<User> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val users = mutableListOf<User>()
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    birthDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTH_DATE)),
                    cpf = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CPF)),
                    city = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)),
                    isActive = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE)) == 1,
                    photoUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_URI))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }
}
