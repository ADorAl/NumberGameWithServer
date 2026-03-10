package com.example.numbergame.screens.number

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.getRecords
import com.example.numbergame.data.saveRecord
import kotlin.math.abs

@Composable
fun NumberSuccessScreen(
    navController: NavController,
    difficulty: Int,
    elapsedTime: Double?
) {

    val context = LocalContext.current
    val records = remember { mutableStateListOf<Double>() }

    LaunchedEffect(elapsedTime) {
        if (elapsedTime != null) {
            saveRecord(context, difficulty, elapsedTime)
            records.clear()
            records.addAll(getRecords(context, difficulty))
        }
    }

    val bestScore = records.minOrNull()

    val backgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
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

            // 🎉 클리어 타이틀
            Text(
                text = "🏆 CLEAR!",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 💎 이번 기록 카드
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF00E5FF)
                ),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Text(
                    text = "이번 기록\n${
                        elapsedTime?.let { String.format("%.3f", it) } ?: "-"
                    } 초",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ⭐ BEST
            bestScore?.let {
                Text(
                    text = "⭐ BEST : ${String.format("%.3f", it)} 초",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E676)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "🏅 TOP 10",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            records.take(10).forEachIndexed { index, time ->

                val isNew = elapsedTime?.let {
                    abs(it - time) < 0.001
                } ?: false

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${index + 1}등",
                        color = Color.White
                    )

                    if (isNew) {
                        Text(
                            text = "🔥 NEW!",
                            color = Color.Yellow,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${String.format("%.3f", time)}초",
                        color = if (isNew) Color.Yellow else Color.White,
                        fontWeight = if (isNew) FontWeight.Bold else FontWeight.Normal
                    )


                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔥 다시 도전 버튼
            Button(
                onClick = {
                    navController.navigate("game/number/$difficulty") {
                        popUpTo("game/number/$difficulty") {
                            inclusive = true
                        }
                        launchSingleTop = true
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


            // 🎮 버튼들
            GameMenuButton("난이도 선택") {
                navController.navigate("difficulty/number")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (difficulty < 4) {
                GameMenuButton("다음 난이도 ▶") {
                    navController.navigate("game/number/${difficulty + 1}")
                }
            }
        }
    }
}

@Composable
fun GameMenuButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(55.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6200EE)
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}