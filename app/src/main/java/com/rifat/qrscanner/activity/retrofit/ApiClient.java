package com.rifat.qrscanner.activity.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String BASE_URL = "http://mrifat.com/";
    private ApiClient mInstance;
    private Retrofit retrofit;

    public ApiClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public ApiClient getInstance(){
        if (mInstance == null){
            mInstance = new ApiClient();
        }
        return mInstance;
    }
    public RetrofitInterface getApi(){
        return retrofit.create(RetrofitInterface.class);
    }
}

