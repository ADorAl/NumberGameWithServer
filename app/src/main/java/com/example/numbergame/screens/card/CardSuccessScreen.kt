package com.example.numbergame.screens.card

// Compose 애니메이션 관련
import androidx.compose.animation.animateColorAsState

// UI 구성 요소
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// Material3 UI
import androidx.compose.material3.*

// Compose 상태 관리
import androidx.compose.runtime.*

// UI 관련 클래스
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

// Navigation
import androidx.navigation.NavController

// 기록 저장/불러오기
import com.example.numbergame.data.getRecords
import com.example.numbergame.data.saveRecord

// 코루틴 delay
import kotlinx.coroutines.delay

// 수학 함수
import kotlin.math.abs


// 카드 게임 성공 화면
@Composable
fun CardSuccessScreen(
    navController: NavController, // 화면 이동을 위한 네비게이션
    difficulty: Int,              // 현재 난이도
    usedTime: Int                 // 이번 게임에 걸린 시간
) {

    // 안드로이드 context 가져오기 (데이터 저장에 필요)
    val context = LocalContext.current

    // 기록 리스트 (Top10 저장용)
    val records = remember { mutableStateListOf<Double>() }

    // 사용 시간을 Double로 변환 (저장 시 Double 사용)
    val usedTimeDouble = usedTime.toDouble()

    /**
     * 화면이 실행될 때 기록 저장 및 불러오기
     */
    LaunchedEffect(usedTime) {

        // 기록 저장
        // difficulty + 100 → 카드게임용 구분값
        saveRecord(context, difficulty + 100, usedTimeDouble)

        // 기존 리스트 초기화
        records.clear()

        // 저장된 기록 다시 불러오기
        records.addAll(getRecords(context, difficulty + 100))
    }

    // 최고 기록 (가장 작은 시간)
    val bestScore = records.minOrNull()

    /**
     * 배경 그라데이션
     */
    val background = Brush.verticalGradient(
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    /**
     * 타이틀 색상 깜빡이는 효과
     */
    var glow by remember { mutableStateOf(true) }

    // 0.7초마다 색상 변경
    LaunchedEffect(Unit) {
        while (true) {
            delay(700)
            glow = !glow
        }
    }

    // 색상 애니메이션
    val titleColor by animateColorAsState(
        targetValue = if (glow) Color(0xFF00E676) else Color(0xFF69F0AE),
        label = ""
    )

    /**
     * 전체 화면 레이아웃
     */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()), // 스크롤 가능
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            /**
             * 스테이지 클리어 타이틀
             */
            Text(
                text = "🎉 STAGE CLEAR!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            /**
             * 이번 게임 기록
             */
            Text(
                text = "이번 게임의 기록 : ${usedTime} 초",
                fontSize = 22.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            /**
             * 최고 기록 표시
             */
            bestScore?.let {
                Text(
                    text = "🏆 BEST : ${String.format("%.0f", it)} sec",
                    fontSize = 20.sp,
                    color = Color.Yellow
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /**
             * TOP10 랭킹 타이틀
             */
            Text(
                text = "TOP 10 RANKING",
                fontSize = 18.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            /**
             * 기록 리스트 출력
             */
            records.forEachIndexed { index, time ->

                // 현재 기록이 이번 게임 기록인지 확인
                val isNew = abs(time - usedTimeDouble) < 0.001

                Text(
                    text = "${index + 1}. ${
                        String.format("%.0f", time)
                    } sec ${if (isNew) "✨ NEW!" else ""}",
                    fontSize = 18.sp,

                    // NEW 기록이면 색상 강조
                    color = if (isNew) Color(0xFF00E5FF) else Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            /**
             * 다시 도전 버튼
             */
            Button(
                onClick = {

                    // 같은 난이도 다시 시작
                    navController.navigate("game/card/$difficulty") {

                        // 이전 게임 화면 제거
                        popUpTo("game/card/$difficulty") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF)
                ),
                elevation = ButtonDefaults.buttonElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
            ) {
                Text("🔁 재시작", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            /**
             * 레벨 선택 화면 이동
             */
            Button(
                onClick = {
                    navController.navigate("difficulty/card")
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF)
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
            ) {
                Text("🎯 레벨 선택", fontSize = 18.sp)
            }

            /**
             * 다음 단계 버튼
             * (최대 난이도 전까지만 표시)
             */
            if (difficulty < 4) {

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {

                        // 다음 난이도 게임 시작
                        navController.navigate("game/card/${difficulty + 1}")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp)
                ) {
                    Text("🚀 다음 단계로", fontSize = 18.sp)
                }
            }
        }
    }
}