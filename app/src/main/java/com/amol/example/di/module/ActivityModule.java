package com.amol.example.di.module;

import com.amol.example.ui.activity.GithubListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract GithubListActivity contributeGithubListActivity();
}