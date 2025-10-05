package com.zulfa.eabsensi.presentation.main.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.core.data.pref.UserPreference
import com.zulfa.eabsensi.core.domain.model.HistoryAttendanceDomain
import com.zulfa.eabsensi.core.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
        private val authUseCase: AuthUseCase,
        private val userPreference: UserPreference
) : ViewModel() {
        private val _historyState = MutableStateFlow<Resource<List<HistoryAttendanceDomain>>>(Resource.Loading())
        val historyState: StateFlow<Resource<List<HistoryAttendanceDomain>>> = _historyState

        private val _logoutState = MutableStateFlow(false)
        val logoutState: StateFlow<Boolean> = _logoutState

        fun getAttendanceHistory() {
            viewModelScope.launch {
                authUseCase.getHistoryAttendance()
                    .collect { result ->
                        _historyState.value = result
                    }
            }
        }

        fun logout() {
            viewModelScope.launch {
                userPreference.logout()
                _logoutState.value = true
            }
        }
}