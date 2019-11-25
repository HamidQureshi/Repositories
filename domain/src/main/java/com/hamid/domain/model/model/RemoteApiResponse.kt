package com.hamid.domain.model.model

import com.google.gson.annotations.SerializedName

data class RemoteApiResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String? = "No Description",
    @SerializedName("owner") val owner: Owner
)