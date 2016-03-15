package me.zlv.weather.bean;

/**
 * 天气信息
 * Created by jeremyhe on 16-3-12.
 */
public class WeatherInfo {

    private double temperature;
    private int code;
    private String description;
    // 降雨量
    private int pcpn;
    // 湿度
    private int hum;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPcpn() {
        return pcpn;
    }

    public void setPcpn(int pcpn) {
        this.pcpn = pcpn;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }
}
