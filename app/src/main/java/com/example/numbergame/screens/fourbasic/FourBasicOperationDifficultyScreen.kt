package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.screens.GameMenuButton

/**
 * 사칙연산 게임 난이도 선택 화면
 *
 * @param navController 화면 이동을 위한 네비게이션 컨트롤러
 * @param operation 사용자가 선택한 연산 종류 (+, -, ×, ÷ 등)
 */
@Composable
fun FourBasicOperationDifficultyScreen(
    navController: NavController,
    operation: String
) {

    // 🔹 전체 화면을 차지하는 Box (배경 + UI 배치용 컨테이너)
    Box(
        modifier = Modifier
            .fillMaxSize()                 // 화면 전체 크기 사용
            .background(Color(0xFF101820)) // 어두운 배경 색
            .padding(24.dp)                // 전체 여백
    ) {

        // 🔹 뒤로가기 버튼 (왼쪽 상단 위치)
        Button(
            onClick = { navController.popBackStack() }, // 이전 화면으로 이동
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF263238) // 버튼 배경 색
            ),
            shape = RoundedCornerShape(12.dp), // 버튼 모서리 둥글게
            modifier = Modifier.align(Alignment.TopStart) // 좌측 상단 정렬
        ) {
            Text("⬅ 뒤로가기", color = Color.White)
        }

        // 🔹 중앙에 배치되는 메인 UI 영역
        Column(
            modifier = Modifier.align(Alignment.Center), // 화면 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 현재 선택된 연산 종류 표시
            Text(
                text = "⚔️ $operation 연산 선택",
                fontSize = 28.sp,
                color = Color.Yellow
            )

            // 🔹 제목과 버튼 사이 간격
            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 난이도 목록 생성 (STAGE 1 ~ 3)
            listOf(1, 2, 3).forEach { difficulty ->

                // 🔹 난이도 선택 버튼
                GameMenuButton(
                    text = "STAGE $difficulty",

                    // 난이도에 따라 버튼 색상 변경
                    color = when (difficulty) {
                        1 -> Color(0xFF4CAF50) // 쉬움 (초록)
                        2 -> Color(0xFFFF9800) // 보통 (주황)
                        else -> Color(0xFFF44336) // 어려움 (빨강)
                    }
                ) {

                    // 🔹 선택한 연산 + 난이도를 다음 게임 화면으로 전달
                    navController.navigate(
                        "four_basic_operation/$operation/$difficulty"
                    )
                }

                // 🔹 버튼 사이 간격
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}