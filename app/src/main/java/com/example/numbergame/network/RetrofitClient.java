package com.example.numbergame.network;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // 🔹 ngrok 외부 URL
    private static final String BASE_URL = "https://rancid-chet-overreadily.ngrok-free.dev/";

    private static Retrofit retrofit = null;

    public static UserApi getUserApi(Context context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context))
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(UserApi.class);
    }
}