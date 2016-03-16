package me.zlv.weather;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zlv.weather.bean.WeatherForecast;
import me.zlv.weather.converter.QueryResultConverters;
import me.zlv.weather.httpservice.QueryWeatherService;
import me.zlv.weather.result.QueryWeatherResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<QueryWeatherResult>, View.OnClickListener {

    private FloatingActionButton mFab;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private Call<QueryWeatherResult> mQueryCall;
    private Toolbar mToolbar;
    private ListView mContentLv;

    private ForecastAdapter mAdapter;
    private List<WeatherForecast> mDisplayForecastList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findWidget();
        initWidget();
        setListener();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.heweather.com/")
                .addConverterFactory(new QueryResultConverters())
                .build();

        QueryWeatherService service = retrofit.create(QueryWeatherService.class);
        mQueryCall = service.queryWeather("深圳", "a7856d56f36243869de395482e36b0a0");
        mQueryCall.clone().enqueue(this);
    }

    private void findWidget() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mContentLv = (ListView) findViewById(R.id.content_lv);
    }

    private void initWidget() {
        mToolbar.setTitle("深圳");
    }

    private void setListener() {
        mFab.setOnClickListener(this);
    }

    @Override
    public void onResponse(Call<QueryWeatherResult> call, Response<QueryWeatherResult> response) {
        if (response.isSuccess()) {
            final QueryWeatherResult result = response.body();
            if (result == null) {
                Toast.makeText(this, "查询失败,请稍候重试", Toast.LENGTH_SHORT).show();
                return;
            }

            mToolbar.setTitle(result.getCityInfo().getCity());
            mToolbar.setSubtitle(result.getCityInfo().getUpdateTime());

            // TODO: 16-3-16
//            final WeatherInfo weatherInfo = result.getWeatherInfo();
//            StringBuilder infoBuilder = new StringBuilder();
//            infoBuilder.append("======> 天气状况： ").append("<br>");
//            infoBuilder.append(weatherInfo.getDescription()).append("<br>");
//            infoBuilder.append("温度： ").append(weatherInfo.getTemperature()).append("<br>");
//            infoBuilder.append("湿度： ").append(weatherInfo.getHum()).append("<br>");
//            infoBuilder.append("降雨量： ").append(weatherInfo.getPcpn()).append("<br>");
//            infoBuilder.append("<br>");

            final List<WeatherForecast> forecastList = result.getHourForecastList();
            mDisplayForecastList.clear();
            final long currentTime = System.currentTimeMillis();
            for (WeatherForecast forecast : forecastList) {
                final String dateText = forecast.getDate();
                try {
                    final Date date = mDateFormat.parse(dateText);
                    final long time = date.getTime();
                    if (time > currentTime) {
                        forecast.setDate(dateText.substring(11));
                        mDisplayForecastList.add(forecast);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (mAdapter == null) {
                mAdapter = new ForecastAdapter(this, mDisplayForecastList);
                mContentLv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
        }

        Drawable drawable = mFab.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).stop();
        }
    }

    @Override
    public void onFailure(Call<QueryWeatherResult> call, Throwable t) {
        Toast.makeText(this, "请求出现异常 Execption: ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Drawable drawable = mFab.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
        mQueryCall.clone().enqueue(MainActivity.this);
    }
}
