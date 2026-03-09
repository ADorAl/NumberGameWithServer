package com.example.numbergame.screens.main

import androidx.compose.animation.AnimatedVisibility
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
import com.example.numbergame.data.TokenManager
import com.example.numbergame.screens.GameMenuButton

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    var showFourOpButtons by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(24.dp)
    ) {
        // 🔹 중앙 콘텐츠
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🕹️ 메인 화면",
                fontSize = 32.sp,
                color = Color.Cyan
            )

            Spacer(modifier = Modifier.height(40.dp))

            GameMenuButton("숫자 맞히기 🎯", Color(0xFF2196F3)) {
                navController.navigate("difficulty/number")
            }
            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton("카드 짝 맞추기 🃏", Color(0xFF4CAF50)) {
                navController.navigate("difficulty/card")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (!showFourOpButtons) {
                GameMenuButton("사칙연산 게임 ➕➖✖️➗", Color(0xFFFF9800)) {
                    showFourOpButtons = true
                }
            }

            AnimatedVisibility(visible = showFourOpButtons) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("+", "-", "×", "÷").forEach { op ->
                            Button(
                                onClick = { navController.navigate("four_basic_operation_difficulty/$op") },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp)
                                    .padding(horizontal = 4.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                            ) {
                                Text(op, fontSize = 24.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton("반응 속도 테스트 ⏱️", Color(0xFF9C27B0)) {
                navController.navigate("reaction_test")
            }
            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton("기록 보기 🏆", Color(0xFF607D8B)) {
                navController.navigate("record")
            }
        }

        // 🔹 상단 우측 로그아웃 버튼
        Button(
            onClick = {
                TokenManager.clearToken(context)
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("로그아웃", color = Color.White, fontSize = 16.sp)
        }
    }
}