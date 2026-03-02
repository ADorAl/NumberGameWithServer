package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.getRecords
import com.example.numbergame.data.saveRecord

@Composable
fun FourBasicOperationSuccessScreen(
    navController: NavController,
    operation: String,
    difficulty: Int,
    usedTime: Int
) {
    val context = LocalContext.current

    var top10 by remember { mutableStateOf(listOf<Double>()) }
    var newRecords by remember { mutableStateOf(setOf<Double>()) }

    LaunchedEffect(Unit) {
        val oldTop10 = getRecords(context, difficulty)
        saveRecord(context, difficulty, usedTime.toDouble())
        val updatedRecords = getRecords(context, difficulty)
        top10 = updatedRecords

        // 새로 Top10에 들어간 기록만 추출
        newRecords = updatedRecords.filter { it !in oldTop10 }.toSet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎉 성공!", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("연산: $operation", fontSize = 24.sp)
        Text("난이도: $difficulty", fontSize = 24.sp)
        Text(
            "이번 기록: ${usedTime}초 ${if (usedTime.toDouble() in newRecords) "🔥 New!" else ""}",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (top10.isNotEmpty()) {
            Text("Top 10 기록:", fontSize = 20.sp)
            top10.forEachIndexed { index, time ->
                val isNew = time in newRecords
                Text("${index + 1}. ${time.toInt()}초 ${if (isNew) "🔥 New!" else ""}", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("main") },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text("메인으로 돌아가기", fontSize = 20.sp)
        }
    }
}