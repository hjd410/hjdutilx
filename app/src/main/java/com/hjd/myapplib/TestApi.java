package com.hjd.myapplib;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by HJD on 2021/1/11 0011 and 14:43.
 */
interface TestApi {

    @POST("version/getVersion")
    Call<ResponseBody> getBlog();
}
