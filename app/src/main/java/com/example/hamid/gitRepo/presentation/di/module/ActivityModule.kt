package com.example.hamid.gitRepo.presentation.di.module

import com.example.hamid.gitRepo.presentation.ui.activity.RepoActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeActivity(): RepoActivity

}
