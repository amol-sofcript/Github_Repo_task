package com.amol.example.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.amol.example.di.ViewModelKey;
import com.amol.example.factory.ViewModelFactory;
import com.amol.example.ui.viewmodel.GithubListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(GithubListViewModel.class)
    protected abstract ViewModel githubListViewModel(GithubListViewModel githubListViewModel);
}