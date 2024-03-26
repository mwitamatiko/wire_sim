package com.example.sim.encryption

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import java.io.File

class SqliteDbCallback(private val context: Context,private val encryptor: SqliteEncryptor, private val alias: String): RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val key = encryptor.generateKey(alias)
        encryptor.encrypt(context, File(db.path.toString()),alias)
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        val key = encryptor.generateKey(alias)
        encryptor.decrypt(context,File(db.path.toString()),alias)
    }
}