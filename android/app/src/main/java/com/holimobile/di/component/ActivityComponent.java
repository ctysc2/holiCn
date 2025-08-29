package com.holimobile.di.component;

import android.app.Activity;
import android.content.Context;

import com.holimobile.di.module.ActivityModule;
import com.holimobile.di.scope.ContextLife;
import com.holimobile.di.scope.PerActivity;
import com.holimobile.mvp.ui.activity.LoginActivity;
import com.holimobile.mvp.ui.activity.MainActivity;
import com.holimobile.mvp.ui.activity.SettingActivity;
import com.holimobile.mvp.ui.activity.VerifyCodeActivity;
import com.holimobile.mvp.ui.activity.common.ImageGalleryActivity;
import com.holimobile.mvp.ui.activity.splash.SplashActivity;
import com.holimobile.mvp.ui.activity.webview.WebViewActivity;


import dagger.Component;

/**
 * Created by cty on 16/10/19.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(SplashActivity activity);
    void inject(WebViewActivity activity);
    void inject(ImageGalleryActivity activity);
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(SettingActivity activity);
    void inject(VerifyCodeActivity activity);


}