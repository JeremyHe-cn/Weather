package me.zlv.weather.bean;

/**
 * 天气预测
 * Created by jeremyhe on 16-3-12.
 */
public class WeatherForecast {
    private String date;	        // 当地日期和时间
    private double temperature;	        // 当前温度(摄氏度)
    private int pop;	        // 降水概率
    private int hum;	        // 湿度(%)

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }
}
