package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.network.ApiHelper
import com.charlesmuchogo.research.data.network.Http
import com.charlesmuchogo.research.domain.dto.DeleteTestResultsDTO
import com.charlesmuchogo.research.domain.dto.ErrorDTO
import com.charlesmuchogo.research.domain.dto.GetTestResultsDTO
import com.charlesmuchogo.research.domain.dto.login.GetClinicsDTO
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.login.LoginResponseDTO
import com.charlesmuchogo.research.domain.dto.login.RegistrationRequestDTO
import com.charlesmuchogo.research.domain.dto.message.GetMessagesDTO
import com.charlesmuchogo.research.domain.dto.message.SendMessageResponse
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsDTO
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsResponse
import com.charlesmuchogo.research.domain.dto.updateUser.EditProfileDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsResponseDTO
import com.charlesmuchogo.research.domain.models.Message
import com.charlesmuchogo.research.domain.states.UpdateProfileState
import com.charlesmuchogo.research.presentation.utils.Results
import com.charlesmuchogo.research.presentation.utils.decodeExceptionMessage
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RemoteRepositoryImpl(
    private val apiHelper: ApiHelper,
    private val settingsRepository: MultiplatformSettingsRepository,
) : RemoteRepository {
    override suspend fun login(loginRequestDTO: LoginRequestDTO): Results<LoginResponseDTO> {
        return try {
            val response =
                Http(settingsRepository = settingsRepository).client.post("/api/login") {
                    contentType(ContentType.Application.Json)
                    setBody(loginRequestDTO)
                }

            if (response.status != HttpStatusCode.OK) {
               val apiResponse = apiHelper.safeApiCall(response.status) {
                    response.body<ErrorDTO>()
                }
                Results.error(apiResponse.data?.message ?: "Error logging in")
            } else {
                 apiHelper.safeApiCall(response.status) {
                    response.body<LoginResponseDTO>()
                }
            }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }
    }

    override suspend fun signUp(registrationRequestDTO: RegistrationRequestDTO): Flow<Results<LoginResponseDTO>> {
        return flow {
            try {
                val response =
                    Http(settingsRepository = settingsRepository).client.post("/api/register") {
                        contentType(ContentType.Application.Json)
                        setBody(registrationRequestDTO)
                    }

                if (response.status != HttpStatusCode.OK) {
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
        }
    }

    override suspend fun completeRegistration(detailsDTO: UpdateUserDetailsDTO): Flow<Results<UpdateUserDetailsResponseDTO>> {
        return flow {
            try {
                val response =
                    Http(settingsRepository = settingsRepository).client.post("/api/mobile/user") {
                        contentType(ContentType.Application.Json)
                        setBody(detailsDTO)
                    }

                if (response.status != HttpStatusCode.OK) {
                    val apiResponse = apiHelper.safeApiCall(response.status) {
                        response.body<ErrorDTO>()
                    }
                    emit(
                        Results.error(
                            apiResponse.data?.message ?: "Error updating your details. Try again"
                        )
                    )
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
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateProfile(request: UpdateProfileState): Results<EditProfileDTO> {
        return try {
            val response =
                Http(settingsRepository = settingsRepository).client.put("/api/mobile/profile") {
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append("first_name", request.firstName)
                                append("last_name", request.lastName)
                                append("phone", request.phoneNumber)
                                append("age", request.dateOfBirth)
                                append("gender", request.gender)
                                append("tested_before", request.testedBefore)
                                append("country", request.country)

                                request.image?.let {
                                    append(
                                        key = "image",
                                        value = it,
                                        headers = Headers.build {
                                            append(HttpHeaders.ContentType, "image/png")

                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=\"${Uuid.random()}.png\""
                                            )
                                        }
                                    )
                                }

                            }
                        )
                    )
                }

            if (response.status != HttpStatusCode.OK) {
                val apiResponse = apiHelper.safeApiCall(statusCode = response.status) {
                    response.body<ErrorDTO>()
                }
                Results.error(
                    msg = apiResponse.data?.message ?: "Error updating profile. Try again"
                )
            } else {
                apiHelper.safeApiCall(statusCode = response.status) {
                    response.body<EditProfileDTO>()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Results.error(decodeExceptionMessage(e))
        }
    }

    override suspend fun fetchTestResults(): Flow<Results<GetTestResultsDTO>> =
        flow {
            try {
                val response =
                    Http(settingsRepository = settingsRepository).client.get("/api/mobile/results") {
                        contentType(ContentType.Application.Json)
                    }

                if (response.status != HttpStatusCode.OK) {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<ErrorDTO>()
                        }

                    emit(
                        Results.error(
                            apiResponse.data?.message ?: "Error getting results. Try again"
                        )
                    )
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
        }

    override suspend fun fetchClinics(): Flow<Results<GetClinicsDTO>> {
        return flow {
            try {
                val response =
                    Http(settingsRepository = settingsRepository).client.get("/api/mobile/clinics") {
                        contentType(ContentType.Application.Json)
                    }

                if (response.status != HttpStatusCode.OK) {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<ErrorDTO>()
                        }

                    emit(
                        Results.error(
                            apiResponse.data?.message ?: "Error getting clinics. Try again"
                        )
                    )
                } else {
                    val apiResponse =
                        apiHelper.safeApiCall(response.status) {
                            response.body<GetClinicsDTO>()
                        }
                    emit(apiResponse)
                }
            } catch (e: Exception) {
                emit(Results.error(decodeExceptionMessage(e)))
            }
        }
    }

    override suspend fun deleteResult(uuid: String): Results<DeleteTestResultsDTO> {
        return try {
            val response =
                Http(settingsRepository = settingsRepository).client.delete("/api/mobile/results") {
                    contentType(ContentType.Application.Json)
                    parameter("uuid", uuid)
                }

            if (response.status != HttpStatusCode.OK) {
                val apiResponse =
                    apiHelper.safeApiCall(response.status) {
                        response.body<ErrorDTO>()
                    }

                Results.error(
                    apiResponse.data?.message ?: "Error deleting test. Try again"

                )
            } else {

                apiHelper.safeApiCall(response.status) {
                    response.body<DeleteTestResultsDTO>()
                }
            }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }

    }

    override suspend fun uploadResults(results: UploadTestResultsDTO): Flow<Results<UploadTestResultsResponse>> {
        return flow {
            try {
                println("Care option: ${results.careOption}")
                val response =
                    Http(settingsRepository = settingsRepository).client.post("/api/mobile/results") {
                        setBody(
                            MultiPartFormDataContent(
                                formData {
                                    results.careOption?.let { append("care_option", it) }
                                    append(
                                        "user_photo",
                                        results.image,
                                        Headers.build {
                                            append(HttpHeaders.ContentType, "image/png")
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename=\"userPhoto.png\""
                                            )
                                        }
                                    )


                                    results.partnerImage?.let {
                                        append(
                                            "partner_photo",
                                            it,
                                            Headers.build {
                                                append(HttpHeaders.ContentType, "image/png")
                                                append(
                                                    HttpHeaders.ContentDisposition,
                                                    "filename=\"userPhoto.png\""
                                                )
                                            }
                                        )
                                    }

                                }
                            )
                        )
                    }

                if (response.status != HttpStatusCode.OK) {
                    val apiResponse = apiHelper.safeApiCall(statusCode = response.status) {
                        response.body<ErrorDTO>()
                    }

                    emit(
                        Results.error(
                            apiResponse.data?.message ?: "Error uploading results. Try again"
                        )
                    )
                } else {
                    val apiResponse = apiHelper.safeApiCall(statusCode = response.status) {
                        response.body<UploadTestResultsResponse>()
                    }
                    emit(apiResponse)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Results.error(decodeExceptionMessage(e)))
            }
        }
    }

    override suspend fun sendMessage(message: Message): Results<SendMessageResponse> {
        return try {
            val response =
                Http(settingsRepository = settingsRepository).client.post("/api/mobile/messages") {
                    contentType(ContentType.Application.Json)
                    setBody(message)
                }

            if (response.status != HttpStatusCode.OK) {
                val apiResponse = apiHelper.safeApiCall(response.status) {
                    response.body<ErrorDTO>()
                }
                Results.error(apiResponse.data?.message ?: "Something went wrong. Try again")
            } else {
                apiHelper.safeApiCall(response.status) {
                    response.body<SendMessageResponse>()
                }
            }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }
    }

    override suspend fun getMessages(): Results<GetMessagesDTO> {
        return try {
            val response =
                Http(settingsRepository = settingsRepository).client.get("/api/mobile/messages") {
                    contentType(ContentType.Application.Json)
                }

            if (response.status != HttpStatusCode.OK) {
                val apiResponse = apiHelper.safeApiCall(response.status) {
                    response.body<ErrorDTO>()
                }
                Results.error(apiResponse.data?.message ?: "Something went wrong. Try again")
            } else {
                apiHelper.safeApiCall(response.status) {
                    response.body<GetMessagesDTO>()
                }
            }
        } catch (e: Exception) {
            Results.error(decodeExceptionMessage(e))
        }
    }
}
