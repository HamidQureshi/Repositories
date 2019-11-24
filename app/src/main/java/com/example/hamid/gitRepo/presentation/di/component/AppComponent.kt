package com.example.hamid.gitRepo.presentation.di.component

import android.app.Application
import com.example.hamid.gitRepo.presentation.di.AppController
import com.example.hamid.gitRepo.presentation.di.module.ActivityModule
import com.example.hamid.gitRepo.presentation.di.module.DBModule
import com.example.hamid.gitRepo.presentation.di.module.HttpClientModule
import com.example.hamid.gitRepo.presentation.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [HttpClientModule::class, DBModule::class, ViewModelModule::class, ActivityModule::class, AndroidSupportInjectionModule::class])
interface AppComponent {

    fun inject(appController: AppController)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}

