package io.github.misaki0729.dnbl.network;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import io.github.misaki0729.dnbl.entity.SubwayDelayAPI;
import io.github.misaki0729.dnbl.entity.SubwayDelayInfo;
import io.github.misaki0729.dnbl.entity.db.Subway;
import io.github.misaki0729.dnbl.util.db.SubwayTableUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SubwayDelay {
    private SubwayDelayAPI api;

    private HttpLoggingInterceptor logging;

    public SubwayDelay() {
        logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = getHttpClient(logging);

        api = getApi(client);
    }

    private OkHttpClient getHttpClient(Interceptor interceptor) {
        if (interceptor == null) return new OkHttpClient.Builder().build();

        return new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(logging).build();
    }

    private SubwayDelayAPI getApi(OkHttpClient client) {
        String baseUrl = "https://tetsudo.rti-giken.jp/";

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build()
                .create(SubwayDelayAPI.class);
    }

    public Observable<List<SubwayDelayInfo.Info>> setDelayInfo() {
        Observable<Response<ResponseBody>> info = api.getSubwayDelayInfo();

        return info.map(response -> {
            if (!isSuccessConnection(response)) {
                return null;
            }

            return response.body();
        }).map(delayInfo -> {
            List<SubwayDelayInfo.Info> infos = new ArrayList<>();
            try {
                infos = new ObjectMapper().readValue(delayInfo.string(), new TypeReference<List<SubwayDelayInfo.Info>>() {});

                SubwayTableUtil util = new SubwayTableUtil();

                for (SubwayDelayInfo.Info in: infos) {
                    Log.d("debug", in.name);

                    Subway subway = util.getRecord(in.name);
                    if (subway != null) {
                        subway.delay_time = 1;
                        util.updateRecord(subway);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return infos;
        }).doOnCompleted(() -> {

        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
    }

    protected static boolean isSuccessConnection(Response<?> response) {
        return !(response.code() >= 400);
    }
}
