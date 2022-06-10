package com.example.chatme.ui.api;


import com.example.chatme.pojo.notification.RootModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyApiInterface {
    //Send Notification
    @Headers({"Authorization: key=" + "AAAANMpTsdE:APA91bHm4lezW5a25VsPdxgberxNuPwOEQwUbWdudZMmlYZf0Z9D3uvaQ4a0RWQ4Z_zwSfKwSZAzCwjSjZ3wgu6R6Liyj8tQiezp36s3p2ZrOSi-oMoKdFUMYmxzj_RjfVciF7l15nqL", "Content-Type:application/json"})
    @POST("fcm/send")
    Observable<ResponseBody> sendNotification(@Body RootModel root);
}
