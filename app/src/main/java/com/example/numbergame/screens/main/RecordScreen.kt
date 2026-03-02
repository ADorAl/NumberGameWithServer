package com.example.numbergame.screens.record

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.launch

@Composable
fun RecordScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf("number") } // 기본 탭: 숫자 맞히기

    // 게임별 최대 난이도 정의 (동적 대응 가능)
    val maxDifficulty = when (selectedTab) {
        "number" -> 4   // 숫자 게임 난이도 1~4
        "card" -> 4     // 카드 게임 난이도 1~4
        "four_basic" -> 3 // 사칙 연산 게임 난이도 1~3

        else -> 2       // 다른 게임 기본값
    }
    val difficulties = (1..maxDifficulty).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("🏆 최고 기록", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 탭 버튼
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("number" to "숫자 맞히기", "card" to "카드 맞히기", "four_basic" to "사칙연산").forEach { (type, label) ->
                Button(
                    onClick = { selectedTab = type },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == type)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(label)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 난이도별 기록 표시
        difficulties.forEach { difficulty ->
            val recordFlow = RecordManager.getRecord(context, selectedTab, difficulty)
            val record by recordFlow.collectAsState(initial = null)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("난이도 $difficulty")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(record?.let { "최고 기록: ${it}초" } ?: "기록 없음")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 초기화 버튼
        Button(onClick = {
            scope.launch {
                RecordManager.resetAll(context)
            }
        }) {
            Text("기록 초기화")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 메인 화면 버튼
        Button(onClick = {
            navController.navigate("main") { popUpTo("main") { inclusive = true } }
        }) {
            Text("메인 화면")
        }
    }
}