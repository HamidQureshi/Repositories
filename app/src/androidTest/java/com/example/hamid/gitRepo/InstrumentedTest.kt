package com.example.hamid.gitRepo

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.hamid.gitRepo.presentation.di.module.HttpClientModule
import com.example.hamid.gitRepo.presentation.ui.activity.RepoActivity
import com.example.hamid.gitRepo.presentation.ui.viewmodel.RepoViewModel
import com.hamid.data.GitRepoRepositoryImpl
import com.hamid.data.local.db.RepoDaoImpl
import com.hamid.data.local.db.RepositoryRoomDatabase
import com.hamid.data.model.ModelMapperImpl
import com.hamid.data.model.PresentationModelMapperImpl
import com.hamid.data.remote.APIService
import com.hamid.data.utils.EspressoIdlingResource
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.usecases.GitRepoUseCase
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class InstrumentedTest {

    private lateinit var apiService: APIService
    private lateinit var db: RepositoryRoomDatabase
    private lateinit var daoImpl: RepoDaoImpl
    private val dbMapper = ModelMapperImpl()
    private val presentationMapper = PresentationModelMapperImpl()
    private lateinit var repositoryImpl: GitRepoRepositoryImpl
    private lateinit var gitRepoUseCase: GitRepoUseCase
    private lateinit var viewModel: RepoViewModel


    @get:Rule
    var activityRule: ActivityTestRule<RepoActivity>? = ActivityTestRule(
        RepoActivity::class.java
    )
    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var context: Context

    @Before
    @Throws(Exception::class)
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context, RepositoryRoomDatabase::class.java
        ).build()

        daoImpl = db.dao()

        val httpModule = HttpClientModule()
        val cache = httpModule.provideCache(context.applicationContext as Application)
        val httpClient = httpModule.provideOkhttpClient(cache)
        val retrofit = httpModule.provideRetrofit(httpClient)

        apiService = httpModule.provideApiService(retrofit)

        repositoryImpl =
            GitRepoRepositoryImpl(apiService, daoImpl, dbMapper, presentationMapper)

        gitRepoUseCase = GitRepoUseCase(repositoryImpl)


        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        viewModel = RepoViewModel(gitRepoUseCase)
        repositoryImpl.getRepoFromServer()
        viewModel.getData()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
        activityRule = null
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }


    @Test
    fun verifyLiveDataHasSomeValue() {
        viewModel.formattedList.observeForTesting {
            Assert.assertNotNull(viewModel.formattedList.value)
        }
    }

    @Test
    fun verifyLiveDataHasLoadingResponse() {
        viewModel.formattedList.observeForTesting {
            Assert.assertTrue(viewModel.formattedList.value!!.status == Status.LOADING)
        }
    }

    @Test
    fun verifyApiResponseAndDbSameSize() {

        viewModel.formattedList.observeForTesting {
            Assert.assertTrue(viewModel.formattedList.value!!.data.size == daoImpl.getAllRepo().test().values()[0].size)
        }
    }

    @Test
    fun verifyApiResponseAndDbSameData() {

        viewModel.formattedList.observeForTesting {
            Assert.assertTrue(
                viewModel.formattedList.value!!.data == presentationMapper.fromEntity(
                    daoImpl.getAllRepo().test().values()[0]
                ).data
            )
        }
    }


    //should be accessed from a common class
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

