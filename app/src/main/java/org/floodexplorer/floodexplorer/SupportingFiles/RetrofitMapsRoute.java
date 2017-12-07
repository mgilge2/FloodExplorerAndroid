package org.floodexplorer.floodexplorer.SupportingFiles;

/**
 * Created by mgilge on 7/17/17.
 */


import org.floodexplorer.floodexplorer.SupportingFiles.POJO.Route.RouteExample;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by navneet on 17/7/16.
 */
public interface RetrofitMapsRoute
{
    @GET("api/directions/json?key=AIzaSyAOtM7z8SsRneqeap8xmbtT_Px5E2JQINI")
    Call<RouteExample> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);
}

