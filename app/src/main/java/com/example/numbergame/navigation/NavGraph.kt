package com.example.numbergame.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.numbergame.screens.NumberSuccessScreen
import com.example.numbergame.screens.number.FailScreen
import com.example.numbergame.screens.card.CardFailScreen
import com.example.numbergame.screens.card.CardGameScreen
import com.example.numbergame.screens.card.CardSuccessScreen
import com.example.numbergame.screens.fourbasic.FourBasicOperationDifficultyScreen
import com.example.numbergame.screens.fourbasic.FourBasicOperationFailScreen
import com.example.numbergame.screens.fourbasic.FourBasicOperationScreen
import com.example.numbergame.screens.fourbasic.FourBasicOperationSuccessScreen
import com.example.numbergame.screens.main.MainScreen
import com.example.numbergame.screens.main.DifficultyScreen
import com.example.numbergame.screens.number.HintDifficultyScreen
import com.example.numbergame.screens.number.HintGameScreen
import com.example.numbergame.screens.number.HintSuccessScreen
import com.example.numbergame.screens.number.NumberGameScreen
import com.example.numbergame.screens.reaction.ReactionTestScreen
import com.example.numbergame.screens.record.RecordScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        // 🔹 메인
        composable("main") {
            MainScreen(navController)
        }

        // 🔹 난이도 선택
        composable(
            route = "difficulty/{gameType}",
            arguments = listOf(
                navArgument("gameType") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val gameType = backStackEntry.arguments?.getString("gameType")!!
            DifficultyScreen(navController = navController, gameType = gameType)
        }

        // 🔹 게임 화면 (공통)
        composable(
            route = "game/{gameType}/{difficulty}",
            arguments = listOf(
                navArgument("gameType") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val gameType = backStackEntry.arguments?.getString("gameType")!!
            val difficulty = backStackEntry.arguments?.getInt("difficulty")!!

            if (gameType == "number") {
                NumberGameScreen(navController, difficulty)
            } else if (gameType == "card") {
                CardGameScreen(navController, difficulty)
            }
        }

        // 🔥 숫자 게임 성공 화면
        composable(
            route = "number_success/{difficulty}/{time}",
            arguments = listOf(
                navArgument("difficulty") { type = NavType.IntType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            val timeString = backStackEntry.arguments?.getString("time")
            val elapsedTime = timeString?.toDoubleOrNull()

            NumberSuccessScreen(
                navController = navController,
                difficulty = difficulty,
                elapsedTime = elapsedTime
            )
        }

        // 🔹 숫자 게임 실패 화면
        composable(
            route = "fail/{difficulty}",
            arguments = listOf(
                navArgument("difficulty") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            FailScreen(navController, difficulty)
        }

        // 🔹 힌트 게임 시작 화면
        composable(
            route = "hintgame/{difficulty}",
            arguments = listOf(navArgument("difficulty") { type = NavType.IntType })
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            HintGameScreen(navController, difficulty)
        }

// 🔹 힌트 게임 성공 화면
        composable(
            route = "hint_success/{difficulty}/{time}",
            arguments = listOf(
                navArgument("difficulty") { type = NavType.IntType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            val timeString = backStackEntry.arguments?.getString("time")
            val elapsed = timeString?.toDoubleOrNull()

            HintSuccessScreen(navController, difficulty, elapsed)
        }

        // 🔹 힌트 게임 모드 화면 (추가, route "hint")
        composable("hint") {
            HintDifficultyScreen(navController)
        }

        // 🔹 카드 게임
        composable("card_game/{difficulty}") { backStack ->
            val difficulty = backStack.arguments?.getString("difficulty")?.toInt() ?: 1
            CardGameScreen(navController, difficulty)
        }

        // 🔹 카드 성공
        composable("card_success/{difficulty}/{usedTime}") { backStack ->
            val difficulty = backStack.arguments?.getString("difficulty")?.toInt() ?: 1
            val usedTime = backStack.arguments?.getString("usedTime")?.toInt() ?: 0
            CardSuccessScreen(navController, difficulty, usedTime)
        }

        // 🔹 카드 실패
        composable("card_fail/{difficulty}") { backStack ->
            val difficulty = backStack.arguments?.getString("difficulty")?.toInt() ?: 1
            CardFailScreen(navController, difficulty)
        }

        // 🔹 기록 화면
        composable("record") {
            RecordScreen(navController)
        }


// 🔹 사칙연산 게임 난이도 선택 화면
        composable(
            route = "four_basic_operation_difficulty/{operation}",
            arguments = listOf(
                navArgument("operation") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val operation = backStackEntry.arguments?.getString("operation") ?: "+"
            FourBasicOperationDifficultyScreen(navController, operation)
        }

// 🔹 사칙연산 문제 화면
        composable(
            route = "four_basic_operation/{operation}/{difficulty}",
            arguments = listOf(
                navArgument("operation") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val operation = backStackEntry.arguments?.getString("operation") ?: "+"
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            FourBasicOperationScreen(navController, operation, difficulty)
        }

        // 사칙연산 성공
        composable(
            route = "four_basic_operation_success/{operation}/{difficulty}/{usedTime}",
            arguments = listOf(
                navArgument("operation") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.IntType },
                navArgument("usedTime") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val operation = backStackEntry.arguments?.getString("operation") ?: "+"
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            val usedTime = backStackEntry.arguments?.getInt("usedTime") ?: 0
            FourBasicOperationSuccessScreen(navController, operation, difficulty, usedTime)
        }

// 사칙연산 실패
        composable(
            route = "four_basic_operation_fail/{operation}/{difficulty}",
            arguments = listOf(
                navArgument("operation") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val operation = backStackEntry.arguments?.getString("operation") ?: "+"
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            FourBasicOperationFailScreen(navController, operation, difficulty)
        }

        // 🔹 반응 속도 테스트
        composable("reaction_test") {
            ReactionTestScreen(navController)
        }
    }
}