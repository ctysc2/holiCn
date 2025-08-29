package com.tmslibrary.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.common.hash.Hashing;
import com.tmslibrary.common.Const;
import com.tmslibrary.entity.FileUploadEntity;
import com.tmslibrary.mvp.interactor.impl.FileUploadInteractorImpl;
import com.tmslibrary.mvp.presenter.impl.FileUploadPresenterImpl;
import com.tmslibrary.mvp.view.FileUploadView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FileUtils {
    private static String[] previewTypes = {"doc","docx","xls","xlsx","pdf","ppt","pptx","png","jpg","jpeg"};

    public static boolean canPreview(String fileName){

        if(TextUtils.isEmpty(fileName)){
            return true;
        }

        boolean canPreview = false;
        String[] arr = fileName.split("\\.");
        if(arr.length > 0){
            String suf = arr[arr.length - 1];
            if(!TextUtils.isEmpty(suf)){
                for(int i = 0;i<previewTypes.length;i++){
                    if(suf.toLowerCase().equals(previewTypes[i])){
                        return true;
                    }
                }
            }
        }
        return canPreview;
    }

    public static String getFileName(String path){
        String fileName = "";

        if(TextUtils.isEmpty(path)){
            return fileName;
        }

        String[] arr = path.split("/");
        if(arr.length > 0){
            fileName = arr[arr.length - 1];
        }
        return fileName;
    }

    private static String sign  = "";


    public static void uploadFile(File file, OnUploadSingleListener listener){

        if(file == null){
            return;
        }
        HashMap<Integer,String> requestMap = new HashMap<>();

        try {
            sign = Hashing.sha256().newHasher().putString(EncryptUtil.AESEncrypt("appId=09&contentType=&fileName=&tenantId=", Const.UPLOAD_RESOURCE_AES_KEY).trim(), Charset.forName("UTF-8")).hash().toString();
        }catch (Exception e){
            sign = "";
        }

        if(TextUtils.isEmpty(sign)){
            listener.onUploadCompleted(null);
            return;
        }


        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        FileUploadPresenterImpl mFileUploadPresenterImpl = new FileUploadPresenterImpl(new FileUploadInteractorImpl());
        mFileUploadPresenterImpl.attachView(new FileUploadView() {
            @Override
            public void fileUploadCompleted(FileUploadEntity data) {
                if(data != null){
                    if(listener != null){
                        listener.onUploadCompleted(data.getData());
                    }
                }
            }

            @Override
            public void showProgress(String reqType) {

            }

            @Override
            public void hideProgress(String reqType) {

            }

            @Override
            public void showErrorMsg(String reqType, String msg) {
                if(listener != null){
                    listener.onUploadCompleted(null);
                }
            }
        });
        RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/png"), file);
        try {
            requestBodyMap.put("file" + "\";filename=\"" + URLEncoder.encode(file.getName(), "utf-8"), requestBodyFile);
        } catch (UnsupportedEncodingException e) {
            requestBodyMap.put("file" + "\";filename=\"" + file.getName(), requestBodyFile);
        }
        requestBodyMap.put("appId", RequestBody.create(MediaType.parse("multipart/form-data"), "09"));
        requestBodyMap.put("sign", RequestBody.create(MediaType.parse("multipart/form-data"), sign));
        requestBodyMap.put("tenantId", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        requestBodyMap.put("contentType", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        requestBodyMap.put("fileName", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
        mFileUploadPresenterImpl.fileUpload(requestBodyMap);
    }


    public static void uploadFiles(ArrayList<File> localFiles,OnUploadListener listener){

        if(localFiles == null){
            return;
        }
        HashMap<Integer,String> requestMap = new HashMap<>();

        try {
            //Log.i("aes","aes:"+EncryptUtil.AESEncrypt("appId=09&contentType=&fileName=&tenantId=", Const.UPLOAD_RESOURCE_AES_KEY));
            sign = Hashing.sha256().newHasher().putString(EncryptUtil.AESEncrypt("appId=09&contentType=&fileName=&tenantId=", Const.UPLOAD_RESOURCE_AES_KEY).trim(), Charset.forName("UTF-8")).hash().toString();
            //Log.i("aes","sign:"+sign);
        }catch (Exception e){
            sign = "";
        }

        if(TextUtils.isEmpty(sign)){
            listener.onUploadCompleted(null);
            return;
        }

        for(int i = 0;i<localFiles.size();i++){
            File file = localFiles.get(i);
            Map<String, RequestBody> requestBodyMap = new HashMap<>();
            final int pos = i;
            FileUploadPresenterImpl mFileUploadPresenterImpl = new FileUploadPresenterImpl(new FileUploadInteractorImpl());
            mFileUploadPresenterImpl.attachView(new FileUploadView() {
                @Override
                public void fileUploadCompleted(FileUploadEntity data) {
                    if(data != null){
                        requestMap.put(pos,data.getData().getPreviewUrl());
                        setResult(requestMap,localFiles.size(),listener);
                    }
                }

                @Override
                public void showProgress(String reqType) {

                }

                @Override
                public void hideProgress(String reqType) {

                }

                @Override
                public void showErrorMsg(String reqType, String msg) {
                    requestMap.put(pos,"");
                    setResult(requestMap,localFiles.size(),listener);
                    Log.i("fileUpload","err:"+msg);

                }
            });
            RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/png"), file);
            try {
                requestBodyMap.put("file" + "\";filename=\"" + URLEncoder.encode(file.getName(), "utf-8"), requestBodyFile);
            } catch (UnsupportedEncodingException e) {
                requestBodyMap.put("file" + "\";filename=\"" + file.getName(), requestBodyFile);
            }
            requestBodyMap.put("appId", RequestBody.create(MediaType.parse("multipart/form-data"), "09"));
            requestBodyMap.put("sign", RequestBody.create(MediaType.parse("multipart/form-data"), sign));
            requestBodyMap.put("tenantId", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
            requestBodyMap.put("contentType", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
            requestBodyMap.put("fileName", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
            mFileUploadPresenterImpl.fileUpload(requestBodyMap);
        }
    }

    private static void setResult(HashMap<Integer,String> hashMap,int maxSize,OnUploadListener listener){
        ArrayList<String> result = new ArrayList<>();
        if(hashMap.size() == maxSize){

            for(int i = 0;i<maxSize;i++){
                if(!TextUtils.isEmpty(hashMap.get(i))){
                    result.add(hashMap.get(i));
                }
            }
            if(listener != null){
                listener.onUploadCompleted(result);
            }


        }
    }

    public static interface OnUploadListener{
        void onUploadCompleted(ArrayList<String> urls);
    }

    public static interface OnUploadSingleListener{
        void onUploadCompleted(FileUploadEntity.DataEntity data);
    }
}
