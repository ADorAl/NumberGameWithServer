package com.example.numbergame.screens.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController

@Composable
fun CardFailScreen(
    navController: NavController,
    difficulty: Int
) {

    val background = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    var blink by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(600)
            blink = !blink
        }
    }

    val titleColor by animateColorAsState(
        targetValue = if (blink) Color(0xFFFF1744) else Color(0xFFFF5252),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "⏰ 시간 초과 ⏰",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "MISSION FAILED",
                fontSize = 18.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🔥 다시 도전 버튼
            Button(
                onClick = {
                    navController.navigate("card_game/$difficulty") {
                        popUpTo("card_game/$difficulty") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF)
                ),
                elevation = ButtonDefaults.buttonElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
            ) {
                Text("🔁 재시작", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🎯 난이도 선택 버튼
            Button(
                onClick = {
                    navController.navigate("difficulty/card") {
                        popUpTo("main")
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE)
                ),
                elevation = ButtonDefaults.buttonElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
            ) {
                Text("🎯 난이도 메뉴로", fontSize = 18.sp)
            }
        }
    }
}