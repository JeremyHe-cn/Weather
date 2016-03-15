package me.zlv.weather.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import me.zlv.weather.result.QueryWeatherResult;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 查询结果转换工具
 * Created by jeremyhe on 16-3-12.
 */
public class QueryResultConverters extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == QueryWeatherResult.class) {
            return new QueryWeatherResultConverter();
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }
}
