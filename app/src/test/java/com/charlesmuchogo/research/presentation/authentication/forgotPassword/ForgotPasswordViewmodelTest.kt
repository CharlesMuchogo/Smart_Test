package com.charlesmuchogo.research.presentation.authentication.forgotPassword

import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.domain.dto.authentication.ForgotPasswordResponse
import com.charlesmuchogo.research.presentation.utils.Results
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.charlesmuchogo.research.presentation.utils.ResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ForgotPasswordViewmodelTest {

    private lateinit var viewModel: ForgotPasswordViewmodel
    private val remoteRepository: RemoteRepository = mock()
    private val snackBarViewModel: SnackBarViewModel = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ForgotPasswordViewmodel(remoteRepository, snackBarViewModel)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onAction(OnUpdateEmail) should update email in state`() {
        val newEmail = "test@example.com"
        viewModel.onAction(ForgotPasswordAction.OnUpdateEmail(newEmail))
        assert(viewModel.state.value.email == newEmail)
    }

    @Test
    fun `onAction(OnSubmit) with invalid email should show email error`() = runTest {
        viewModel.onAction(ForgotPasswordAction.OnUpdateEmail("invalid-email"))
        viewModel.onAction(ForgotPasswordAction.OnSubmit)
        assert(viewModel.state.value.emailError != null)
    }

    @Test
    fun `onAction(OnSubmit) with valid email and success response should show success`() = runTest {
        val email = "test@example.com"
        val response = Results(data = ForgotPasswordResponse("Success"), message = null, status = ResultStatus.INITIAL)
        whenever(remoteRepository.forgotPassword(any())).thenReturn(response)

        viewModel.onAction(ForgotPasswordAction.OnUpdateEmail(email))
        viewModel.onAction(ForgotPasswordAction.OnSubmit)

        testScheduler.advanceUntilIdle() 

        assert(viewModel.state.value.hasSubmittedEmail)
        verify(snackBarViewModel).sendEvent(any())
    }

    @Test
    fun `onAction(OnSubmit) with valid email and error response should show error`() = runTest {
        val email = "test@example.com"
        val response = Results(data = null, message = "Error", status = ResultStatus.INITIAL)
        whenever(remoteRepository.forgotPassword(any())).thenReturn(response)

        viewModel.onAction(ForgotPasswordAction.OnUpdateEmail(email))
        viewModel.onAction(ForgotPasswordAction.OnSubmit)

        testScheduler.advanceUntilIdle()

        assert(!viewModel.state.value.hasSubmittedEmail)
        verify(snackBarViewModel).sendEvent(any())
    }
}