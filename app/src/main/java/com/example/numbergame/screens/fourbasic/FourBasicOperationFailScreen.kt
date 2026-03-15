// 패키지 위치 (프로젝트 구조에서 이 파일이 속한 폴더)
package com.example.numbergame.screens.fourbasic

// Compose UI를 만들기 위한 기본 라이브러리 import
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

// Material3 UI 컴포넌트 (Button, Text 등)
import androidx.compose.material3.*

import androidx.compose.runtime.Composable

// UI 정렬 관련
import androidx.compose.ui.Alignment

// Modifier (크기, 배치, 스타일 지정)
import androidx.compose.ui.Modifier

// 색상 관련
import androidx.compose.ui.graphics.Color

// 단위 관련 (dp, sp)
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 화면 이동을 위한 Navigation 컨트롤러
import androidx.navigation.NavController


// Composable 함수 (Compose UI 화면 하나를 의미)
@Composable
fun FourBasicOperationFailScreen(

    // 화면 이동을 위해 NavController 전달
    navController: NavController,

    // 현재 플레이한 연산 종류 (예: +, -, ×, ÷)
    operation: String,

    // 현재 난이도
    difficulty: Int
) {

    // 전체 화면을 차지하는 Box (레이아웃 컨테이너)
    Box(
        modifier = Modifier
            .fillMaxSize() // 화면 전체 크기 사용
            .background(Color(0xFF1A0000)) // 어두운 붉은 배경색
            .padding(24.dp) // 전체 패딩
    ) {

        // UI 요소들을 세로로 배치하는 Column
        Column(

            // Box 중앙에 배치
            modifier = Modifier.align(Alignment.Center),

            // Column 내부 요소들을 가로 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // GAME OVER 텍스트
            Text(
                "💀 GAME OVER",
                fontSize = 32.sp,
                color = Color.Red
            )

            // 위젯 사이 여백
            Spacer(modifier = Modifier.height(20.dp))

            // 현재 연산 표시
            Text(
                "연산: $operation",
                fontSize = 22.sp,
                color = Color.White
            )

            // 현재 난이도 표시
            Text(
                "난이도: $difficulty",
                fontSize = 22.sp,
                color = Color.White
            )

            // 버튼 위 여백
            Spacer(modifier = Modifier.height(40.dp))

            // 다시 도전 버튼
            Button(

                // 버튼 클릭 시 게임 화면으로 다시 이동
                onClick = {
                    navController.navigate(
                        "four_basic_operation/$operation/$difficulty"
                    )
                },

                modifier = Modifier
                    .fillMaxWidth() // 버튼 가로 전체
                    .height(60.dp), // 버튼 높이

                // 버튼 모서리 둥글게
                shape = RoundedCornerShape(16.dp),

                // 버튼 색상 설정
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336) // 빨간색
                )
            ) {

                // 버튼 텍스트
                Text(
                    "🔁 다시 도전",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            // 버튼 사이 여백
            Spacer(modifier = Modifier.height(16.dp))

            // 메인 메뉴 이동 버튼
            Button(

                // 클릭 시 메인 화면으로 이동
                onClick = { navController.navigate("main") },

                modifier = Modifier
                    .fillMaxWidth() // 가로 전체
                    .height(60.dp), // 버튼 높이

                shape = RoundedCornerShape(16.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray // 회색 버튼
                )
            ) {

                // 버튼 텍스트
                Text(
                    "🏠 메인으로",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}