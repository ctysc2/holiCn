package com.tmslibrary.utils;

import java.util.ArrayList;

public class ParallelRequestUtils {

    private ArrayList<Object> responseArr = new ArrayList<>();

    private int count = 0;

    private ParallelResponseCallBack responseCallBack;

    public void setCount(int count){
        this.count = count;
    }

    public synchronized void setResponse(Object response){
        responseArr.add(response);
        if(responseArr.size() == count){
            if(responseCallBack != null){
                responseCallBack.onResponse(responseArr);
                responseArr = new ArrayList<>();
            }
        }
    }

    public void setResponseCallBack(ParallelResponseCallBack responseCallBack) {
        this.responseCallBack = responseCallBack;
    }


    public  interface ParallelRequestCallBack{
        void onRequest();
    }

    public  interface ParallelResponseCallBack{
        void onResponse(ArrayList<Object> responseArr);
    }
}
