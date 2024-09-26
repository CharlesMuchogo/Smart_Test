package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.domain.dto.GetTestResultsDTO
import com.charlesmuchogo.research.domain.dto.login.GetClinicsDTO
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.login.LoginResponseDTO
import com.charlesmuchogo.research.domain.dto.login.RegistrationRequestDTO
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsDTO
import com.charlesmuchogo.research.domain.dto.results.UploadTestResultsResponse
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsDTO
import com.charlesmuchogo.research.domain.dto.updateUser.UpdateUserDetailsResponseDTO
import com.charlesmuchogo.research.presentation.utils.Results
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun login(loginRequestDTO: LoginRequestDTO): Flow<Results<LoginResponseDTO>>
    suspend fun signUp(registrationRequestDTO: RegistrationRequestDTO): Flow<Results<LoginResponseDTO>>
    suspend fun completeRegistration(detailsDTO: UpdateUserDetailsDTO): Flow<Results<UpdateUserDetailsResponseDTO>>
    suspend fun fetchTestResults(): Flow<Results<GetTestResultsDTO>>
    suspend fun fetchClinics(): Flow<Results<GetClinicsDTO>>

    suspend fun uploadResults(results: UploadTestResultsDTO): Flow<Results<UploadTestResultsResponse>>
}
