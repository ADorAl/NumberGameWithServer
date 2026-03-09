package com.example.numbergame.network;

import com.example.numbergame.dto.LoginRequest;
import com.example.numbergame.dto.SignupRequest;
import com.example.numbergame.dto.UserResponse;
import com.example.numbergame.dto.JwtResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("/api/users/signup")
    Call<UserResponse> signup(@Body SignupRequest request);

    @POST("/api/users/login")
    Call<JwtResponse> login(@Body LoginRequest request);
}