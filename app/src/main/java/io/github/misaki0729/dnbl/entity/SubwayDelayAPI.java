package io.github.misaki0729.dnbl.entity;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

public interface SubwayDelayAPI {
    @GET("/free/delay.json")
    Observable<Response<ResponseBody>> getSubwayDelayInfo();
}
