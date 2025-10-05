package com.zulfa.eabsensi

import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.data.remote.response.User
import com.zulfa.eabsensi.core.domain.model.LoginDomain
import com.zulfa.eabsensi.core.domain.model.UserDomain
import com.zulfa.eabsensi.core.domain.usecase.AuthUseCase
import com.zulfa.eabsensi.presentation.auth.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authUseCase: AuthUseCase
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authUseCase = mock(AuthUseCase::class.java)
        viewModel = LoginViewModel(authUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success updates state with success`() = runTest {
        // Given
        val fakeResult = LoginDomain(
            "123", "Zulfa",
            user = UserDomain(
                "1", "zulfa", "Zulfa",
                role = "1",
            )
        )
        `when`(authUseCase.login("user", "pass"))
            .thenReturn(flowOf(Resource.Success(fakeResult)))

        // When
        viewModel.login("user", "pass")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.loginState.value
        assert(state is Resource.Success && state.data == fakeResult)
        verify(authUseCase).login("user", "pass")
    }

    @Test
    fun `login failure updates state with error`() = runTest {
        // Given
        val errorMessage = "Invalid credentials"
        `when`(authUseCase.login("wrong", "wrong"))
            .thenReturn(flowOf(Resource.Error(errorMessage)))

        // When
        viewModel.login("wrong", "wrong")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.loginState.value
        assert(state is Resource.Error && state.message == errorMessage)
        verify(authUseCase).login("wrong", "wrong")
    }
}
