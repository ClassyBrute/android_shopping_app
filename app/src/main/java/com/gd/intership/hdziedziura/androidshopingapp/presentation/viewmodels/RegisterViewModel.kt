package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels

import androidx.databinding.ObservableField
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.common.Resource
import com.gd.intership.hdziedziura.androidshopingapp.data.network.dto.UserDto
import com.gd.intership.hdziedziura.androidshopingapp.data.storage.DataKeys
import com.gd.intership.hdziedziura.androidshopingapp.data.storage.DataStoreManager
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.RegisterUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStatus
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.UserStream
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.EmailValidationUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.NameValidationUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.PasswordValidationUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.ValidationResult
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private var registerUseCase: RegisterUseCase,
    private var dataStoreManager: DataStoreManager,
    private var passwordValidationUseCase: PasswordValidationUseCase,
    private var nameValidationUseCase: NameValidationUseCase,
    private var emailValidationUseCase: EmailValidationUseCase,
    private var userStream: UserStream
) : ViewModel() {
    val events = MutableSharedFlow<Event>()
    val validationEvents = MutableSharedFlow<ValidationEvents>()
    var name = ObservableField("mor_2314")
    var email = ObservableField("mor_2314@")
    var password = ObservableField("83r5^_")
    private var isNameValid = false
    private var isEmailValid = false
    private var isPasswordValid = false

    private val disposables = CompositeDisposable()

    fun onRegisterClick() {
        validateName()
        validateEmail()
        validatePassword()
        if (isNameValid && isEmailValid && isPasswordValid) {
            val userDto = UserDto(
                username = name.get()!!,
                email = email.get()!!,
                password = password.get()!!
            )

            viewModelScope.launch {
                when (val token = registerUseCase.execute(userDto)) {
                    is Resource.Success -> {
                        val saveJob = async {
                            saveUser(userDto, token.data!!)
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
        dataStoreManager.writeToDataStore(stringPreferencesKey(DataKeys.email), userDto.email)
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

    fun validateEmail() {
        val validationResult = emailValidationUseCase.execute(email.get())
        isEmailValid = validationResult.isValid

        viewModelScope.launch {
            validationEvents.emit(
                ValidationEvents.EmailValidationEvent(
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

    fun onGoToLoginButtonClick() {
        viewModelScope.launch {
            events.emit(Event.NavigateToLoginEvent)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    sealed class Event {
        object NavigateToLoginEvent : Event()
        object NavigateToHomeEvent : Event()
        object InvalidCredentialsEvent : Event()
    }

    sealed class ValidationEvents {
        data class NameValidationEvent(val validationResult: ValidationResult) : ValidationEvents()
        data class EmailValidationEvent(val validationResult: ValidationResult) : ValidationEvents()
        data class PasswordValidationEvent(val validationResult: ValidationResult) :
            ValidationEvents()
    }
}
