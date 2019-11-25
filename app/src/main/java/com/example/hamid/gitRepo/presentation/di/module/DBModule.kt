package com.example.hamid.gitRepo.presentation.di.module

import android.app.Application
import androidx.room.Room
import androidx.test.espresso.idling.CountingIdlingResource
import com.hamid.data.GitRepoRepositoryImpl
import com.hamid.data.local.db.RepoDaoImpl
import com.hamid.data.local.db.RepositoryRoomDatabase
import com.hamid.data.model.ModelMapperImpl
import com.hamid.data.model.PresentationModelMapperImpl
import com.hamid.data.remote.APIService
import com.hamid.domain.model.repository.GitRepoRepository
import com.hamid.domain.model.usecases.GitRepoUseCase
import com.hamid.domain.model.utils.Constants
import dagger.Module
import dagger.Provides
import io.reactivex.annotations.NonNull

import javax.inject.Singleton

@Module
class DBModule {

    @Provides
    @Singleton
    fun provideDatabase(@NonNull application: Application): RepositoryRoomDatabase {
        return Room.databaseBuilder(
            application,
            RepositoryRoomDatabase::class.java,
            Constants.dbName
        )
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideItemDao(@NonNull appDatabase: RepositoryRoomDatabase): RepoDaoImpl {
        return appDatabase.dao()
    }

    @Singleton
    @Provides
    fun providePresentationModelMapper() = PresentationModelMapperImpl()

    @Singleton
    @Provides
    fun provideDBModelMapper() = ModelMapperImpl()

    @Provides
    @Singleton
    fun provideRepository(@NonNull apiService: APIService, @NonNull daoImpl: RepoDaoImpl, @NonNull dbMapper: ModelMapperImpl, @NonNull presentationMapper: PresentationModelMapperImpl): GitRepoRepository {
        return GitRepoRepositoryImpl(
            apiService,
            daoImpl,
            dbMapper,
            presentationMapper
        )
    }

    @Provides
    @Singleton
    fun provideIdlingResource(): CountingIdlingResource {
        return CountingIdlingResource(Constants.idlingResourceName)
    }


    @Provides
    @Singleton
    fun provideUseCase(@NonNull gitRepoRepository: GitRepoRepository) =
        GitRepoUseCase(gitRepoRepository)

}
