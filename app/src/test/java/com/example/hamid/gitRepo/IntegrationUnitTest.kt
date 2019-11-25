package com.example.hamid.gitRepo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.hamid.gitRepo.presentation.ui.viewmodel.RepoViewModel
import com.hamid.data.GitRepoRepositoryImpl
import com.hamid.data.local.db.RepoDaoImpl
import com.hamid.data.model.ModelMapperImpl
import com.hamid.data.model.PresentationModelMapperImpl
import com.hamid.data.remote.APIService
import com.hamid.data.utils.helper.MockApiResponse
import com.hamid.data.utils.helper.MockDBResponse
import com.hamid.data.utils.helper.MockResponseForPresentation
import com.hamid.domain.model.usecases.GitRepoUseCase
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

class IntegrationUnitTest {

    @get:Rule
    val rxSchedulerRule = ViewModelTest.RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var apiService: APIService = mock()
    private var daoImpl: RepoDaoImpl = mock()
    private var dbMapper: ModelMapperImpl = mock()
    private var presentationMapper: PresentationModelMapperImpl = mock()

    private lateinit var repoImpl: GitRepoRepositoryImpl
    private lateinit var gitRepoUseCase: GitRepoUseCase
    private lateinit var viewModel: RepoViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {

        `when`(
            apiService.fetchRepo()
        ).thenReturn(Single.just(MockApiResponse.repoResponseList))

        `when`(
            daoImpl.getAllRepo()
        ).thenReturn(Flowable.just(MockDBResponse.repoDBList))

        `when`(
            dbMapper.fromEntity(MockApiResponse.repoResponseList)
        ).thenReturn(MockDBResponse.repoDBList)

        `when`(
            presentationMapper.fromEntity(MockDBResponse.repoDBList)
        ).thenReturn(MockResponseForPresentation.responseSuccess)

        repoImpl =
            GitRepoRepositoryImpl(apiService, daoImpl, dbMapper, presentationMapper)
        gitRepoUseCase = GitRepoUseCase(repoImpl)
        viewModel = RepoViewModel(gitRepoUseCase)

        viewModel.getData()

    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun verifyApiResponseAndLDSameSize() {

        val apiData = apiService.fetchRepo()
            .test()
            .values()

        viewModel.formattedList.observeForTesting {
            assertEquals(viewModel.formattedList.value!!.data.size, apiData[0].size)
            assertEquals(
                viewModel.formattedList.value!!.data,
                presentationMapper.fromEntity(dbMapper.fromEntity(apiData[0])).data
            )
        }
    }

    @Test
    fun verifyDBAndLDSameSize() {

        val dbData = daoImpl.getAllRepo()
            .test()
            .values()

        viewModel.formattedList.observeForTesting {
            assertEquals(viewModel.formattedList.value!!.data.size, dbData[0].size)
            assertEquals(
                viewModel.formattedList.value!!.data,
                presentationMapper.fromEntity(dbData[0]).data

            )
        }
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

}