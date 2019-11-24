package com.hamid.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hamid.data.model.RepoDBModel

@Database(entities = [RepoDBModel::class], version = 1, exportSchema = false)
abstract class RepositoryRoomDatabase : RoomDatabase() {

    abstract fun dao(): RepoDaoImpl

}
