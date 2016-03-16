package me.zlv.weather;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.zlv.weather.bean.WeatherForecast;

/**
 * Adapter for daily forecast.
 * Created by jeremyhe on 16-3-16.
 */
public class ForecastAdapter extends BaseAdapter{

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    private LayoutInflater mInflater;
    private List<WeatherForecast> mWeatherForecastList;

    @ColorInt
    private int mPrimaryTextColor;

    public ForecastAdapter(Context context, List<WeatherForecast> weatherForecastList) {
        mInflater = LayoutInflater.from(context);
        mWeatherForecastList = weatherForecastList;
        mPrimaryTextColor = context.getResources().getColor(R.color.primaryText);
    }

    @Override
    public int getCount() {
        return mWeatherForecastList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWeatherForecastList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_forecast, parent, false);
            holder = new ViewHolder();
            holder.dateTv = (TextView) convertView.findViewById(R.id.date_tv);
            holder.temperatureTv = (TextView) convertView.findViewById(R.id.temperature_tv);
            holder.percentTv = (TextView) convertView.findViewById(R.id.percent_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WeatherForecast forecast = (WeatherForecast) getItem(position);
        holder.dateTv.setText(forecast.getDate());
        holder.temperatureTv.setText((int)forecast.getTemperature() + "℃");

        final int percent = forecast.getPop();
        holder.percentTv.setText(percent + "%");
        if (percent > 50) {
            holder.percentTv.setTextColor(Color.RED);
        } else {
            holder.percentTv.setTextColor(mPrimaryTextColor);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView dateTv;
        TextView temperatureTv;
        TextView percentTv;
    }
}
