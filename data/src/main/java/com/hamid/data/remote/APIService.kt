package com.hamid.data.remote

import com.hamid.domain.model.model.RemoteApiResponse
import io.reactivex.Single
import retrofit2.http.GET

interface APIService {

    @GET("repos")
    fun fetchRepo(): Single<List<RemoteApiResponse>>

}
