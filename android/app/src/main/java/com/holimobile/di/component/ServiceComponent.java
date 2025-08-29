package com.holimobile.di.component;


import android.content.Context;


import com.holimobile.di.module.ServiceModule;
import com.holimobile.di.scope.ContextLife;
import com.holimobile.di.scope.PerService;

import dagger.Component;


@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
