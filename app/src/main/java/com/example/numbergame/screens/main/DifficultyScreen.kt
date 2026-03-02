package com.example.numbergame.screens.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DifficultyScreen(navController: NavController, gameType: String) {

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔙 뒤로가기
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("main") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "뒤로가기",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 🎮 제목
            Text(
                text = if (gameType == "hint") "💡 힌트 모드" else "🎮 일반 모드",
                fontSize = 22.sp,
                color = Color.Cyan,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "난이도를 선택하세요",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ⭐ 난이도 버튼들
            for (i in 1..4) {
                GameDifficultyButton(
                    difficulty = i,
                    onClick = {
                        navController.navigate("game/$gameType/$i")
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun GameDifficultyButton(
    difficulty: Int,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 1.1f else 1f,
        label = ""
    )

    val difficultyColor = when (difficulty) {
        1 -> Color(0xFF00E676) // 초록
        2 -> Color(0xFF00B0FF) // 파랑
        3 -> Color(0xFFFFA000) // 주황
        else -> Color(0xFFFF1744) // 빨강
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .scale(scale)
            .clickable {
                pressed = true
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = difficultyColor.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

// ⭐ 별 표시
            Text(
                text = "⭐".repeat(difficulty),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "난이도 $difficulty",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = when (difficulty) {
                    1 -> "초보자 추천"
                    2 -> "일반인"
                    3 -> "집중력 필요"
                    else -> "🔥 극한 도전"
                },
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}