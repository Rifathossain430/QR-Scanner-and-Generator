package com.rifat.qrscanner.activity.retrofit;

import com.rifat.qrscanner.activity.model.Bar;
import com.rifat.qrscanner.activity.model.BarNew;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @GET("api")
    Call<List<Bar>> getData();
    @POST("api")
    Call<Bar> postData(@Body Bar bar);
}
