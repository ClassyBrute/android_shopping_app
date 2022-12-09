package com.gd.intership.hdziedziura.androidshopingapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class, ProductFavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProductDao(): ProductDao

    companion object {
        private var db_instance: AppDatabase? = null

        fun getAppDatabaseInstance(context: Context): AppDatabase {
            if (db_instance == null) {
                db_instance = Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AndroidShopingApp"
                )
                    .build()
            }

            return db_instance!!
        }
    }

    operator fun invoke(context: Context) {
        db_instance ?: getAppDatabaseInstance(context).also { db_instance = it }
    }
}
