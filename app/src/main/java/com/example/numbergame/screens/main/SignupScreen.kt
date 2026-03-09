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

import com.example.numbergame.network.RetrofitClient
import com.example.numbergame.dto.SignupRequest
import com.example.numbergame.dto.UserResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SignupScreen(navController: NavController) {

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
            text = "회원가입",
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

                val request = SignupRequest(id, password)

                userApi.signup(request).enqueue(object : Callback<UserResponse> {

                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                        if (response.isSuccessful) {

                            Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            navController.navigate("login")

                        } else {

                            Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show()

                        }

                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                        Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()

                    }

                })

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("회원가입")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text("로그인으로 돌아가기")
        }

    }
}