package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.network.ApiHelper
import com.charlesmuchogo.research.data.network.Http
import com.charlesmuchogo.research.domain.dto.ErrorDTO
import com.charlesmuchogo.research.domain.dto.GetTestResultsDTO
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.login.LoginResponseDTO
import com.charlesmuchogo.research.domain.dto.login.RegistrationRequestDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsResponseDTO
import com.charlesmuchogo.research.presentation.utils.Results
import com.charlesmuchogo.research.presentation.utils.decodeExceptionMessage
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteRepositoryImpl(
    private val apiHelper: ApiHelper,
    private val appDatabase: AppDatabase,
) : RemoteRepository {
    override suspend fun login(loginRequestDTO: LoginRequestDTO): Flow<Results<LoginResponseDTO>> =
        flow {
            try {
                val response =
                    Http(appDatabase = appDatabase).client.post("/api/login") {
                        contentType(ContentType.Application.Json)
                        setBody(loginRequestDTO)
                    }

                if (response.status != HttpStatusCode.OK) {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<ErrorDTO>()
                        }

                    emit(Results.error(apiResponse.data?.message ?: "Error logging in"))
                } else {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<LoginResponseDTO>()
                        }
                    emit(apiResponse)
                }
            } catch (e: Exception) {
                emit(Results.error(decodeExceptionMessage(e)))
            }
        }.flowOn(Dispatchers.Main)

    override suspend fun signUp(registrationRequestDTO: RegistrationRequestDTO): Flow<Results<LoginResponseDTO>> {
        return flow {
            try {
                val response =
                    Http(appDatabase = appDatabase).client.post("/api/register") {
                        contentType(ContentType.Application.Json)
                        setBody(registrationRequestDTO)
                    }

                if (response.status != HttpStatusCode.Created) {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<ErrorDTO>()
                        }

                    emit(Results.error(apiResponse.data?.message ?: "Error signing up"))
                } else {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<LoginResponseDTO>()
                        }
                    emit(apiResponse)
                }
            } catch (e: Exception) {
                emit(Results.error(decodeExceptionMessage(e)))
            }
        }.flowOn(Dispatchers.Main)
    }

    override suspend fun completeRegistration(detailsDTO: UpdateUserDetailsDTO): Flow<Results<UpdateUserDetailsResponseDTO>> {
        return flow {
            try {
                val response =
                    Http(appDatabase = appDatabase).client.post("/api/mobile/user") {
                        contentType(ContentType.Application.Json)
                        setBody(detailsDTO)
                    }

                if (response.status != HttpStatusCode.OK) {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<ErrorDTO>()
                        }

                    emit(Results.error(apiResponse.data?.message ?: "Error updating your details. Try again"))
                } else {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<UpdateUserDetailsResponseDTO>()
                        }
                    emit(apiResponse)
                }
            } catch (e: Exception) {
                emit(Results.error(decodeExceptionMessage(e)))
            }
        }.flowOn(Dispatchers.Main)
    }

    override suspend fun fetchTestResults(): Flow<Results<GetTestResultsDTO>> =
        flow {
            try {
                val response =
                    Http(appDatabase = appDatabase).client.get("/api/mobile/results") {
                        contentType(ContentType.Application.Json)
                    }

                if (response.status != HttpStatusCode.OK) {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<ErrorDTO>()
                        }

                    emit(Results.error(apiResponse.data?.message ?: "Error getting results. Try again"))
                } else {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<GetTestResultsDTO>()
                        }
                    emit(apiResponse)
                }
            } catch (e: Exception) {
                emit(Results.error(decodeExceptionMessage(e)))
            }
        }.flowOn(Dispatchers.Main)
}
