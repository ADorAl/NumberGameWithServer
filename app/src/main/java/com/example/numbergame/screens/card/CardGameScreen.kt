package com.example.numbergame.screens.card

// 애니메이션 관련
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState

// UI 레이아웃 관련
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape

// Material3 UI
import androidx.compose.material3.*

// Compose 상태 관리
import androidx.compose.runtime.*

// UI 기본 요소
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*

// 네비게이션
import androidx.navigation.NavController

// 기록 저장 클래스
import com.example.numbergame.data.RecordManager

// 코루틴
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 수학 함수
import kotlin.math.min


// 카드 데이터 클래스
data class Card(
    val id: Int,          // 카드 고유 ID
    val value: Int,       // 카드에 표시될 숫자 (짝 맞추기용)
    val isFlipped: Boolean = false,   // 카드가 뒤집혔는지 여부
    val isMatched: Boolean = false    // 카드가 매칭 완료 되었는지 여부
)

@Composable
fun CardGameScreen(navController: NavController, difficulty: Int) {

    // 코루틴 실행을 위한 scope
    val scope = rememberCoroutineScope()

    // Context (기록 저장 등에서 사용)
    val context = LocalContext.current

    // 난이도에 따른 카드 행 개수
    val rowCount = difficulty + 1

    // 카드 열 개수
    val columnCount = 4

    // 전체 카드 수
    val totalCardCount = rowCount * columnCount

    // 카드 쌍 개수
    val pairCount = totalCardCount / 2

    // 난이도에 따른 제한 시간
    val timeLimit = when (difficulty) {
        1 -> 20
        2 -> 30
        3 -> 40
        else -> 50
    }

    // 남은 시간 상태
    var timeLeft by remember { mutableStateOf(timeLimit) }

    // 카드 리스트 상태
    var cards by remember { mutableStateOf(generateCards(pairCount)) }

    // 현재 선택된 카드 리스트 (최대 2장)
    var selectedCards by remember {
        mutableStateOf<List<Card>>(emptyList())
    }

    // ⏳ 타이머 동작
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)   // 1초 대기
            timeLeft--    // 시간 감소
        } else {
            // 시간 초과 → 실패 화면 이동
            navController.navigate("card_fail/$difficulty")
        }
    }

    // 배경 그라데이션
    val background = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {

        // 화면 크기 기반으로 카드 크기 계산
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

            val cardSize = min(
                maxWidth / columnCount - 12.dp,
                (maxHeight - 120.dp) / rowCount - 12.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                // 타이머 색상 애니메이션 (5초 이하일 때 빨간색)
                val timerColor by animateColorAsState(
                    targetValue = if (timeLeft <= 5)
                        Color(0xFFFF1744)
                    else
                        Color(0xFF00E5FF),
                    label = ""
                )

                // HUD 타이머 UI
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = timerColor),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Text(
                        text = "⏳ TIME : $timeLeft",
                        fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 카드들을 행 단위로 나누어 배치
                cards.chunked(columnCount).forEach { row ->
                    Row {
                        row.forEach { card ->

                            // 카드 UI
                            NeonFlipCard(
                                card = card,
                                size = cardSize,
                                onClick = {

                                    // 카드 선택 조건
                                    if (!card.isFlipped &&
                                        !card.isMatched &&
                                        selectedCards.size < 2
                                    ) {

                                        // 카드 뒤집기
                                        cards = cards.map {
                                            if (it.id == card.id)
                                                it.copy(isFlipped = true)
                                            else it
                                        }

                                        // 선택된 카드 목록 업데이트
                                        val newSelected =
                                            selectedCards + card.copy(isFlipped = true)

                                        selectedCards = newSelected

                                        // 카드 2장 선택 시 비교
                                        if (newSelected.size == 2) {

                                            scope.launch {

                                                delay(700) // 카드 확인 시간

                                                // 숫자가 같으면 매칭 성공
                                                if (newSelected[0].value ==
                                                    newSelected[1].value
                                                ) {

                                                    cards = cards.map {
                                                        if (it.id == newSelected[0].id ||
                                                            it.id == newSelected[1].id
                                                        )
                                                            it.copy(isMatched = true)
                                                        else it
                                                    }

                                                } else {

                                                    // 틀리면 다시 뒤집기
                                                    cards = cards.map {
                                                        if (it.id == newSelected[0].id ||
                                                            it.id == newSelected[1].id
                                                        )
                                                            it.copy(isFlipped = false)
                                                        else it
                                                    }
                                                }

                                                // 선택 카드 초기화
                                                selectedCards = emptyList()

                                                // 모든 카드 매칭 시 성공
                                                if (cards.all { it.isMatched }) {

                                                    val usedTime =
                                                        timeLimit - timeLeft

                                                    // 기록 저장
                                                    RecordManager.saveRecord(
                                                        context,
                                                        "card",
                                                        difficulty,
                                                        usedTime
                                                    )

                                                    // 성공 화면 이동
                                                    navController.navigate(
                                                        "card_success/$difficulty/$usedTime"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


// 카드 뒤집기 애니메이션 UI
@Composable
fun NeonFlipCard(card: Card, size: Dp, onClick: () -> Unit) {

    // 회전 애니메이션 (0 → 180도)
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        label = ""
    )

    Box(
        modifier = Modifier
            .padding(6.dp)
            .size(size)
            .clickable(enabled = !card.isMatched) { onClick() }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            },
        contentAlignment = Alignment.Center
    ) {

        // 카드 뒷면
        if (rotation <= 90f) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(0xFF6200EE),
                        RoundedCornerShape(12.dp)
                    )
            )

        } else {

            // 카드 앞면
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
                    .background(
                        if (card.isMatched)
                            Color(0xFFB2FF59)
                        else
                            Color.White,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = card.value.toString(),
                    fontSize = 22.sp,
                    color = Color.Black
                )
            }
        }
    }
}


// 카드 생성 함수 (짝 카드 생성 후 섞기)
fun generateCards(pairCount: Int): List<Card> {

    // 1~pairCount 숫자를 두 번씩 만들어 짝 생성
    val numbers = (1..pairCount)
        .flatMap { listOf(it, it) }
        .shuffled()

    // 카드 객체로 변환
    return numbers.mapIndexed { index, value ->
        Card(
            id = index,
            value = value
        )
    }
}