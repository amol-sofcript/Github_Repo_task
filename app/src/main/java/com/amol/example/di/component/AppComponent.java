package com.amol.example.di.component;

import android.app.Application;

import com.amol.example.AppController;
import com.amol.example.di.module.ActivityModule;
import com.amol.example.di.module.ApiModule;
import com.amol.example.di.module.DbModule;
import com.amol.example.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Component(modules = {
                ApiModule.class,
                DbModule.class,
                ViewModelModule.class,
                ActivityModule.class,
                AndroidSupportInjectionModule.class})
@Singleton
public interface AppComponent {


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }


    void inject(AppController appController);
}
