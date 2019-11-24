package com.example.hamid.gitRepo.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.usecases.GitRepoUseCase
import io.reactivex.disposables.CompositeDisposable
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
                .subscribe({ response ->
                    Log.d(TAG, "On Next Called")
                    if (response.data.isEmpty()) {
                        gitRepoUseCase.getRepoFromServer()
                    }
                    formattedList.postValue(response)
                }, { error ->
                    Log.d(TAG, "On Error Called $error")
                    gitRepoUseCase.getRepoFromServer()
                }, {
                    Log.d(TAG, "On Complete Called")
                })
        )

    }

    public override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        gitRepoUseCase.clearDisposable()
    }

}
