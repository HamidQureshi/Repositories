package com.example.hamid.gitRepo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.hamid.gitRepo.presentation.ui.viewmodel.RepoViewModel
import com.hamid.data.GitRepoRepositoryImpl
import com.hamid.data.utils.helper.MockResponseForPresentation
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.usecases.GitRepoUseCase
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.only
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mockito.`when`


class ViewModelTest {

    // RxSchedulerRule rule for making the RxJava to run synchronously in unit test
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repo: GitRepoRepositoryImpl = mock()

    private lateinit var gitRepoUseCase: GitRepoUseCase
    private lateinit var viewModel: RepoViewModel


    @Before
    @Throws(Exception::class)
    fun setUp() {

        `when`(
            repo.getRepoFromDb()
        ).thenReturn(Flowable.just(MockResponseForPresentation.responseSuccess))

        gitRepoUseCase = GitRepoUseCase(repo)
        viewModel = RepoViewModel(gitRepoUseCase)

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }


    @Test
    fun getData_getDataFromDomainCalled() {
        viewModel.getData()
        verify(repo, only()).getRepoFromDb()
    }

    @Test
    fun verifyLiveDataNotNull() {

        viewModel.formattedList.observeForTesting {
            Assert.assertNotNull(viewModel.formattedList)
        }
    }

    @Test
    fun verifyLiveData_StatusSuccess() {
        viewModel.getData()

        viewModel.formattedList.observeForTesting {
            Assert.assertTrue(viewModel.formattedList.value!!.status == Status.SUCCESS)
        }
    }

    @Test
    fun verifyLiveData_StatusError() {

        `when`(
            repo.getRepoFromDb()
        ).thenReturn(Flowable.just(MockResponseForPresentation.responseLoading))

        viewModel.getData()

        viewModel.formattedList.observeForTesting {
            Assert.assertTrue(viewModel.formattedList.value!!.status == Status.ERROR)
        }
    }

    @Test
    fun disposableTest() {

        assert(viewModel.compositeDisposable.size() == 0)

        viewModel.getData()

        assert(viewModel.compositeDisposable.size() == 1)

        viewModel.onCleared()

        assert(viewModel.compositeDisposable.size() == 0)

    }


    private fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
        val observer = Observer<T> { }
        try {
            observeForever(observer)
            block()
        } finally {
            removeObserver(observer)
        }
    }

    class RxSchedulerRule : TestRule {

        override fun apply(base: Statement, description: Description) =
            object : Statement() {
                override fun evaluate() {
                    RxAndroidPlugins.reset()
                    RxAndroidPlugins.setInitMainThreadSchedulerHandler { SCHEDULER_INSTANCE }

                    RxJavaPlugins.reset()
                    RxJavaPlugins.setIoSchedulerHandler { SCHEDULER_INSTANCE }
                    RxJavaPlugins.setNewThreadSchedulerHandler { SCHEDULER_INSTANCE }
                    RxJavaPlugins.setComputationSchedulerHandler { SCHEDULER_INSTANCE }

                    base.evaluate()
                }
            }

        companion object {
            private val SCHEDULER_INSTANCE = Schedulers.trampoline()
        }
    }

}


