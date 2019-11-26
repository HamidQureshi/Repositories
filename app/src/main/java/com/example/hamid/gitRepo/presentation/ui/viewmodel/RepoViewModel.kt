package com.example.hamid.gitRepo.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hamid.data.utils.EspressoIdlingResource
import com.hamid.domain.model.model.RemoteApiResponse
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.usecases.GitRepoUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepoViewModel @Inject
constructor(
    val gitRepoUseCase: GitRepoUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "viewModel"
    }

    val formattedList = MutableLiveData<Response>()
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getData() {
        compositeDisposable.add(
            gitRepoUseCase.getRepoFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ response ->
                    Log.e(TAG, "On Next Called")
                    if (response.data.isEmpty()) {
                        getDataFromServer()
                    }
                    formattedList.postValue(response)
                }, { error ->
                    formattedList.postValue(Response(Status.ERROR, emptyList()))
                    Log.e(TAG, "On Error Called $error")
                    getDataFromServer()
                }, {
                    Log.e(TAG, "On Complete Called")
                })
        )

    }

    fun getDataFromServer(){
        EspressoIdlingResource.increment()
        compositeDisposable.add(
            gitRepoUseCase.getRepoFromServer()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<List<RemoteApiResponse>>() {

                    override fun onError(e: Throwable) {
                        Log.e("error", e.message + "")
                        formattedList.postValue(Response(Status.ERROR, emptyList()))
                        EspressoIdlingResource.decrement()
                    }

                    override fun onSuccess(response: List<RemoteApiResponse>) {

                        gitRepoUseCase.insertRepoToDB(response)

                        EspressoIdlingResource.decrement()
                    }
                })
        )
    }

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
