package com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gd.intership.hdziedziura.androidshopingapp.domain.use_case.user.ForgotPasswordUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.EmailValidationUseCase
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.ValidationResult
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(
    private var forgotPasswordUseCase: ForgotPasswordUseCase,
    private var emailValidationUseCase: EmailValidationUseCase
) : ViewModel() {
    val validationEvents = MutableSharedFlow<ValidationEvents>()
    val email = ObservableField("")
    private var isEmailValid = false

    private val disposables = CompositeDisposable()

    fun onSendClick() {
        validateEmail()
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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    sealed class Event

    sealed class ValidationEvents {
        data class EmailValidationEvent(val validationResult: ValidationResult) : ValidationEvents()
    }
}
