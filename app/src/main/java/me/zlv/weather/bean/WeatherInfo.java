package me.zlv.weather.bean;

/**
 * 天气信息
 * Created by jeremyhe on 16-3-12.
 */
public class WeatherInfo {

    private String date;
    private double minTemperature;
    private double maxTemperature;
    private int codeAtDay;
    private int codeAtNight;
    private String descAtDay;
    private String descAtNight;
    // 降雨量
    private double pcpn;
    // 降雨概率
    private int pop;
    // 湿度
    private int hum;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double temperature) {
        this.minTemperature = temperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double temperature) {
        this.maxTemperature = temperature;
    }

    public int getCodeAtDay() {
        return codeAtDay;
    }

    public void setCodeAtDay(int code) {
        this.codeAtDay = code;
    }

    public int getCodeAtNight() {
        return codeAtNight;
    }

    public void setCodeAtNight(int code) {
        this.codeAtNight = code;
    }

    public String getDescAtDay() {
        return descAtDay;
    }

    public void setDescAtDay(String description) {
        this.descAtDay = description;
    }

    public String getDescAtNight() {
        return descAtNight;
    }

    public void setDescAtNight(String description) {
        this.descAtNight = description;
    }

    public double getPcpn() {
        return pcpn;
    }

    public void setPcpn(double pcpn) {
        this.pcpn = pcpn;
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
