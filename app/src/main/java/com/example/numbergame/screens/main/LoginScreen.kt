package com.example.numbergame.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.numbergame.data.TokenManager

import com.example.numbergame.dto.LoginRequest
import com.example.numbergame.dto.JwtResponse
import com.example.numbergame.network.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val userApi = RetrofitClient.getUserApi(context)

    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Number Game",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                val request = LoginRequest(id, password)

                userApi.login(request).enqueue(object : Callback<JwtResponse> {

                    override fun onResponse(call: Call<JwtResponse>, response: Response<JwtResponse>) {

                        if (response.isSuccessful) {

                            val token = response.body()?.token

                            if (token != null) {

                                TokenManager.saveToken(context, token)

                                Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()

                                navController.navigate("main")
                            }

                        } else {

                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()

                        }

                    }

                    override fun onFailure(call: Call<JwtResponse>, t: Throwable) {

                        Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()

                    }

                })

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("로그인")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.navigate("signup")
        }) {
            Text("회원가입")
        }

    }
}