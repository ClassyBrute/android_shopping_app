package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentForgotPasswordBinding
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.ValidationResult
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.ForgotPasswordViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ForgotPasswordFragment : BaseFragment() {

    private val viewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var binding: FragmentForgotPasswordBinding
    override val showToolbar: Boolean
        get() = true
    override val showBottomNavBar: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_forgot_password, container, false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validationEvents.collectLatest {
                    when (it) {
                        is ForgotPasswordViewModel.ValidationEvents.EmailValidationEvent ->
                            validateEmail(it.validationResult)
                    }
                }
            }
        }

        binding.vm = viewModel

        return binding.root
    }

    private fun validateEmail(validationResult: ValidationResult) {
        binding.userEmailParent.isErrorEnabled = !validationResult.isValid
        binding.userEmailParent.error = validationResult.errorMessage
        binding.userEmailParent.isEndIconVisible = validationResult.isValid
        binding.userEmailParent.endIconMode = TextInputLayout.END_ICON_CUSTOM
    }
}
