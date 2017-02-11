package com.zappos.namanrajpal.ilovezappos;

/**
 * Created by Naman Rajpal on 2/2/2017.
 */
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZapposAPI {

    @GET("/Search?key=b743e26728e16b81da139182bb2094357c31d331")
    Call<ReturnedProducts> loadProducts(@Query("term") String term);

    @GET("/Images?key=b743e26728e16b81da139182bb2094357c31d331")
    Call<ReturnedProducts> loadImages(@Query("term") String id);


}
