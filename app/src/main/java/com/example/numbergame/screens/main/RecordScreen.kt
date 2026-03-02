package com.example.numbergame.screens.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.launch

@Composable
fun RecordScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf("number") }
    var selectedOperation by remember { mutableStateOf<String?>(null) }

    val maxDifficulty = when (selectedTab) {
        "number" -> 4
        "card" -> 4
        "four_basic" -> 3
        else -> 1
    }

    val difficulties = (1..maxDifficulty).toList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F111A),
                        Color(0xFF1A1F2E)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "🏆 RECORD ROOM",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Cyan
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🎮 게임 탭 버튼
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf(
                    "number" to "숫자",
                    "card" to "카드",
                    "four_basic" to "사칙연산",
                    "reaction" to "반응속도"
                ).forEach { (type, label) ->

                    val selected = selectedTab == type

                    Button(
                        onClick = {
                            selectedTab = type
                            selectedOperation = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (selected) Color(0xFF00E5FF)
                                else Color(0xFF263238)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            label,
                            color = if (selected) Color.Black else Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ➕ 사칙연산 하위 버튼
            if (selectedTab == "four_basic") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("+", "-", "×", "÷").forEach { op ->
                        val selected = selectedOperation == op

                        Button(
                            onClick = { selectedOperation = op },
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (selected) Color(0xFF00E676)
                                    else Color(0xFF37474F)
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                op,
                                fontSize = 20.sp,
                                color = if (selected) Color.Black else Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 🏆 기록 카드들
            if (selectedTab == "reaction") {

                val recordFlow = RecordManager.getRecord(context, "reaction", 0)
                val record by recordFlow.collectAsState(initial = null)

                GameRecordCard(
                    title = "⚡ 반응 속도",
                    record = record
                )

            } else {

                difficulties.forEach { difficulty ->

                    val recordFlow =
                        if (selectedTab == "four_basic" && selectedOperation != null) {
                            RecordManager.getRecord(
                                context,
                                selectedTab,
                                difficulty,
                                selectedOperation
                            )
                        } else {
                            RecordManager.getRecord(
                                context,
                                selectedTab,
                                difficulty
                            )
                        }

                    val record by recordFlow.collectAsState(initial = null)

                    GameRecordCard(
                        title = "⭐ 난이도 $difficulty",
                        record = record
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { scope.launch { RecordManager.resetAll(context) } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD50000)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("🗑 기록 초기화")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("🏠 메인 화면")
            }
        }
    }
}

@Composable
fun GameRecordCard(
    title: String,
    record: Int?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF263238).copy(alpha = 0.7f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                title,
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                record?.let { "🔥 BEST : ${it}초" } ?: "기록 없음",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (record != null)
                    Color(0xFF00E676)
                else
                    Color.Gray
            )
        }
    }
}