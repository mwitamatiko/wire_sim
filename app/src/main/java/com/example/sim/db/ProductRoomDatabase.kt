package com.example.sim.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sim.dao.ProductDao
import com.example.sim.encryption.SqliteDbCallback
import com.example.sim.encryption.SqliteEncryptorImpl
import com.example.sim.entity.Product

@Database(entities = [(Product::class)], version = 1)
abstract class ProductRoomDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object{
        private var INSTANCE: ProductRoomDatabase?=null
        private val DB_NAME = "product_database"
        private val ALIAS = "key_yangu"

        fun getInstance(context: Context): ProductRoomDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    val encryptor = SqliteEncryptorImpl
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductRoomDatabase::class.java,
                        DB_NAME
                    )
                        .addCallback(SqliteDbCallback(context,encryptor, ALIAS))
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE=instance
                }

                return instance
            }
        }
    }



//    companion object{
//
//        private var INSTANCE: ProductRoomDatabase?=null
//
//        fun getInstance(context: Context): ProductRoomDatabase {
//            synchronized(this){
//                var instance = INSTANCE
//                if(instance==null){
//                    instance=Room.databaseBuilder(
//                        context.applicationContext,
//                        ProductRoomDatabase::class.java,
//                        "product_database"
//                    ).fallbackToDestructiveMigration()
//                        .build()
//
//                    INSTANCE=instance
//                }
//                return instance
//            }
//        }
//
//    }
}