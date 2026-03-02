package com.example.numbergame.screens.number

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NumberGameScreen(navController: NavController, difficulty: Int) {

    val gridSize = difficulty + 2
    val totalCount = gridSize * gridSize

    var currentNumber by remember { mutableStateOf(1) }
    var numbers by remember { mutableStateOf((1..totalCount).shuffled()) }
    var missCount by remember { mutableStateOf(0) }
    val maxLives = 3
    var wrongIndex by remember { mutableStateOf<Int?>(null) }

    val startTime = remember { System.currentTimeMillis() }
    var elapsedSeconds by remember { mutableStateOf(0.0) }

    val scope = rememberCoroutineScope()

    // ⏱ 타이머
    LaunchedEffect(Unit) {
        while (true) {
            elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0
            delay(10)
        }
    }

    val backgroundGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // 🎯 현재 숫자 HUD
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF00E5FF)),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Text(
                    text = "NEXT : $currentNumber",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ⏱ 시간 + ❤️ 목숨
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TIME ${String.format("%.2f", elapsedSeconds)}",
                    fontSize = 18.sp,
                    color = Color.White
                )

                Row {
                    repeat(maxLives) { index ->
                        val heartColor =
                            if (index < maxLives - missCount) Color.Red else Color.Gray

                        Text(
                            text = "❤️",
                            fontSize = 22.sp,
                            color = heartColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔲 게임 격자
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

                val density = LocalDensity.current
                val cellSizePx =
                    minOf(constraints.maxWidth / gridSize, constraints.maxHeight / gridSize)
                val cellSizeDp = with(density) { cellSizePx.toDp() }
                val fontSize = (cellSizePx / 5).sp

                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridSize),
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = false
                ) {
                    items(numbers.size) { index ->

                        val value = numbers[index]

                        val baseColor = when {
                            value == -1 -> Color(0xFF00E676)
                            wrongIndex == index -> Color(0xFFFF1744)
                            else -> Color(0xFF37474F)
                        }

                        val animatedColor by animateColorAsState(baseColor)

                        Box(
                            modifier = Modifier
                                .size(cellSizeDp)
                                .padding(2.dp)
                        ) {

                            Button(
                                onClick = {
                                    if (value == currentNumber) {
                                        numbers =
                                            numbers.toMutableList().also { it[index] = -1 }
                                        currentNumber++

                                        if (currentNumber > totalCount) {
                                            val elapsed =
                                                (System.currentTimeMillis() - startTime) / 1000.0
                                            scope.launch {
                                                RecordManager.saveRecord(
                                                    navController.context,
                                                    "number",
                                                    difficulty,
                                                    elapsed.toInt()
                                                )
                                            }
                                            navController.navigate(
                                                "number_success/$difficulty/$elapsedSeconds"
                                            )
                                        }
                                    } else {
                                        wrongIndex = index
                                        missCount++
                                        if (missCount >= maxLives) {
                                            navController.navigate("fail/$difficulty")
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = animatedColor
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {}

                            if (value != -1) {
                                Text(
                                    "$value",
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ❌ 틀린 칸 플래시
    LaunchedEffect(wrongIndex) {
        if (wrongIndex != null) {
            delay(200)
            wrongIndex = null
        }
    }
}