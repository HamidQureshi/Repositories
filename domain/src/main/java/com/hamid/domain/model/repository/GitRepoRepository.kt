package com.hamid.domain.model.repository

import com.hamid.domain.model.model.Response
import io.reactivex.Flowable

/**
 * Repository interface to be implemented by Data layer.
 */
interface GitRepoRepository {

    fun getRepoFromDb(): Flowable<Response>

    fun getRepoFromServer()

    fun nukeDB()

    fun clearDisposable()

}