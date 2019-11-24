package com.hamid.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hamid.data.model.RepoDBModel
import com.hamid.domain.model.repository.RepoDao
import io.reactivex.Flowable

@Dao
interface RepoDaoImpl : RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<RepoDBModel>)

    @Query("SELECT * from gitRepo_table ORDER BY id ")
    fun getAllRepo(): Flowable<List<RepoDBModel>>

    @Query("DELETE FROM gitRepo_table")
    fun deleteAll()

}