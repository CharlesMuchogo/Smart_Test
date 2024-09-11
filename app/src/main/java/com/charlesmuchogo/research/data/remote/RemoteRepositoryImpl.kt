package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.network.ApiHelper
import com.charlesmuchogo.research.data.network.Http
import com.charlesmuchogo.research.domain.dto.ErrorDTO
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.login.LoginResponseDTO
import com.charlesmuchogo.research.presentation.utils.Results
import com.charlesmuchogo.research.presentation.utils.decodeExceptionMessage
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.coroutineContext

class RemoteRepositoryImpl(private val apiHelper: ApiHelper, private val appDatabase: AppDatabase): RemoteRepository {
    override suspend fun login(loginRequestDTO: LoginRequestDTO): Flow<Results<LoginResponseDTO>> {
        return flow {
            try {
                val response =
                    Http(appDatabase = appDatabase).client.post("/api/login") {
                        contentType(ContentType.Application.Json)
                        setBody(loginRequestDTO)
                    }

                if (response.status != HttpStatusCode.OK) {

                    val apiResponse = apiHelper.safeApiCall(response.status) {
                        response.body<ErrorDTO>()
                    }

                    emit(Results.error(apiResponse.data?.message ?: "Error logging in"))
                } else {
                    val apiResponse = apiHelper.safeApiCall(response.status) {
                        response.body<LoginResponseDTO>()
                    }
                    emit(apiResponse)
                }
            } catch (e: Exception) {
                emit(Results.error(decodeExceptionMessage(e)))
            }
        }.flowOn(Dispatchers.Main)
    }

}