package com.charlesmuchogo.research.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.dto.login.LoginRequestDTO
import com.charlesmuchogo.research.domain.dto.login.LoginResponseDTO
import com.charlesmuchogo.research.domain.events.AuthenticationEvent
import com.charlesmuchogo.research.domain.models.User
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import com.charlesmuchogo.research.presentation.utils.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel
    @Inject
    constructor(
        private val database: AppDatabase,
        private val remoteRepository: RemoteRepository,
    ) : ViewModel() {
        val loginStatus =  MutableStateFlow(
                Results<LoginResponseDTO>(
                    data = null,
                    message = null,
                    status = ResultStatus.INITIAL,
                ),
            )

        val profileStatus =
            MutableStateFlow(
                Results<User>(
                    data = null,
                    message = null,
                    status = ResultStatus.INITIAL,
                ),
            )


    val authenticationEventState = MutableStateFlow(
        Results(
            status = ResultStatus.INITIAL,
            data = AuthenticationEvent(),
            message = null
        )
    )

    fun updateAuthenticationEvent(authenticationEvent: AuthenticationEvent) {
        authenticationEventState.value = Results.initial()
        authenticationEventState.value = Results.success(authenticationEvent)
    }

    init {
            getCurrentUser()
        }

        fun login(loginRequestDTO: LoginRequestDTO) {
            viewModelScope.launch {
                loginStatus.value = Results.loading()
                remoteRepository.login(loginRequestDTO).collect { results ->
                    results.data?.let { result ->
                        database.userDao().insertUser(user = result.user.copy(token = result.token))
                    }
                    loginStatus.value = results
                }
            }
        }

        fun getCurrentUser() {
            viewModelScope.launch {
                profileStatus.value = Results.loading()
                database
                    .userDao()
                    .getUser()
                    .catch {
                        profileStatus.value = Results.error()
                    }.collect {
                        profileStatus.value = Results.success(it)
                    }
            }
        }

        fun logout() {
            viewModelScope.launch {
                database.userDao().deleteUsers()
                database.testResultsDao().deleteResults()
            }
        }
    }
