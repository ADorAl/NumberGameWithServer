package com.example.numbergame.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.numbergame.data.TokenManager
import com.example.numbergame.screens.auth.LoginScreen
import com.example.numbergame.screens.auth.SignupScreen
import com.example.numbergame.screens.main.MainScreen
import com.example.numbergame.screens.main.DifficultyScreen
import com.example.numbergame.screens.number.NumberGameScreen
import com.example.numbergame.screens.number.NumberSuccessScreen
import com.example.numbergame.screens.number.FailScreen
import com.example.numbergame.screens.card.CardGameScreen
import com.example.numbergame.screens.card.CardSuccessScreen
import com.example.numbergame.screens.card.CardFailScreen
import com.example.numbergame.screens.fourbasic.FourBasicOperationDifficultyScreen
import com.example.numbergame.screens.fourbasic.FourBasicOperationScreen
import com.example.numbergame.screens.fourbasic.FourBasicOperationSuccessScreen
import com.example.numbergame.screens.fourbasic.FourBasicOperationFailScreen
import com.example.numbergame.screens.reaction.ReactionTestScreen
import com.example.numbergame.screens.record.RecordScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // 🔹 로그인 여부에 따라 시작 화면 결정
    val startDestination = remember {
        if (TokenManager.getToken(context) != null) "main" else "login"
    }

    NavHost(navController = navController, startDestination = startDestination) {

        // 🔹 인증
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }

        // 🔹 메인
        composable("main") { MainScreen(navController) }

        // 🔹 난이도 선택
        composable(
            route = "difficulty/{gameType}",
            arguments = listOf(navArgument("gameType") { type = NavType.StringType })
        ) { backStackEntry ->
            val gameType = backStackEntry.arguments?.getString("gameType")!!
            DifficultyScreen(navController, gameType)
        }

        // 🔹 게임 화면
        composable(
            route = "game/{gameType}/{difficulty}",
            arguments = listOf(
                navArgument("gameType") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val gameType = backStackEntry.arguments?.getString("gameType")!!
            val difficulty = backStackEntry.arguments?.getInt("difficulty")!!
            if (gameType == "number") NumberGameScreen(navController, difficulty)
            else if (gameType == "card") CardGameScreen(navController, difficulty)
        }

        // 🔹 숫자 게임 성공/실패
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
            NumberSuccessScreen(navController, difficulty, elapsedTime)
        }

        composable(
            route = "fail/{difficulty}",
            arguments = listOf(navArgument("difficulty") { type = NavType.IntType })
        ) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getInt("difficulty") ?: 1
            FailScreen(navController, difficulty)
        }

        // 🔹 카드 게임 성공/실패
        composable("card_success/{difficulty}/{usedTime}") { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getString("difficulty")?.toInt() ?: 1
            val usedTime = backStackEntry.arguments?.getString("usedTime")?.toInt() ?: 0
            CardSuccessScreen(navController, difficulty, usedTime)
        }
        composable("card_fail/{difficulty}") { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getString("difficulty")?.toInt() ?: 1
            CardFailScreen(navController, difficulty)
        }

        // 🔹 기록 화면
        composable("record") { RecordScreen(navController) }

        // 🔹 사칙연산 게임
        composable(
            route = "four_basic_operation_difficulty/{operation}",
            arguments = listOf(navArgument("operation") { type = NavType.StringType })
        ) { backStackEntry ->
            val operation = backStackEntry.arguments?.getString("operation") ?: "+"
            FourBasicOperationDifficultyScreen(navController, operation)
        }

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
        composable("reaction_test") { ReactionTestScreen(navController) }
    }
}