package com.wceng.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wceng.database.dao.TranslateDao
import com.wceng.database.model.TranslateEntity
import com.wceng.database.model.TranslateFtsEntity
import com.wceng.database.util.InstantConverter

@Database(
    entities = [TranslateEntity::class, TranslateFtsEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = DatabaseMigrations.Schema1To2::class),
    ]
)
@TypeConverters(InstantConverter::class)
abstract class WoDatabase : RoomDatabase() {
    abstract fun transDao(): TranslateDao
}