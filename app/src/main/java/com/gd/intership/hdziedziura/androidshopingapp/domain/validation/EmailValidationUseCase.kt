package com.gd.intership.hdziedziura.androidshopingapp.domain.validation

import android.content.res.Resources
import com.gd.intership.hdziedziura.androidshopingapp.R
import javax.inject.Inject

class EmailValidationUseCase @Inject constructor(private var resources: Resources) {

    fun execute(email: String?): ValidationResult {

        val isEmailValid = !email.isNullOrEmpty() && email.contains("@")
        val error = if (isEmailValid) {
            ""
        } else {
            resources.getString(R.string.email_error)
        }

        return ValidationResult(isEmailValid, error)
    }
}
