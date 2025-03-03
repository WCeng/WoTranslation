package com.wceng.database

import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

internal object DatabaseMigrations {

    @DeleteColumn.Entries(
        DeleteColumn(
            tableName = "translates",
            columnName = "update_date"
        )
    )
    class Schema1To2 : AutoMigrationSpec

}