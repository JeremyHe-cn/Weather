package me.zlv.weather;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.zlv.weather.bean.WeatherForecast;
import me.zlv.weather.bean.WeatherInfo;
import me.zlv.weather.converter.QueryResultConverters;
import me.zlv.weather.httpservice.QueryWeatherService;
import me.zlv.weather.result.QueryWeatherResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<QueryWeatherResult> {

    private FloatingActionButton mFab;
    private TextView mContentTv;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private Call<QueryWeatherResult> mQueryCall;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mContentTv = (TextView) findViewById(R.id.content_tv);
    }

    private void initWidget() {
        mContentTv.setText("正在请求天气数据...");
    }

    private void setListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContentTv.setText("正在请求天气数据...");
                Drawable drawable = mFab.getDrawable();
                if (drawable instanceof AnimationDrawable) {
                    ((AnimationDrawable) drawable).start();
                }
                mQueryCall.clone().enqueue(MainActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<QueryWeatherResult> call, Response<QueryWeatherResult> response) {
        if (response.isSuccess()) {
            final QueryWeatherResult result = response.body();
            if (result == null) {
                mContentTv.setText("查询失败,请稍候重试");
                return;
            }

            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append(result.getCityInfo().getCity()).append("<br>");
            infoBuilder.append("最后更新时间： ").append(result.getCityInfo().getUpdateTime()).append("<br>");
            infoBuilder.append("<br>");

            final WeatherInfo weatherInfo = result.getWeatherInfo();
            infoBuilder.append("======> 天气状况： ").append("<br>");
            infoBuilder.append(weatherInfo.getDescription()).append("<br>");
            infoBuilder.append("温度： ").append(weatherInfo.getTemperature()).append("<br>");
            infoBuilder.append("湿度： ").append(weatherInfo.getHum()).append("<br>");
            infoBuilder.append("降雨量： ").append(weatherInfo.getPcpn()).append("<br>");
            infoBuilder.append("<br>");

            final List<WeatherForecast> forecastList = result.getHourForecastList();
            if (!forecastList.isEmpty()) {
                infoBuilder.append("======> 当天天气预测<br>");
            }

            final long currentTime = System.currentTimeMillis();
            for (WeatherForecast forecast : forecastList) {
                final String date = forecast.getDate();
                try {
                    final long time = mDateFormat.parse(date).getTime();
                    if (time < currentTime) {
                        continue;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                infoBuilder.append(date).append("<br>");
                infoBuilder.append("温度: ").append(forecast.getTemperature()).append("<br>");
                infoBuilder.append("降雨可能性： ");
                final int percent = forecast.getPop();
                if (percent > 50) {
                    infoBuilder.append("<font color=red>");
                }
                infoBuilder.append(percent).append("%");
                if (percent > 50) {
                    infoBuilder.append("  WARN!!</font>");
                }
                infoBuilder.append("<br><br>");
            }

            mContentTv.setText(Html.fromHtml(infoBuilder.toString()));
        } else {
            mContentTv.setText("请求失败");
        }

        Drawable drawable = mFab.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).stop();
        }
    }

    @Override
    public void onFailure(Call<QueryWeatherResult> call, Throwable t) {
        mContentTv.setText("请求出现异常 Execption: " + t.toString());
    }
}
