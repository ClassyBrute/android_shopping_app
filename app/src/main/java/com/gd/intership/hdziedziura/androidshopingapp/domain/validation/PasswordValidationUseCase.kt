package com.gd.intership.hdziedziura.androidshopingapp.domain.validation

import android.content.res.Resources
import com.gd.intership.hdziedziura.androidshopingapp.R
import javax.inject.Inject

class PasswordValidationUseCase @Inject constructor(private var resources: Resources) {

    fun execute(password: String?): ValidationResult {
        val isPasswordValid = !password.isNullOrEmpty() &&
            password.length >= 4 &&
            password.contains("\\d".toRegex())
        val error = if (isPasswordValid) {
            ""
        } else {
            resources.getString(R.string.password_error)
        }

        return ValidationResult(isPasswordValid, error)
    }
}
