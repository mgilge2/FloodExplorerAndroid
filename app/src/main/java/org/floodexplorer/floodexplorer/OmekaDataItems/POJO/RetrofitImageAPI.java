package org.floodexplorer.floodexplorer.OmekaDataItems.POJO;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by mgilge on 7/21/17.
 */

public interface RetrofitImageAPI
{
    @GET("api/RetrofitAndroidImageResponse")
    Call<ResponseBody> getImageDetails();

}
