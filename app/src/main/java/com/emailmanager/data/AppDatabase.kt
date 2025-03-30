package com.emailmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emailmanager.data.converter.DateConverter
import com.emailmanager.data.dao.*
import com.emailmanager.data.entity.*

@Database(
    entities = [
        Email::class,
        Tag::class,
        BrowserGroup::class,
        UseCase::class,
        EmailTag::class,
        EmailBrowserGroup::class,
        EmailUseCase::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emailDao(): EmailDao
    abstract fun tagDao(): TagDao
    abstract fun browserGroupDao(): BrowserGroupDao
    abstract fun useCaseDao(): UseCaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "email_manager_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}