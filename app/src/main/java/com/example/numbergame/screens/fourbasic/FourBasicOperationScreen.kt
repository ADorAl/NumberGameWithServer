package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@Composable
fun FourBasicOperationScreen(
    navController: NavController,
    operation: String,
    difficulty: Int
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var timeLeft by remember { mutableStateOf(30) }
    var answer by remember { mutableStateOf("") }

    val problem: Problem = remember {
        generateProblem(operation, difficulty)
    }

    // 타이머
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else {
            navController.navigate("four_basic_operation_fail/$operation/$difficulty")
        }
    }

    val progress = timeLeft / 30f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 🔥 상단 타이머 바
            Column {
                Text(
                    text = "⏳ TIME",
                    color = Color.White,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp),
                    color = if (timeLeft <= 5) Color.Red else Color.Green,
                    trackColor = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$timeLeft 초",
                    color = Color.White
                )
            }

            // 🎯 문제 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1F2A38)
                ),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "문제",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = problem.question,
                        fontSize = 30.sp,
                        color = Color.White
                    )
                }
            }

            // 🎮 입력 & 버튼
            Column {

                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("정답 입력") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 22.sp,
                        color = Color.White
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Cyan,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Cyan,
                        cursorColor = Color.Cyan
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val isCorrect = answer.toIntOrNull() == problem.answer
                        val usedTime = 30 - timeLeft

                        scope.launch {
                            RecordManager.saveRecord(
                                context,
                                "four_basic",
                                difficulty,
                                usedTime
                            )

                            if (isCorrect) {
                                navController.navigate(
                                    "four_basic_operation_success/$operation/$difficulty/$usedTime"
                                )
                            } else {
                                navController.navigate(
                                    "four_basic_operation_fail/$operation/$difficulty"
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C853)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "🚀 제출",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ----------------------------
// 문제 데이터 클래스
data class Problem(
    val question: String,
    val answer: Int
)

// 문제 생성 함수
fun generateProblem(operation: String, difficulty: Int): Problem {

    val range = when (difficulty) {
        1 -> 1..10
        2 -> 1..20
        3 -> 1..30
        else -> 1..10
    }

    fun randomNumber() = range.random()

    // 연산 개수 = 난이도
    val operationCount = difficulty

    // 나눗셈은 따로 처리
    if (operation == "÷") {

        val divisors = MutableList(operationCount) {
            randomNumber().let { if (it == 0) 1 else it }
        }

        val answer = randomNumber()

        // 나눗셈은 역으로 만들어서 항상 정수 결과 나오게
        val dividend = divisors.fold(answer) { acc, value ->
            acc * value
        }

        val question = buildString {
            append(dividend)
            divisors.forEach {
                append(" ÷ $it")
            }
            append(" = ?")
        }

        return Problem(question, answer)
    }

    // + - × 공통 처리
    val numbers = MutableList(operationCount + 1) {
        randomNumber()
    }

    val question = buildString {
        append(numbers[0])
        for (i in 1 until numbers.size) {
            append(" $operation ${numbers[i]}")
        }
        append(" = ?")
    }

    val result = when (operation) {
        "+" -> numbers.sum()
        "-" -> numbers.reduce { acc, value -> acc - value }
        "×" -> numbers.reduce { acc, value -> acc * value }
        else -> numbers.sum()
    }

    return Problem(question, result)
}