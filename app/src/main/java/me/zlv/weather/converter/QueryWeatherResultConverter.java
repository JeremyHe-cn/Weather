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
            WeatherInfo nowWeatherInfo = new WeatherInfo();
            JSONObject nowJsonObj = bodyJsonObj.getJSONObject("now");
            nowWeatherInfo.setCodeAtDay(nowJsonObj.getJSONObject("cond").getInt("code"));
            nowWeatherInfo.setDescAtDay(nowJsonObj.getJSONObject("cond").getString("txt"));
            nowWeatherInfo.setMinTemperature(nowJsonObj.getDouble("tmp"));
            nowWeatherInfo.setPcpn(nowJsonObj.getInt("pcpn"));
            nowWeatherInfo.setHum(nowJsonObj.getInt("hum"));
            result.setWeatherInfo(nowWeatherInfo);

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

            // 近几天天气预测
            JSONArray dailyForecastJsonArray = bodyJsonObj.getJSONArray("daily_forecast");
            ArrayList<WeatherInfo> dailyWeatherInfoForecastList = new ArrayList<>();
            final int dailyForecastLen = dailyForecastJsonArray.length();
            for (int i = 0; i < dailyForecastLen; i++) {
                JSONObject forecastJsonObj = dailyForecastJsonArray.getJSONObject(i);
                WeatherInfo weatherInfo = new WeatherInfo();
                JSONObject condJsonObj = forecastJsonObj.getJSONObject("cond");
                weatherInfo.setCodeAtDay(condJsonObj.getInt("code_d"));
                weatherInfo.setDescAtDay(condJsonObj.getString("txt_d"));
                weatherInfo.setCodeAtNight(condJsonObj.getInt("code_n"));
                weatherInfo.setDescAtNight(condJsonObj.getString("txt_n"));

                weatherInfo.setDate(forecastJsonObj.getString("date"));
                weatherInfo.setHum(forecastJsonObj.getInt("hum"));
                weatherInfo.setPcpn(forecastJsonObj.getDouble("pcpn"));
                weatherInfo.setPop(forecastJsonObj.getInt("pop"));

                JSONObject tempJsonObj = forecastJsonObj.getJSONObject("tmp");
                weatherInfo.setMinTemperature(tempJsonObj.getInt("min"));
                weatherInfo.setMaxTemperature(tempJsonObj.getInt("max"));

                dailyWeatherInfoForecastList.add(weatherInfo);
            }
            result.setDailyWeatherInfoForecastList(dailyWeatherInfoForecastList);

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            value.close();
        }

        return result;
    }
}
