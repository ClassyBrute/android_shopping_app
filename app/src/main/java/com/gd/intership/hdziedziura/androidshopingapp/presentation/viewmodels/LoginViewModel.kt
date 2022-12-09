package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels

import androidx.databinding.ObservableField
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import com.gd.intership.hdziedziura.androidshopingapp.data.storage.DataKeys
import com.gd.intership.hdziedziura.androidshopingapp.data.storage.DataStoreManager
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.LoginUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStatus
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStream
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.NameValidationUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.PasswordValidationUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.ValidationResult
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private var dataStoreManager: DataStoreManager,
    private var nameValidationUseCase: NameValidationUseCase,
    private var passwordValidationUseCase: PasswordValidationUseCase,
    private var userStream: UserStream
) : ViewModel() {
    val events = MutableSharedFlow<Event>()
    val validationEvents = MutableSharedFlow<ValidationEvents>()
    val name = ObservableField("")
    val password = ObservableField("")
    private var isNameValid = false
    private var isPasswordValid = false

    private val disposables = CompositeDisposable()

    fun onLoginClick() {
        validateName()
        validatePassword()
        if (isNameValid && isPasswordValid) {
            val userDto = UserDto(
                username = name.get()!!,
                password = password.get()!!
            )

            viewModelScope.launch {
                when (val token = loginUseCase.execute(userDto)) {
                    is Resource.Success -> {
                        val saveJob = async {
                            saveUser(userDto, token.data?.token!!)
                        }
                        saveJob.await()
                        userStream.emit(UserStatus.Authorized)
                        events.emit(Event.NavigateToHomeEvent)
                    }
                    is Resource.Error -> {
                        events.emit(Event.InvalidCredentialsEvent)
                    }
                    else -> {}
                }
            }
        }
    }

    private suspend fun saveUser(userDto: UserDto, token: String) {
        dataStoreManager.writeToDataStore(stringPreferencesKey(DataKeys.username), userDto.username)
        dataStoreManager.writeToDataStore(stringPreferencesKey(DataKeys.password), userDto.password)
        dataStoreManager.writeToDataStore(stringPreferencesKey(DataKeys.token), token)
    }

    fun validateName() {
        val validationResult = nameValidationUseCase.execute(name.get())
        isNameValid = validationResult.isValid

        viewModelScope.launch {
            validationEvents.emit(
                ValidationEvents.NameValidationEvent(
                    validationResult
                )
            )
        }
    }

    fun validatePassword() {
        val validationsResult = passwordValidationUseCase.execute(password.get())
        isPasswordValid = validationsResult.isValid

        viewModelScope.launch {
            validationEvents.emit(
                ValidationEvents.PasswordValidationEvent(
                    validationsResult
                )
            )
        }
    }

    fun onGoToForgotPasswordButtonClick() {
        viewModelScope.launch {
            events.emit(Event.NavigateToForgotPasswordEvent)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    sealed class Event {
        object NavigateToForgotPasswordEvent : Event()
        object NavigateToHomeEvent : Event()
        object InvalidCredentialsEvent : Event()
    }

    sealed class ValidationEvents {
        data class NameValidationEvent(val validationResult: ValidationResult) : ValidationEvents()
        data class PasswordValidationEvent(val validationResult: ValidationResult) : ValidationEvents()
    }
}
