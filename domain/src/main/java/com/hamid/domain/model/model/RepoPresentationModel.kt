package com.hamid.domain.model.model

data class RepoPresentationModel(
    val id: Int,
    val name: String,
    val description: String? = "No Description",
    val avatar: String
)