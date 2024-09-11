package com.charlesmuchogo.research.data.network

import com.charlesmuchogo.research.presentation.utils.Results
import io.ktor.http.HttpStatusCode


class ApiHelper(
  //  private val mainViewModel: MainViewModel
) {
    suspend fun <T : Any> safeApiCall(
        statusCode: HttpStatusCode,
        apiCall: suspend () -> T
    ): Results<T> {
        return try {
             when (statusCode) {
                HttpStatusCode.Unauthorized -> {
                   // mainViewModel.updateAuthenticationEvent(AuthenticationEvent(logout = true))
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
}