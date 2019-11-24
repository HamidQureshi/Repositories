package com.hamid.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hamid.data.local.db.RepoDaoImpl
import com.hamid.data.local.db.RepositoryRoomDatabase
import com.hamid.data.utils.helper.MockDBResponse
import org.junit.*

class DBTest {

    private lateinit var db: RepositoryRoomDatabase
    private lateinit var daoImpl: RepoDaoImpl
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repoList = MockDBResponse.repoDBList

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), RepositoryRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        daoImpl = db.dao()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
    }


    @Test
    fun getDataWhenNoDataInserted() {
        val list = daoImpl.getAllRepo()
            .test()
            .values()[0]

        Assert.assertEquals(0, list.size)
    }

    @Test
    fun insertAndGetData() {
        daoImpl.insertAll(repoList)

        daoImpl.getAllRepo()
            .test()
            .assertValue { it.size == repoList.size && it == repoList }
    }

    @Test
    fun deleteAndGetData() {
        daoImpl.insertAll(repoList)

        daoImpl.deleteAll()
        val list = daoImpl.getAllRepo()
            .test()
            .values()[0]

        Assert.assertEquals(0, list.size)

    }

}


