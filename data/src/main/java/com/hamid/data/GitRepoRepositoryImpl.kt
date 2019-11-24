package com.hamid.data

import android.util.Log
import com.hamid.data.local.db.RepoDaoImpl
import com.hamid.data.model.DBModelMapperImpl
import com.hamid.data.model.PresentationModelMapperImpl
import com.hamid.data.model.RemoteApiResponse
import com.hamid.data.model.RepoDBModel
import com.hamid.data.remote.APIService
import com.hamid.data.utils.EspressoIdlingResource
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.repository.GitRepoRepository
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitRepoRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val daoImpl: RepoDaoImpl,
    private val dbMapper: DBModelMapperImpl,
    private val presentationMapper: PresentationModelMapperImpl
) : GitRepoRepository {

    private var disposable = CompositeDisposable()

    override fun getRepoFromDb(): Flowable<Response> {
        return daoImpl.getAllRepo()
            .map { presentationMapper.fromEntity(it) }
    }

    override fun getRepoFromServer() {
        EspressoIdlingResource.increment()

        disposable.add(
            apiService.fetchRepo()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<List<RemoteApiResponse>>() {

                    override fun onError(e: Throwable) {
                        Log.e("error", e.message + "")
                        EspressoIdlingResource.decrement()
                    }

                    override fun onSuccess(response: List<RemoteApiResponse>) {

                        insertRepoToDB(dbMapper.fromEntity(response))

                        EspressoIdlingResource.decrement()
                    }
                })
        )

    }

    fun insertRepoToDB(repoDBModels: List<RepoDBModel>) = daoImpl.insertAll(repoDBModels)

    override fun nukeDB() = daoImpl.deleteAll()

    fun getDisposable() = disposable

    override fun clearDisposable() =
        disposable.clear()

}




