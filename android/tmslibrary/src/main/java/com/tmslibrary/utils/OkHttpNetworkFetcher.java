package com.tmslibrary.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.imagepipeline.producers.BaseNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.ProducerContext;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.common.Const;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpNetworkFetcher extends BaseNetworkFetcher {

    private static OkHttpClient mOkClient;

    private Map<String, Call> mCallMap;


    private Context mAppContext;


    static {
        mOkClient = new OkHttpClient.Builder()
                .build();
    }

    public OkHttpNetworkFetcher(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    @Override
    public FetchState createFetchState(Consumer consumer, ProducerContext producerContext) {
        return new FetchState(consumer, producerContext);
    }

    @Override
    public void fetch(FetchState fetchState, final Callback callback) {

        final Request request = buildRequest(fetchState);
        Call fetchCall = mOkClient.newCall(request);

        if(mCallMap == null)
            mCallMap = new HashMap<>();

        mCallMap.put(fetchState.getId(), fetchCall);

        final String fetchId = fetchState.getId();

        fetchState.getContext().addCallbacks(new BaseProducerContextCallbacks(){
            @Override
            public void onCancellationRequested() {
                cancelAndRemove(fetchId);
                callback.onCancellation();
            }
        });

        try {
            Response response =  fetchCall.execute();
            ResponseBody responseBody = response.body();
            callback.onResponse(responseBody.byteStream(), (int) responseBody.contentLength());
        } catch (IOException e) {
            callback.onFailure(e);
        }

        removeCall(fetchId);
    }


    private void removeCall(String id){
        if(mCallMap != null){
            mCallMap.remove(id);
        }
    }

    private void cancelAndRemove(String id){
        if(mCallMap != null){
            Call call = mCallMap.remove(id);
            if(call != null)
                call.cancel();
        }
    }


    private Request buildRequest(FetchState fetchState){
        DisplayMetrics dm  = mAppContext.getResources().getDisplayMetrics();
        int density =  dm.densityDpi;
        String token = PreferenceUtils.getPrefString(TmsLibraryApp.getInstances(), Const.KEY_HOLI_T,"");
        Log.i("TOKEN","buildRequest token:"+token);
        return new Request.Builder()
                .addHeader("x-density", String.valueOf(density))
                .addHeader("Cookie","token="+token)
                .get()
                .url(fetchState.getUri().toString())
                .build();

    }


}