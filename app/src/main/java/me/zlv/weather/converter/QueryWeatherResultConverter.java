package me.zlv.weather.converter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.zlv.weather.bean.CityInfo;
import me.zlv.weather.bean.WeatherForecast;
import me.zlv.weather.bean.WeatherInfo;
import me.zlv.weather.result.QueryWeatherResult;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 查询结果转换
 * Created by jeremyhe on 16-3-12.
 */
public final class QueryWeatherResultConverter implements Converter<ResponseBody, QueryWeatherResult> {

    @Override
    public QueryWeatherResult convert(ResponseBody value) throws IOException {
        QueryWeatherResult result = null;

        try {
            String response = value.string();
            JSONObject respJsonObj = new JSONObject(response);
            JSONObject bodyJsonObj = respJsonObj.getJSONArray("HeWeather data service 3.0").getJSONObject(0);
            String status = bodyJsonObj.getString("status");
            if (!"ok".equalsIgnoreCase(status)) {
                return null;
            }

            result = new QueryWeatherResult();

            // 城市基本信息
            JSONObject basicJsonObj = bodyJsonObj.getJSONObject("basic");
            CityInfo cityInfo = new CityInfo();
            cityInfo.setCity(basicJsonObj.optString("city"));
            cityInfo.setId(basicJsonObj.optString("id"));
            cityInfo.setLat(basicJsonObj.optString("lat"));
            cityInfo.setLon(basicJsonObj.optString("lon"));
            cityInfo.setUpdateTime(basicJsonObj.getJSONObject("update").optString("loc"));
            result.setCityInfo(cityInfo);

            // 当前天气状况
            WeatherInfo weatherInfo = new WeatherInfo();
            JSONObject nowJsonObj = bodyJsonObj.getJSONObject("now");
            weatherInfo.setCode(nowJsonObj.getJSONObject("cond").getInt("code"));
            weatherInfo.setDescription(nowJsonObj.getJSONObject("cond").getString("txt"));
            weatherInfo.setTemperature(nowJsonObj.getDouble("tmp"));
            weatherInfo.setPcpn(nowJsonObj.getInt("pcpn"));
            weatherInfo.setHum(nowJsonObj.getInt("hum"));
            result.setWeatherInfo(weatherInfo);

            // 24小时内天气预测
            ArrayList<WeatherForecast> forecastList = new ArrayList<>();
            JSONArray hourForecastJsonArray = bodyJsonObj.getJSONArray("hourly_forecast");
            final int len = hourForecastJsonArray.length();
            for (int i = 0; i < len; i++) {
                JSONObject info = hourForecastJsonArray.getJSONObject(i);
                WeatherForecast weatherForecast = new WeatherForecast();
                weatherForecast.setDate(info.getString("date"));
                weatherForecast.setTemperature(info.getDouble("tmp"));
                weatherForecast.setPop(info.getInt("pop"));
                weatherForecast.setHum(info.getInt("hum"));

                forecastList.add(weatherForecast);
            }
            result.setHourForecastList(forecastList);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            value.close();
        }

        return result;
    }
}
