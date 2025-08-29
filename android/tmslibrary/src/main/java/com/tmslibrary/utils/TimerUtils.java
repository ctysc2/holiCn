package com.tmslibrary.utils;

import com.tmslibrary.listener.TimerCallback;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;


//延时工具类
public class TimerUtils {
    //延迟
    public static void delay(long delay, final TimerCallback callback){
        Observable.timer(delay, TimeUnit.MILLISECONDS).compose(TransformUtils.<Object>defaultSchedulers())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        if(callback != null){
                            callback.onTimerEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object data) {

                    }

                });
    }
    public static Subscription delayWithResult(long delay, final TimerCallback callback){
        return Observable.timer(delay, TimeUnit.MILLISECONDS).compose(TransformUtils.<Object>defaultSchedulers())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        if(callback != null){
                            callback.onTimerEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object data) {

                    }

                });
    }
    //轮询
    public static Subscription interval(long interval,final TimerCallback callback){
        return Observable.interval(interval,TimeUnit.MILLISECONDS)
                .compose(TransformUtils.<Object>defaultSchedulers())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object data) {
                        if(callback != null){
                            callback.onTimerEnd();
                        }
                    }

                });
    }

    public static String getTimeBySecond(int second){
        String min = "";
        String sec = "";

        min  = (second/60<10)?"0"+second/60:""+second/60;
        sec = second%60 < 10?("0"+second%60):(second%60+"");

        return min+":"+sec;
    }
}
