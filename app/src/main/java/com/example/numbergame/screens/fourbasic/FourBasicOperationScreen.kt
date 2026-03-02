package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    var timeLeft by remember { mutableStateOf(30) } // 제한 시간
    var answer by remember { mutableStateOf("") }

    // 문제 생성
    val problem = remember { generateProblem(operation, difficulty) }

    // 타이머
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else {
            navController.navigate("four_basic_operation_fail/$operation/$difficulty")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("문제: ${problem.question}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("남은 시간: $timeLeft 초", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("정답 입력") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val isCorrect = answer.toIntOrNull() == problem.answer
            val usedTime = 30 - timeLeft

            scope.launch {
                // ✅ DataStore 최고 기록 갱신
                RecordManager.saveRecord(context, "four_basic", difficulty, usedTime)

                // 성공/실패 화면 이동
                if (isCorrect) {
                    navController.navigate(
                        "four_basic_operation_success/$operation/$difficulty/$usedTime"
                    )
                } else {
                    navController.navigate("four_basic_operation_fail/$operation/$difficulty")
                }
            }
        }) {
            Text("제출")
        }
    }
}

// 문제 데이터 클래스
data class Problem(val question: String, val answer: Int)

// 문제 생성 함수
fun generateProblem(operation: String, difficulty: Int): Problem {
    val count = difficulty
    var question = ""
    var answer = 0

    repeat(count) { i ->
        when (operation) {
            "+", "-", "×" -> {
                val (a, b) = when (difficulty) {
                    1 -> (0..10).random() to (0..10).random()
                    2 -> (0..50).random() to (0..50).random()
                    3 -> (0..100).random() to (0..100).random()
                    else -> 1 to 1
                }

                if (i == 0) {
                    answer = when (operation) {
                        "+" -> a + b
                        "-" -> a - b
                        "×" -> a * b
                        else -> a + b
                    }
                    question = when (operation) {
                        "+" -> "$a + $b"
                        "-" -> "$a - $b"
                        "×" -> "$a × $b"
                        else -> "$a + $b"
                    }
                } else {
                    val newPart = when (operation) {
                        "+" -> " + $b"
                        "-" -> " - $b"
                        "×" -> " × $b"
                        else -> " + $b"
                    }
                    val newAnswer = when (operation) {
                        "+" -> answer + b
                        "-" -> answer - b
                        "×" -> answer * b
                        else -> answer + b
                    }
                    question += newPart
                    answer = newAnswer
                }
            }
            "÷" -> {
                if (i == 0) {
                    answer = when (difficulty) {
                        1 -> (1..10).random()
                        2 -> (2..20).random()
                        3 -> (2..50).random()
                        else -> 1
                    }
                    val divisor = when (difficulty) {
                        1 -> (1..5).random()
                        2 -> (2..10).random()
                        3 -> (2..20).random()
                        else -> 1
                    }
                    val dividend = answer * divisor
                    question = "$dividend ÷ $divisor"
                } else {
                    val divisors = (1..answer).filter { answer % it == 0 }
                    val divisor = divisors.random()
                    val dividend = answer * divisor
                    question += " ÷ $divisor"
                    answer /= divisor
                }
            }
        }
    }

    return Problem("$question = ?", answer)
}