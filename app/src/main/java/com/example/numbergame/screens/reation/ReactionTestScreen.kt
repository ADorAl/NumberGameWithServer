package com.example.numbergame.screens.reaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlin.random.Random

enum class CellState { EMPTY, WARNING, DANGER }

@Composable
fun ReactionTestScreen(navController: NavController) {

    val gridSize = 5
    var playerPos by remember { mutableStateOf(2 to 2) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var gameOver by remember { mutableStateOf(false) }
    var bestTime by remember { mutableStateOf(0L) }

    val scope = rememberCoroutineScope()
    val context = navController.context

    var dangerGrid by remember {
        mutableStateOf(List(gridSize) { MutableList(gridSize) { CellState.EMPTY } })
    }

    // 🔥 시작 시 최고 기록 불러오기
    LaunchedEffect(Unit) {
        val record = RecordManager
            .getRecord(context, "reaction", 1)
            .first()
        bestTime = record?.toLong() ?: 0L
    }

    // ⏱ 시간 카운트
    LaunchedEffect(gameOver) {
        while (!gameOver) {
            delay(1000)
            elapsedTime++
        }
    }

    // ⚡ 점점 아주 미세하게 빨라짐
    val speedMultiplier = 1f - (elapsedTime * 0.005f).coerceAtMost(0.25f)
    val warningDelay = (2000 * speedMultiplier).toLong()
    val dangerDelay = (1000 * speedMultiplier).toLong()

    // ⚡ 메인 루프
    LaunchedEffect(Unit) {
        delay(1000)

        while (true) {

            if (!gameOver) {

                val safeCount = if (elapsedTime > 20) 2 else 1

                val safeZones = mutableListOf<Pair<Int, Int>>()
                repeat(safeCount) {
                    var rx: Int
                    var ry: Int
                    do {
                        rx = Random.nextInt(gridSize)
                        ry = Random.nextInt(gridSize)
                    } while (safeZones.contains(rx to ry))
                    safeZones.add(rx to ry)
                }

                // 전체 위험
                dangerGrid = List(gridSize) {
                    MutableList(gridSize) { CellState.DANGER }
                }

                // SAFE 예고
                safeZones.forEach { (x, y) ->
                    dangerGrid[y][x] = CellState.WARNING
                }

                delay(warningDelay)

                // SAFE 확정
                safeZones.forEach { (x, y) ->
                    dangerGrid[y][x] = CellState.EMPTY
                }

                delay(dangerDelay)

                // SAFE 체크
                if (playerPos !in safeZones) {
                    gameOver = true

                    if (elapsedTime > bestTime) {
                        bestTime = elapsedTime

                        scope.launch {
                            RecordManager.saveRecord(
                                context,
                                "reaction",
                                1,
                                bestTime.toInt()
                            )
                        }
                    }
                }

                // 초기화
                dangerGrid = List(gridSize) {
                    MutableList(gridSize) { CellState.EMPTY }
                }
            } else {
                delay(500)
            }
        }
    }

    // ================= UI =================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F111A))
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (gameOver) "💀 GAME OVER" else "⚡ FIND SAFE ZONE",
                fontSize = 24.sp,
                color = if (gameOver) Color.Red else Color.Cyan
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "TIME : $elapsedTime s",
                fontSize = 18.sp,
                color = Color.White
            )

            Text(
                text = "BEST : $bestTime s",
                fontSize = 16.sp,
                color = Color.Magenta
            )

            Spacer(modifier = Modifier.height(24.dp))

            // GRID
            Column {
                for (y in 0 until gridSize) {
                    Row {
                        for (x in 0 until gridSize) {

                            val cellState = dangerGrid[y][x]

                            val color = when {
                                playerPos.first == x && playerPos.second == y ->
                                    Color(0xFF00E5FF)

                                cellState == CellState.WARNING ->
                                    Color(0xFF00E676)

                                cellState == CellState.DANGER ->
                                    Color(0xFFD50000)

                                else -> Color(0xFF1C2230)
                            }

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(2.dp)
                                    .background(
                                        color,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🎮 조작패드
            Box(
                modifier = Modifier.size(220.dp),
                contentAlignment = Alignment.Center
            ) {

                GameButton("▲") {
                    if (playerPos.second > 0)
                        playerPos = playerPos.first to (playerPos.second - 1)
                }

                GameButton("▼", Alignment.BottomCenter) {
                    if (playerPos.second < gridSize - 1)
                        playerPos = playerPos.first to (playerPos.second + 1)
                }

                GameButton("◀", Alignment.CenterStart) {
                    if (playerPos.first > 0)
                        playerPos = (playerPos.first - 1) to playerPos.second
                }

                GameButton("▶", Alignment.CenterEnd) {
                    if (playerPos.first < gridSize - 1)
                        playerPos = (playerPos.first + 1) to playerPos.second
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (gameOver) {
                Button(
                    onClick = {
                        playerPos = 2 to 2
                        elapsedTime = 0
                        gameOver = false
                        dangerGrid =
                            List(gridSize) { MutableList(gridSize) { CellState.EMPTY } }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C853)
                    )
                ) {
                    Text("🔁 RESTART")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("main") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text("🏠 MAIN")
            }
        }
    }
}

@Composable
fun BoxScope.GameButton(
    text: String,
    alignment: Alignment = Alignment.TopCenter,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(80.dp)
            .align(alignment),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF263238).copy(alpha = 0.6f),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 24.sp
        )
    }
}