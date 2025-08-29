package com.holimobile.di.module;

import android.content.Context;


import com.holimobile.app.Application;
import com.holimobile.di.scope.ContextLife;
import com.holimobile.di.scope.PerApp;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @PerApp
    @ContextLife("Application")
    public Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }

}
