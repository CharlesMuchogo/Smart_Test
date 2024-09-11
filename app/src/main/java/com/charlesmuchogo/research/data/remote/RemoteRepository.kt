package com.charlesmuchogo.research.data.remote

import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.login.LoginResponseDTO
import com.charlesmuchogo.research.presentation.utils.Results
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun login(loginRequestDTO: LoginRequestDTO): Flow<Results<LoginResponseDTO>>
}