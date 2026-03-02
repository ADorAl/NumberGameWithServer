package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FourBasicOperationFailScreen(
    navController: NavController,
    operation: String,
    difficulty: Int
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A0000))
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("💀 GAME OVER", fontSize = 32.sp, color = Color.Red)

            Spacer(modifier = Modifier.height(20.dp))

            Text("연산: $operation", fontSize = 22.sp, color = Color.White)
            Text("난이도: $difficulty", fontSize = 22.sp, color = Color.White)

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    navController.navigate(
                        "four_basic_operation/$operation/$difficulty"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                )
            ) {
                Text("🔁 다시 도전", fontSize = 20.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("main") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("🏠 메인으로", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}