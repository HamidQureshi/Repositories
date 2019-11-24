package com.hamid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gitRepo_table")
data class RepoDBModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String? = "No Description",
    val avatar: String
)