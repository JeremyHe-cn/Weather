package me.zlv.weather.result;

import java.util.List;

import me.zlv.weather.bean.CityInfo;
import me.zlv.weather.bean.WeatherForecast;
import me.zlv.weather.bean.WeatherInfo;

/**
 * 查询天气的接口
 * Created by jeremyhe on 16-3-12.
 */
public class QueryWeatherResult {
    private CityInfo cityInfo;
    private WeatherInfo weatherInfo;
    private List<WeatherForecast> hourForecastList;

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public List<WeatherForecast> getHourForecastList() {
        return hourForecastList;
    }

    public void setHourForecastList(List<WeatherForecast> hourForecastList) {
        this.hourForecastList = hourForecastList;
    }
}
