package me.zlv.weather.bean;

/**
 * 城市基本信息
 * Created by jeremyhe on 16-3-12.
 */
public class CityInfo {
    private String city;  // 城市名称
    private String id;  // 城市ID
    private String lat;  // 纬度
    private String lon;  // 经度
    private String updateTime;  // 数据更新的当地时间

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
