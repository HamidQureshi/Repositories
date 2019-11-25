package com.hamid.domain.model.repository

import com.hamid.domain.model.model.RemoteApiResponse
import com.hamid.domain.model.model.Response
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Repository interface to be implemented by Data layer.
 */
interface GitRepoRepository {

    fun getRepoFromDb(): Flowable<Response>

    fun insertRepoToDB(apiResponse: List<RemoteApiResponse>)

    fun getRepoFromServer(): Single<List<RemoteApiResponse>>

    fun nukeDB()

}