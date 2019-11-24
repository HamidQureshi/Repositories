package com.example.hamid.gitRepo.presentation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hamid.gitRepo.presentation.factory.ViewModelFactory
import com.example.hamid.gitRepo.presentation.ui.viewmodel.RepoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(RepoViewModel::class)
    protected abstract fun viewModel(viewModel: RepoViewModel): ViewModel

}
