package me.zlv.weather;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zlv.weather.bean.WeatherForecast;
import me.zlv.weather.bean.WeatherInfo;

/**
 * Adapter for daily forecast.
 * Created by jeremyhe on 16-3-16.
 */
public class ForecastAdapter extends BaseAdapter{

    private static int VIEW_TYPE_TITLE = 0;
    private static int VIEW_TYPE_WEATHER_INFO = 1;

    private static final String UNIT_TEMPERATURE = "℃";

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private LayoutInflater mInflater;
    private List<Object> mDataList = new ArrayList<>();

    @ColorInt
    private int mPrimaryTextColor;

    public ForecastAdapter(Context context, List<WeatherForecast> hourForecastList, List<WeatherInfo> dailyWeatherForecastList) {
        mInflater = LayoutInflater.from(context);
        if (hourForecastList != null && !hourForecastList.isEmpty()) {
            mDataList.add("今天");
            mDataList.addAll(hourForecastList);
        }

        if (dailyWeatherForecastList != null && !dailyWeatherForecastList.isEmpty()) {
            mDataList.add(String.format(Locale.CHINA, "未来%d天", dailyWeatherForecastList.size()));
            mDataList.addAll(dailyWeatherForecastList);
        }

        mPrimaryTextColor = context.getResources().getColor(R.color.primaryText);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Object data = getItem(position);
        if (data instanceof String) {
            return VIEW_TYPE_TITLE;
        } else {
            return VIEW_TYPE_WEATHER_INFO;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_TITLE) {
            return getTitleView(position, convertView, parent);
        } else {
            return getWeatherInfoView(position, convertView, parent);
        }
    }

    public View getTitleView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_title, parent, false);
        }

        String title = (String) getItem(position);
        TextView titleTv = (TextView) convertView;
        titleTv.setText(title);

        return convertView;
    }

    public View getWeatherInfoView(int position, View convertView, ViewGroup parent) {
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

        Object data = getItem(position);
        if (data instanceof WeatherForecast) {
            WeatherForecast forecast = (WeatherForecast) data;
            holder.dateTv.setText(forecast.getDate());
            holder.temperatureTv.setText((int)forecast.getTemperature() + UNIT_TEMPERATURE);

            final int percent = forecast.getPop();
            holder.percentTv.setText(percent + "%");
            if (percent > 50) {
                holder.percentTv.setTextColor(Color.RED);
            } else {
                holder.percentTv.setTextColor(mPrimaryTextColor);
            }
        } else {
            WeatherInfo weatherInfo = (WeatherInfo) data;
            try {
                Date date = mDateFormat.parse(weatherInfo.getDate());
                holder.dateTv.setText(DateUtils.formatDateTime(convertView.getContext(), date.getTime(), DateUtils.FORMAT_SHOW_WEEKDAY));
                String temperature = String.format(
                        Locale.CHINA, "%d%s/%d%s",
                        (int)weatherInfo.getMinTemperature(), UNIT_TEMPERATURE,
                        (int)weatherInfo.getMaxTemperature(), UNIT_TEMPERATURE);
                holder.temperatureTv.setText(temperature);

                final int percent = weatherInfo.getPop();
                holder.percentTv.setText(percent + "%");
                if (percent > 50) {
                    holder.percentTv.setTextColor(Color.RED);
                } else {
                    holder.percentTv.setTextColor(mPrimaryTextColor);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView dateTv;
        TextView temperatureTv;
        TextView percentTv;
    }
}
