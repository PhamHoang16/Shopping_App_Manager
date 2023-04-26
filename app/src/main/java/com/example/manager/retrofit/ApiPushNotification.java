package com.example.manager.retrofit;

import com.example.manager.model.NotiResponse;
import com.example.manager.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                    "content-type:\n" +
                            "application/json",
                    "authorization:\n" +
                            "key=AAAAR2GtI40:APA91bH_pQF4-es3bZUEjp_UMdsd8eo9wYPLfXfBN7VANIoHiWifFw6WZBVYvCQGUWkMELYD5rnwszji_hr0-wY41IJGe-bZPN_-roXtZAIytDKodjqemg_ELk3f5F09EsvLu_uNjman"
            }
    )

    @POST("fcm/send")
    Observable<NotiResponse> sendNotification(@Body NotiSendData data);
}
