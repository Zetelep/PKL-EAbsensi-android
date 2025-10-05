package com.zulfa.eabsensi.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.domain.model.LoginDomain
import com.zulfa.eabsensi.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<LoginDomain>>(Resource.Loading())
    val loginState: StateFlow<Resource<LoginDomain>> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            authUseCase.login(username, password).collect { result ->
                _loginState.value = result
            }
        }
    }
}