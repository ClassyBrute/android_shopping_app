package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.gd.intership.hdziedziura.androidshopingapp.R
import com.gd.intership.hdziedziura.androidshopingapp.databinding.FragmentLoginBinding
import com.gd.intership.hdziedziura.androidshopingapp.domain.validation.ValidationResult
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.navigate
import com.gd.intership.hdziedziura.androidshopingapp.presentation.ext.viewModels
import com.gd.intership.hdziedziura.androidshopingapp.presentation.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
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
            inflater, R.layout.fragment_login, container, false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validationEvents.collectLatest {
                    when (it) {
                        is LoginViewModel.ValidationEvents.NameValidationEvent ->
                            validateName(it.validationResult)
                        is LoginViewModel.ValidationEvents.PasswordValidationEvent ->
                            validatePassword(it.validationResult)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest {
                    when (it) {
                        is LoginViewModel.Event.NavigateToForgotPasswordEvent ->
                            navigateToForgotPassword()
                        is LoginViewModel.Event.NavigateToHomeEvent ->
                            navigateToMain()
                        is LoginViewModel.Event.InvalidCredentialsEvent ->
                            invalidCredentials()
                    }
                }
            }
        }

        binding.vm = viewModel

        return binding.root
    }

    private fun navigateToForgotPassword() {
        navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
    }

    private fun navigateToMain() {
        findNavController().popBackStack()
        findNavController().popBackStack()
    }

    private fun invalidCredentials() {
        Toast.makeText(context, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
    }

    private fun validateName(validationResult: ValidationResult) {
        binding.userNameParent.isErrorEnabled = !validationResult.isValid
        binding.userNameParent.error = validationResult.errorMessage
        binding.userNameParent.isEndIconVisible = validationResult.isValid
        binding.userNameParent.endIconMode = TextInputLayout.END_ICON_CUSTOM
    }

    private fun validatePassword(validationResult: ValidationResult) {
        binding.userPasswordParent.isErrorEnabled = !validationResult.isValid
        binding.userPasswordParent.error = validationResult.errorMessage
        binding.userPasswordParent.isEndIconVisible = validationResult.isValid
        binding.userPasswordParent.endIconMode = TextInputLayout.END_ICON_CUSTOM
    }
}
