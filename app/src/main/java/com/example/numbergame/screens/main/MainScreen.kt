package com.example.numbergame.screens.main

import androidx.compose.animation.AnimatedVisibility
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

@Composable
fun MainScreen(navController: NavController) {

    var showFourOpButtons by remember { mutableStateOf(false) }
    var selectedOperation by remember { mutableStateOf<String?>(null) }

    // 배경 클릭 시 버튼 원상복귀
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = showFourOpButtons) { showFourOpButtons = false }
            .padding(24.dp)
    ) {
        if (selectedOperation != null) {
            Text("선택한 연산: $selectedOperation", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(onClick = { navController.navigate("record") }) {
                Text("기록 보기")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 숫자 맞히기 버튼
            Button(
                onClick = { navController.navigate("difficulty/number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("숫자 맞히기", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("🎯", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 카드 짝 맞추기 버튼
            Button(
                onClick = { navController.navigate("difficulty/card") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("카드 짝 맞추기", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("🃏", fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 사칙연산 버튼
            if (!showFourOpButtons) {
                Button(
                    onClick = { showFourOpButtons = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("사칙연산 게임", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("➕➖✖️➗", fontSize = 24.sp)
                    }
                }
            }

            // 🔹 연산 선택 버튼
            AnimatedVisibility(visible = showFourOpButtons) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("+" to "더하기", "-" to "빼기", "×" to "곱하기", "÷" to "나누기")
                        .forEach { (opSymbol, _) ->
                            Button(
                                onClick = {
                                    selectedOperation = opSymbol
                                    navController.navigate("four_basic_operation_difficulty/$opSymbol")
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                            ) {
                                Text(opSymbol, fontSize = 24.sp)
                            }
                        }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 반응 속도 테스트 버튼
            Button(
                onClick = { navController.navigate("reaction_test") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("반응 속도 테스트", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("⏱️", fontSize = 24.sp)
                }
            }
        }
    }
}