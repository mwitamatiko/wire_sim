package com.example.sim.encryption

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import android.util.Log
import java.io.File

class SqliteDbCallback(private val context: Context,private val encryptor: SqliteEncryptor, private val alias: String): RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        encryptor.encrypt(context, File(db.path.toString()),alias)
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        encryptor.decrypt(context,File(db.path.toString()),alias)
    }


}