package com.hamid.data

import com.hamid.data.local.db.RepoDaoImpl
import com.hamid.data.model.*
import com.hamid.data.remote.APIService
import com.hamid.domain.model.model.RemoteApiResponse
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.repository.GitRepoRepository
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitRepoRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val daoImpl: RepoDaoImpl,
    private val dbMapper: ModelMapperImpl,
    private val presentationMapper: PresentationModelMapperImpl
) : GitRepoRepository {

    override fun getRepoFromDb(): Flowable<Response> {
        return daoImpl.getAllRepo()
            .map { presentationMapper.fromEntity(it) }
    }

    override fun getRepoFromServer(): Single<List<RemoteApiResponse>> {
          return  apiService.fetchRepo()
    }

    override fun insertRepoToDB(apiResponse: List<RemoteApiResponse>) = daoImpl.insertAll(dbMapper.fromEntity(apiResponse))

    override fun nukeDB() = daoImpl.deleteAll()

}




