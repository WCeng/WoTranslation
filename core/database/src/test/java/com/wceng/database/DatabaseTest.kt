package com.wceng.database

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.wceng.database.dao.TranslateDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
open class DatabaseTest {

    private lateinit var database: WoDatabase
    protected lateinit var translateDao: TranslateDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = WoDatabase::class.java
        ).build()
        translateDao = database.transDao()
    }

    @Test
    fun a(){
        assert(true)
    }


    @After
    fun after() {
        database.close()
    }

}