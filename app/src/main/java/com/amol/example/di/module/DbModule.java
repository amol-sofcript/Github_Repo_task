package com.amol.example.di.module;


import android.app.Application;
import androidx.room.Room;
import androidx.annotation.NonNull;

import com.amol.example.data.local.AppDatabase;
import com.amol.example.data.local.dao.GithubDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "Github.db")
                .allowMainThreadQueries().build();
    }

    @Provides
    @Singleton
    GithubDao provideGithubDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.githubDao();
    }
}
