package com.holimobile.di.component;


import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;


import com.holimobile.di.module.FragmentModule;
import com.holimobile.di.scope.ContextLife;
import com.holimobile.di.scope.PerFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    FragmentActivity getFragmentActivity();



}
