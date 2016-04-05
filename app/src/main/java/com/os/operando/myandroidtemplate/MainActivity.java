package com.os.operando.myandroidtemplate;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.annotations.SerializedName;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.os.operando.myandroidtemplate.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.button.setEnabled(false);

        Observable.create(subscriber -> subscriber.onNext("test"))
                .map(text -> Log.d("test", text.toString()));

        Observable.from(new String[]{"test", "Android", "hogehoge", "aaa"})
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .filter(text -> text.length() > 4)
                .subscribe(s -> {
                    Log.d(s, s);
                });


//        Observable.from(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
        Observable.range(1, 10)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(integer -> (integer % 2) == 0)
                .map(integer1 -> integer1 * 10)
                .subscribe(integer2 -> Log.d("test", integer2.toString()));

        Observable<Boolean> nameWritten = RxTextView.textChangeEvents(binding.name)
                .map(textViewTextChangeEvent -> !TextUtils.isEmpty(textViewTextChangeEvent.text()));

        Observable<Boolean> passWritten = RxTextView.textChangeEvents(binding.pass)
                .map(textViewTextChangeEvent -> !TextUtils.isEmpty(textViewTextChangeEvent.text()));

        Observable.combineLatest(nameWritten, passWritten, (aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .subscribe(isValid -> binding.button.setEnabled(isValid));

        String id = "2172797";
        String appId = "464b981be2248f383abxxxxxxxxxxx";
        String path = "data/2.5/weather";

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpclient = new OkHttpClient.Builder()
                .addInterceptor(new RequestHeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpclient)
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        retrofit.create(IWhetherApi.class).getWhether(path, id, appId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WetherResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WetherResponse wetherResponse) {
                        Log.d("next", String.valueOf(wetherResponse.main.tempMin));
                    }
                });
    }

    class RequestHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("headerKey", "value");
            return chain.proceed(builder.build());
        }
    }

    interface IWhetherApi {

        @GET("/{fileName}")
        Observable<WetherResponse> getWhether(@Path(value = "fileName", encoded = false) String fileName,
                                              @Query("id") String id,
                                              @Query("APPID") String appId);
    }

    class WetherResponse {
        public Coord coord;
        @SerializedName("weather")
        public List<Weather> weathers;
        public String base;
        public Main main;
        public int visibility;
        public Wind wind;
        @SerializedName("clouds")
        public Cloud cloud;
        public int dt;
        public Sys sys;
        public int id;
        public String name;
        public int cod;

        public class Coord {
            public double lon;
            public double lat;
        }

        public class Weather {
            public int id;
            public String main;
            public String description;
            public String icon;
        }

        public class Main {
            public double temp;
            public int pressure;
            public int humidity;
            @SerializedName("temp_min")
            public double tempMin;
            @SerializedName("temp_max")
            public double tempMax;
        }

        public class Wind {
            public double speed;
            public double deg;
        }

        public class Cloud {
            public int all;
        }

        public class Sys {
            public int type;
            public int id;
            public double message;
            public String country;
            public int sunrise;
            public int sunset;
        }
    }
}