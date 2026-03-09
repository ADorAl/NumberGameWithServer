package com.example.numbergame.screens.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.numbergame.data.getRecords
import com.example.numbergame.data.saveRecord
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun CardSuccessScreen(
    navController: NavController,
    difficulty: Int,
    usedTime: Int
) {

    val context = LocalContext.current
    val records = remember { mutableStateListOf<Double>() }

    val usedTimeDouble = usedTime.toDouble()

    LaunchedEffect(usedTime) {
        saveRecord(context, difficulty + 100, usedTimeDouble)
        records.clear()
        records.addAll(getRecords(context, difficulty + 100))
    }

    val bestScore = records.minOrNull()

    val background = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    var glow by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(700)
            glow = !glow
        }
    }

    val titleColor by animateColorAsState(
        targetValue = if (glow) Color(0xFF00E676) else Color(0xFF69F0AE),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "🎉 STAGE CLEAR!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "이번 게임의 기록 : ${usedTime} 초",
                fontSize = 22.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            bestScore?.let {
                Text(
                    text = "🏆 BEST : ${String.format("%.0f", it)} sec",
                    fontSize = 20.sp,
                    color = Color.Yellow
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TOP 10 RANKING",
                fontSize = 18.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            records.forEachIndexed { index, time ->

                val isNew = abs(time - usedTimeDouble) < 0.001

                Text(
                    text = "${index + 1}. ${
                        String.format("%.0f", time)
                    } sec ${if (isNew) "✨ NEW!" else ""}",
                    fontSize = 18.sp,
                    color = if (isNew) Color(0xFF00E5FF) else Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔥 다시 도전 버튼
            Button(
                onClick = {
                    navController.navigate("game/card/$difficulty") {
                        popUpTo("game/card/$difficulty") { inclusive = true }
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

            Button(
                onClick = {
                    navController.navigate("difficulty/card")
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF)
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
            ) {
                Text("🎯 레벨 선택", fontSize = 18.sp)
            }

            if (difficulty < 4) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("game/card/${difficulty + 1}")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp)
                ) {
                    Text("🚀 다음 단계로", fontSize = 18.sp)
                }
            }
        }
    }
}