package ru.gb.util;

import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.*;

@UtilityClass
public class RetrofitUtils {

    LoggingInterceptor loggingMy = new LoggingInterceptor();
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new PrettyLogger());
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public Retrofit getRetrofit(){

        logging.setLevel(BODY);
        httpClient.addInterceptor(loggingMy);

        return new Retrofit.Builder()
                .baseUrl(ConfigUtils.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}
