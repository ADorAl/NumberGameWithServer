package com.example.numbergame.screens.number

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FailScreen(navController: NavController, difficulty: Int) {

    val backgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF1B0000),
            Color(0xFF3E0000),
            Color(0xFF000000)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 💀 GAME OVER
            Text(
                text = "💀 GAME OVER",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFF1744)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "난이도 $difficulty 도전 실패",
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🔥 다시 시도 버튼 (강조)
            Button(
                onClick = {
                    navController.navigate("game/number/$difficulty")
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF1744)
                )
            ) {
                Text(
                    text = "🔥 다시 도전",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🎯 난이도 선택으로
            OutlinedButton(
                onClick = {
                    navController.navigate("difficulty/number")
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "난이도 선택으로",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}