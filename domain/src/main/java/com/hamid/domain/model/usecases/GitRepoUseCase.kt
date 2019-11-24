package com.hamid.domain.model.usecases

import com.hamid.domain.model.model.Response
import com.hamid.domain.model.repository.GitRepoRepository
import io.reactivex.Flowable

class GitRepoUseCase(private val repository: GitRepoRepository) {

    fun getRepoFromDb(): Flowable<Response> = repository.getRepoFromDb()

    fun getRepoFromServer() = repository.getRepoFromServer()

    fun nukeDB() = repository.nukeDB()

    fun clearDisposable() = repository.clearDisposable()

}