package com.example.numbergame.screens.reaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ReactionTestScreen(navController: NavController) {

    val gridSize = 5
    var playerPos by remember { mutableStateOf(Pair(2, 2)) } // 중앙 시작

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("반응 속도 테스트", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 맵
        Column {
            for (y in 0 until gridSize) {
                Row {
                    for (x in 0 until gridSize) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .border(1.dp, Color.Black)
                                .background(if (playerPos.first == x && playerPos.second == y) Color.Blue else Color.White)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 조작판
// 🔹 원형 방향키
        Box(
            modifier = Modifier
                .size(200.dp) // 전체 컨트롤 영역
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // 위 버튼
            Button(
                onClick = { if (playerPos.second > 0) playerPos = playerPos.first to (playerPos.second - 1) },
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Text("▲")
            }

            // 아래 버튼
            Button(
                onClick = { if (playerPos.second < gridSize - 1) playerPos = playerPos.first to (playerPos.second + 1) },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text("▼")
            }

            // 왼쪽 버튼
            Button(
                onClick = { if (playerPos.first > 0) playerPos = (playerPos.first - 1) to playerPos.second },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text("◀")
            }

            // 오른쪽 버튼
            Button(
                onClick = { if (playerPos.first < gridSize - 1) playerPos = (playerPos.first + 1) to playerPos.second },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text("▶")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate("main") }) {
            Text("메인으로")
        }
    }
}