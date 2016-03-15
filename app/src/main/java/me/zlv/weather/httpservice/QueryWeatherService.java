package me.zlv.weather.httpservice;

import me.zlv.weather.result.QueryWeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 查询天气的相关服务
 * Created by jeremyhe on 16-3-12.
 */
public interface QueryWeatherService {
    @GET("x3/weather")
    Call<QueryWeatherResult> queryWeather(@Query("city") String cityId, @Query("key") String apiKey);
}
