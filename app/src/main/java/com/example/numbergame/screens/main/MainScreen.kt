package com.example.numbergame.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.screens.GameMenuButton

@Composable
fun MainScreen(navController: NavController) {

    var showFourOpButtons by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🎮 타이틀
            Text(
                text = "🕹️ NUMBER GAME",
                fontSize = 30.sp,
                color = Color.Cyan
            )

            Spacer(modifier = Modifier.height(40.dp))

            GameMenuButton(
                text = "숫자 맞히기 🎯",
                color = Color(0xFF2196F3)
            ) {
                navController.navigate("difficulty/number")
            }

            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton(
                text = "카드 짝 맞추기 🃏",
                color = Color(0xFF4CAF50)
            ) {
                navController.navigate("difficulty/card")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!showFourOpButtons) {
                GameMenuButton(
                    text = "사칙연산 게임 ➕➖✖️➗",
                    color = Color(0xFFFF9800)
                ) {
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
                                onClick = {
                                    navController.navigate("four_basic_operation_difficulty/$op")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp)
                                    .padding(horizontal = 4.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFC107)
                                )
                            ) {
                                Text(op, fontSize = 24.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton(
                text = "반응 속도 테스트 ⏱️",
                color = Color(0xFF9C27B0)
            ) {
                navController.navigate("reaction_test")
            }

            Spacer(modifier = Modifier.height(16.dp))

            GameMenuButton(
                text = "기록 보기 🏆",
                color = Color(0xFF607D8B)
            ) {
                navController.navigate("record")
            }
        }
    }
}