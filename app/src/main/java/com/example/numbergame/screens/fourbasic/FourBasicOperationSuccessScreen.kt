package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.flow.first

@Composable
fun FourBasicOperationSuccessScreen(
    navController: NavController,
    operation: String,
    difficulty: Int,
    usedTime: Int
) {
    val context = LocalContext.current

    var top10 by remember { mutableStateOf(listOf<Int>()) }
    var newRecords by remember { mutableStateOf(setOf<Int>()) }

    LaunchedEffect(Unit) {
        val oldTop10 = mutableListOf<Int>()

        for (i in 1..10) {
            val record = RecordManager
                .getRecord(context, "four_basic", i, operation)
                .first()
            if (record != null) oldTop10.add(record)
        }

        RecordManager.saveRecord(
            context,
            "four_basic",
            difficulty,
            usedTime,
            operation
        )

        val updatedTop10 = mutableListOf<Int>()

        for (i in 1..10) {
            val record = RecordManager
                .getRecord(context, "four_basic", i, operation)
                .first()
            if (record != null) updatedTop10.add(record)
        }

        top10 = updatedTop10.sorted()
        newRecords = top10.filter { it !in oldTop10 }.toSet()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1F3B))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("🎉 STAGE CLEAR!", fontSize = 32.sp, color = Color.Yellow)

            Spacer(modifier = Modifier.height(20.dp))

            Text("연산: $operation", fontSize = 22.sp, color = Color.White)
            Text("난이도: $difficulty", fontSize = 22.sp, color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "⏱ ${usedTime}초 ${if (usedTime in newRecords) "🔥 NEW RECORD!" else ""}",
                fontSize = 24.sp,
                color = if (usedTime in newRecords) Color.Cyan else Color.White
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (top10.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2A2F5A)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Text("🏆 TOP 10", fontSize = 20.sp, color = Color.Yellow)

                        Spacer(modifier = Modifier.height(12.dp))

                        top10.forEachIndexed { index, time ->
                            val isNew = time in newRecords
                            Text(
                                "${index + 1}. ${time}초 ${if (isNew) "🔥" else ""}",
                                fontSize = 18.sp,
                                color = if (isNew) Color.Cyan else Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navController.navigate("main") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("🏠 메인으로", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}