package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.domain.dto.DeleteTestResultsDTO
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
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun login(loginRequestDTO: LoginRequestDTO): Results<LoginResponseDTO>

    suspend fun signUp(registrationRequestDTO: RegistrationRequestDTO): Flow<Results<LoginResponseDTO>>

    suspend fun completeRegistration(detailsDTO: UpdateUserDetailsDTO): Flow<Results<UpdateUserDetailsResponseDTO>>

    suspend fun updateProfile(request: UpdateProfileState): Results<EditProfileDTO>

    suspend fun fetchTestResults(): Flow<Results<GetTestResultsDTO>>

    suspend fun fetchClinics(): Flow<Results<GetClinicsDTO>>

    suspend fun deleteResult(uuid: String): Results<DeleteTestResultsDTO>

    suspend fun uploadResults(results: UploadTestResultsDTO): Flow<Results<UploadTestResultsResponse>>

    suspend fun sendMessage(message: Message): Results<SendMessageResponse>

    suspend fun getMessages(): Results<GetMessagesDTO>
}
