package com.hamid.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hamid.data.local.db.RepoDaoImpl
import com.hamid.data.model.DBModelMapperImpl
import com.hamid.data.model.PresentationModelMapperImpl
import com.hamid.data.model.RepoDBModel
import com.hamid.data.remote.APIService
import com.hamid.data.utils.helper.MockApiResponse
import com.hamid.data.utils.helper.MockDBResponse
import com.hamid.data.utils.helper.MockResponseForPresentation
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.only
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mockito.`when`


class RepositoryTest {

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var apiService: APIService = mock()
    private var daoImpl: RepoDaoImpl =
        mock()
    private var dbMapper: DBModelMapperImpl = mock()
    private var presentationMapper: PresentationModelMapperImpl = mock()

    private lateinit var repoImpl: GitRepoRepositoryImpl

    @Before
    fun setUp() {

        `when`(
            apiService.fetchRepo()
        )
            .thenReturn(Single.just(MockApiResponse.repoResponseList))

        `when`(
            daoImpl.getAllRepo()
        )
            .thenReturn(Flowable.just(MockDBResponse.repoDBList))

        `when`(
            dbMapper.fromEntity(MockApiResponse.repoResponseList)
        ).thenReturn(MockDBResponse.repoDBList)

        `when`(
            presentationMapper.fromEntity(MockDBResponse.repoDBList)
        ).thenReturn(MockResponseForPresentation.responseSuccess)

        repoImpl =
            GitRepoRepositoryImpl(apiService, daoImpl, dbMapper, presentationMapper)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getDataFromServer_apiCalled() {

        repoImpl.getRepoFromServer()

        verify(apiService, times(1))
            .fetchRepo()

        verify(dbMapper, times(1))
            .fromEntity(MockApiResponse.repoResponseList)

        verify(daoImpl, times(1))
            .insertAll(MockDBResponse.repoDBList)
    }

    @Test
    fun insertRepoListToDB_insertToDbCall() {

        repoImpl.insertRepoToDB(MockDBResponse.repoDBList)

        verify(daoImpl, only())
            .insertAll(MockDBResponse.repoDBList)
    }

    @Test
    fun nukeDB_deleteDbCall() {

        repoImpl.nukeDB()

        verify(daoImpl, only())
            .deleteAll()
    }


    @Test
    fun clearDisposable_clearsDisposable() {

        val disposableSize = repoImpl.getDisposable().size()

        assertTrue(disposableSize == 0)

        repoImpl.getRepoFromServer()

        assertTrue(disposableSize + 1 == repoImpl.getDisposable().size())

        repoImpl.clearDisposable()

        assertTrue(0 == repoImpl.getDisposable().size())

    }

    @Test
    fun getDataFromServer_returnsRepositories() {
        val expectedValue = MockApiResponse.repoResponseList

        val actualValue = apiService.fetchRepo()
            .test()
            .values()

        assertEquals(expectedValue, actualValue[0])
    }

    @Test
    fun getAllRepo_containsList() {
        val expectedValue = MockResponseForPresentation.responseSuccess

        val actualValue = repoImpl.getRepoFromDb()
            .test()
            .values()

        assertEquals(expectedValue.status, actualValue[0].status)
        assertEquals(expectedValue.data, actualValue[0].data)

    }

    @Test
    fun getAllRepos_containsEmptyList() {
        val expectedValue = emptyList<RepoDBModel>()

        `when`(
            presentationMapper.fromEntity(emptyList())
        ).thenReturn(MockResponseForPresentation.responseFailure)

        `when`(daoImpl.getAllRepo())
            .thenReturn(Flowable.just(expectedValue))

        repoImpl =
            GitRepoRepositoryImpl(apiService, daoImpl, dbMapper, presentationMapper)

        val actualValue = repoImpl.getRepoFromDb()
            .test()
            .values()

        assertEquals(expectedValue.size, actualValue[0].data.size)
    }

    @Test
    fun verifySizeOfApiResponseAndDbSame() {

        val apiData = apiService.fetchRepo()
            .test()
            .values()

        val dbData = daoImpl.getAllRepo()
            .test()
            .values()

        assertEquals(apiData[0].size, dbData[0].size)
    }

    @Test
    fun verifyApiResponseAndDbSame() {

        val apiData = apiService.fetchRepo()
            .test()
            .values()

        val dbData = daoImpl.getAllRepo()
            .test()
            .values()

        assertEquals(dbMapper.fromEntity(apiData[0]), dbData[0])
    }

    @Test
    fun getDataFromServerAndDBDataSame() {

        val actualValue = repoImpl.getRepoFromDb()
            .test()
            .values()

        assertEquals(
            presentationMapper.fromEntity(MockDBResponse.repoDBList).data,
            actualValue[0].data
        )

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