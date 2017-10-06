package org.floodexplorer.floodexplorer;

/**
 * Created by mgilge on 7/17/17.
 */


import org.floodexplorer.floodexplorer.OmekaDataItems.POJO.Route.RouteExample;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by navneet on 17/7/16.
 */
public interface RetrofitMapsRoute
{

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/directions/json?key=AIzaSyC22GfkHu9FdgT9SwdCWMwKX1a4aohGifM")
    Call<RouteExample> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}

