package com.gd.intership.hdziedziura.androidshopingapp.domain.validation

import android.content.res.Resources
import com.gd.intership.hdziedziura.androidshopingapp.R
import javax.inject.Inject

class NameValidationUseCase @Inject constructor(private var resources: Resources) {

    fun execute(name: String?): ValidationResult {
        val isNameValid = !name.isNullOrEmpty() && name.length >= 4
        val error = if (isNameValid) {
            ""
        } else {
            resources.getString(R.string.name_error)
        }

        return ValidationResult(isNameValid, error)
    }
}
