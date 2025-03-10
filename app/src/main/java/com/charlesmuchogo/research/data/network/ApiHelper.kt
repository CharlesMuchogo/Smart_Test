package com.charlesmuchogo.research.data.network

import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.presentation.navigation.LoginPage
import com.charlesmuchogo.research.presentation.utils.Results
import io.ktor.http.HttpStatusCode

class ApiHelper (
     //private val authenticationViewModel: AuthenticationViewModel
    private val database: AppDatabase,
) {
    suspend fun <T : Any> safeApiCall(
        statusCode: HttpStatusCode,
        apiCall: suspend () -> T,
    ): Results<T> =
        try {
            when (statusCode) {
                HttpStatusCode.Unauthorized -> {
                    database.userDao().deleteUsers()
                    database.clinicsDao().deleteClinics()
                    database.testResultsDao().deleteResults()
                    database.testProgressDao().clearTestProgress()
                    navController.navigate(LoginPage){
                        popUpTo<LoginPage> {
                            inclusive = true
                        }
                    }
                    Results.error("Your session has expired. Login again to continue")
                }

                else -> {
                    val result = apiCall.invoke()
                    Results.success(result)
                }
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Results.error("Server error ${throwable.message}")
        }
}
