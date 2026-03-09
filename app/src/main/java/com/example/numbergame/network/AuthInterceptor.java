package com.example.numbergame.network;

import android.content.Context;
import com.example.numbergame.data.TokenManager;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();

        String token = TokenManager.getToken(context);

        if (token != null) {
            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();

            return chain.proceed(request);
        }

        return chain.proceed(original);
    }
}