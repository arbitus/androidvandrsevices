package com.example.vandrservices.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vandrservices.domain.model.User
import com.example.vandrservices.domain.repository.UserRepository
import com.example.vandrservices.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    // Exponemos el Flow de usuarios
    val usersFlow: Flow<List<User>> = getUserUseCase()

    // Login normal
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = false
            val token = userRepository.login(username, password)
            Log.d("LoginViewModel", "Token: $token")
            if (token != "") {
                _loginResult.value = true
            }
            _token.value = userRepository.getToken()
            userRepository.addUser(
                User(
                    userId = UUID.randomUUID().toString(),
                    name = username,
                    password = password,
                    token = token
                )
            )
        }
    }

    // Actualiza el token del primer usuario (único usuario)
    fun updateFirstUserToken(token: String) {
        viewModelScope.launch {
            val firstUser = usersFlow.firstOrNull()?.firstOrNull()
            if (firstUser != null) {
                val updatedUser = firstUser.copy(token = token)
                userRepository.updateUserToken(updatedUser)
            }
        }
    }
    suspend fun loginAuto(username: String, password: String): Boolean {
        var result = false
        val token = userRepository.login(username, password)
        Log.d("LoginViewModel", "Token: $token")
        if (token != "") {
            _token.value = token
            result = true
        }
        _token.postValue(userRepository.getToken())
        return result
    }
}