package com.example.numbergame.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.dto.SignupRequest
import com.example.numbergame.dto.UserResponse
import com.example.numbergame.network.RetrofitClient

@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Signup",
                fontSize = 32.sp,
                color = Color.Cyan
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.Cyan,
                    focusedBorderColor = Color.Cyan,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Cyan,
                    unfocusedLabelColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.Cyan,
                    focusedBorderColor = Color.Cyan,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Cyan,
                    unfocusedLabelColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val signupRequest = SignupRequest(id, password)
                    RetrofitClient.getUserApi(context).signup(signupRequest)
                        .enqueue(object : retrofit2.Callback<UserResponse> {
                            override fun onResponse(
                                call: retrofit2.Call<UserResponse>,
                                response: retrofit2.Response<UserResponse>
                            ) {
                                if (response.isSuccessful) {
                                    successMessage = "Signup successful! Please login."
                                    navController.navigate("login") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                } else errorMessage = "Signup failed: ${response.code()}"
                            }

                            override fun onFailure(call: retrofit2.Call<UserResponse>, t: Throwable) {
                                errorMessage = "Signup failed: ${t.message}"
                            }
                        })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Signup", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Go to Login", color = Color.Cyan)
            }

            successMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = Color(0xFF00E676))
            }
            errorMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = Color(0xFFD32F2F))
            }
        }
    }
}