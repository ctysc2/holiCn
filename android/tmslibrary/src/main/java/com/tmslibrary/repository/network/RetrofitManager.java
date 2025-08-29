package com.tmslibrary.repository.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;
import com.tmslibrary.R;
import com.tmslibrary.app.TmsLibraryApp;
import com.tmslibrary.common.ApiConstants;
import com.tmslibrary.common.Const;
import com.tmslibrary.entity.ApolloConfigEntity;
import com.tmslibrary.entity.CheckAccountConfirmEntity;
import com.tmslibrary.entity.DocConfigDetailEntity;
import com.tmslibrary.entity.FileUploadEntity;
import com.tmslibrary.entity.LoginEntity;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.entity.VersionCheckEntity;
import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;
import com.tmslibrary.utils.LogUtils;
import com.tmslibrary.utils.PreferenceUtils;
import com.tmslibrary.utils.SystemTool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by cty on 2016/12/10.
 */

public class RetrofitManager {

    private RWService mRWService;
    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";

    private static volatile OkHttpClient sOkHttpClient;

    private static HashMap<String,RetrofitManager> sRetrofitManager = new HashMap<>();

    public RetrofitManager(String hostType) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Log.i("BBBB","baseUrl:"+ApiConstants.getHost(hostType));
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiConstants.getHost(hostType))
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mRWService = retrofit.create(RWService.class);
    }

    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                Cache cache = new Cache(new File(TmsLibraryApp.getInstances().getCacheDir(), "HttpCache"),
                        1024 * 1024 * 100);
                if (sOkHttpClient == null) {
                    sOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addInterceptor(mLoggingInterceptor)
//                            .sslSocketFactory(getSSLSocketFactory())
//                            .hostnameVerifier(getHostnameVerifier())
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .build();
                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!SystemTool.isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            //request = request.newBuilder().addHeader("Cookie","123").build();
            //KLog.i("Retrofit","request.headers:"+request.headers());

            Response originalResponse;
            String token = PreferenceUtils.getPrefString(TmsLibraryApp.getInstances(), Const.KEY_TOKEN,"");
            request = request.newBuilder()
                    .addHeader("accessToken",token)
                    .addHeader("Accept-Language","zh_CN")
                    .build();
            LogUtils.printJson("Retrofit requestHeader",request.headers().toString(),"请求Header");
            originalResponse = chain.proceed(request);

            if (SystemTool.isNetworkAvailable()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                //String cacheControl = request.cacheControl().toString();
                String cacheControl = getCacheControl();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();

            }
        }
    };

    private final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {


            if (!SystemTool.isNetworkAvailable()) {
                throw new IOException(TmsLibraryApp.getInstances().getString(R.string.network_err));
            }


            Request request = chain.request();
            long t1 = System.nanoTime();
            String method = request.method();
            KLog.i(String.format(" Retrofit Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            String body = null;
            RequestBody requestBody = request.body();
            if(requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = null;

                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                }
                if(charset != null){
                    body = buffer.readString(charset);
                }
            }
            //KLog.i(String.format(" Retrofit Sending request body %s", body));
            LogUtils.printJson("Retrofit requestbody",body,"请求消息体");
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            KLog.i(String.format(Locale.getDefault(), "Retrofit Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            String logResponseBody = buffer.clone().readString(UTF_8);
            KLog.json("Retrofit","response.body:"+logResponseBody);
            LogUtils.printJson("Retrofit reponsebody",logResponseBody,"响应消息体");
            return response;
        }
    };


    public static void clearManagerCache(){
        sRetrofitManager = new HashMap<>();
    }

    public static RetrofitManager getInstance(String hostType){
        RetrofitManager retrofitManager = sRetrofitManager.get(hostType);
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager(hostType);
            sRetrofitManager.put(hostType, retrofitManager);
            return retrofitManager;
        }
        return retrofitManager;
    }


    /**
     * 根据网络状况获取缓存的策略
     */
    @NonNull
    private String getCacheControl() {
        return SystemTool.isNetworkAvailable() ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }


    public static SSLSocketFactory getSSLSocketFactory()
    {
        try
        {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager()
    {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
        {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
            {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
            {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[]{};
            }
        }};
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier()
    {
        HostnameVerifier hostnameVerifier = new HostnameVerifier()
        {
            @Override
            public boolean verify(String s, SSLSession sslSession)
            {
                return true;
            }
        };
        return hostnameVerifier;
    }


    public Observable<VersionCheckEntity> getTestVersionCheck(String bundleId){
        return mRWService.getTestVersionCheck(bundleId);
    }

    public Observable<VersionCheckEntity> getProductVersionCheck(RequestBody body){
        return mRWService.getProductVersionCheck(body);
    }

    public Observable<BaseErrorEntity> updateDownloadCount(String id){
        return mRWService.updateDownloadCount(id);
    }

    public Observable<CheckAccountConfirmEntity> checkAccountConfirm(@Query("confirmType") String confirmType){
        return mRWService.checkAccountConfirm(confirmType);
    }

    public Observable<BaseErrorEntity> saveAccountConfirm(String docConfigDetailId,String confirmType){
        return mRWService.saveAccountConfirm(docConfigDetailId,confirmType);
    }

    public Observable<DocConfigDetailEntity> getDocConfigDetail(@Query("docConfigType") String docConfigType, @Query("appId") String appId){
        return  mRWService.getDocConfigDetail(docConfigType,appId);
    }

    public Observable<FileUploadEntity> fileUpload(Map<String, RequestBody> body){
        return mRWService.fileUpload(body);
    }

    public Observable<ApolloConfigEntity> getApolloConfig(String appId){
        return mRWService.getApolloConfig(appId);
    }

    public Observable<VerifyCodeEntity> verifyCode(@Body RequestBody body){
        return mRWService.verifyCode(body);
    }

    public Observable<LoginEntity> login(VerifyCodeEntity.DataEntity body){
        return mRWService.login(body);
    }

    public Observable<BaseHoLiEntity> saveScan(RequestBody body){
        return mRWService.saveScan(body);
    }

    public Observable<BaseHoLiEntity> logout(){
        return mRWService.logout();
    }



}
