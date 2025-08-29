package com.holimobile.di.component;


import android.content.Context;


import com.holimobile.di.module.ApplicationModule;
import com.holimobile.di.scope.ContextLife;
import com.holimobile.di.scope.PerApp;

import dagger.Component;


@PerApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ContextLife("Application")
    Context getApplication();

}

