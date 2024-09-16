package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.domain.dto.GetTestResultsDTO
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.login.LoginResponseDTO
import com.charlesmuchogo.research.domain.dto.login.RegistrationRequestDTO
import com.charlesmuchogo.research.presentation.utils.Results
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun login(loginRequestDTO: LoginRequestDTO): Flow<Results<LoginResponseDTO>>
    suspend fun signUp(registrationRequestDTO: RegistrationRequestDTO): Flow<Results<LoginResponseDTO>>
    suspend fun fetchTestResults(): Flow<Results<GetTestResultsDTO>>
}
