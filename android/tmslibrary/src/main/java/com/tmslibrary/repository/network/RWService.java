package com.tmslibrary.repository.network;

import com.tmslibrary.entity.ApolloConfigEntity;
import com.tmslibrary.entity.CheckAccountConfirmEntity;
import com.tmslibrary.entity.DocConfigDetailEntity;
import com.tmslibrary.entity.FileUploadEntity;
import com.tmslibrary.entity.LoginEntity;
import com.tmslibrary.entity.VerifyCodeEntity;
import com.tmslibrary.entity.VersionCheckEntity;
import com.tmslibrary.entity.base.BaseErrorEntity;
import com.tmslibrary.entity.base.BaseHoLiEntity;

import java.util.HashMap;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cty on 2016/12/10.
 */

public interface RWService {

    @GET("/apps/latest/{bundleId}?api_token=dd1bdb456f8518ab91ca92148d85fe7e")
    Observable<VersionCheckEntity> getTestVersionCheck(@Path("bundleId") String bundleId);

    @POST("/api/version/checkHandlecar")
    Observable<VersionCheckEntity> getProductVersionCheck(@Body RequestBody body);

    @GET("/api/omp-service/app/updateDownLoadCount")
    Observable<BaseErrorEntity> updateDownloadCount(@Query("id") String id);

    @GET("/api/csp-service/accountConfirm/checkAccountConfirm")
    Observable<CheckAccountConfirmEntity> checkAccountConfirm(@Query("confirmType") String confirmType);

    @GET("/api/csp-service/accountConfirm/saveAccountConfirm")
    Observable<BaseErrorEntity> saveAccountConfirm(@Query("docConfigDetailId") String docConfigDetailId,@Query("confirmType") String confirmType);

    @GET("/api/omp-service/docConfig/getDocConfigDetail")
    Observable<DocConfigDetailEntity> getDocConfigDetail(@Query("docConfigType") String docConfigType, @Query("appId") String appId);

    @Multipart
    @POST("/file/resource/upload")
    Observable<FileUploadEntity> fileUpload(@PartMap Map<String, RequestBody> requestBodyMap);

    @GET("/api/trialos-service/appApollo/info")
    Observable<ApolloConfigEntity> getApolloConfig(@Query("appId") String appId);

    //holimobile
    @POST("/api/user/verifyCode")
    Observable<VerifyCodeEntity> verifyCode(@Body RequestBody body);

    @POST("/api/user/login")
    Observable<LoginEntity> login(@Body VerifyCodeEntity.DataEntity body);

    @POST("/api/user/logout")
    Observable<BaseHoLiEntity> logout();

    @POST("/api/operation/save")
    Observable<BaseHoLiEntity> saveScan(@Body RequestBody body);

}


