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
    navController: NavController,   // 화면 이동을 위한 네비게이션 컨트롤러
    difficulty: Int                 // 현재 게임 난이도 (재시작 시 다시 전달)
) {

    // 화면 배경에 사용할 그라데이션 색상 정의
    val background = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    // 텍스트 깜빡임 상태 관리
    var blink by remember { mutableStateOf(true) }

    // Composable이 처음 실행될 때 무한 루프 코루틴 실행
    // 600ms마다 blink 값을 반전시켜 깜빡임 효과 생성
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(600)
            blink = !blink
        }
    }

    // blink 값에 따라 텍스트 색상을 애니메이션으로 변경
    val titleColor by animateColorAsState(
        targetValue = if (blink) Color(0xFFFF1744) else Color(0xFFFF5252),
        label = ""
    )

    // 전체 화면 컨테이너
    Box(
        modifier = Modifier
            .fillMaxSize()       // 화면 전체 채우기
            .background(background), // 위에서 정의한 그라데이션 배경 적용
        contentAlignment = Alignment.Center
    ) {

        // 세로 방향으로 UI 배치
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 실패 메시지 텍스트 (색상 깜빡임 적용)
            Text(
                text = "⏰ 시간 초과 ⏰",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 서브 텍스트
            Text(
                text = "MISSION FAILED",
                fontSize = 18.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🔁 다시 도전 버튼
            Button(
                onClick = {

                    // 현재 난이도로 카드 게임 화면 다시 시작
                    navController.navigate("card_game/$difficulty") {

                        // 동일한 화면 스택을 제거하고 새로 생성
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

            // 🎯 난이도 선택 화면으로 이동 버튼
            Button(
                onClick = {

                    // 난이도 선택 화면으로 이동
                    navController.navigate("difficulty/card") {

                        // 메인 화면까지 스택 정리
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