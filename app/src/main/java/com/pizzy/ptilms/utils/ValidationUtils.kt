package com.pizzy.ptilms.utils

import com.pizzy.ptilms.auth.SignupForm

object ValidationUtils {
    private val PASSWORD_REGEX =
        Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    private val USERNAME_REGEX = Regex("^[a-zA-Z0-9_]{4,}$")
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@(.+)$")

    fun validateUsername(username: String): Resource<Unit> {
        return when {
            username.length < 4 -> Resource.Error("Username must be at least 4 characters")
            !USERNAME_REGEX.matches(username) ->
                Resource.Error("Username can only contain letters, numbers, and underscores")
            else -> Resource.Success(Unit)
        }
    }

    fun validatePassword(password: String): Resource<Unit> {
        return when {
            password.length < 8 -> Resource.Error("Password must be at least 8 characters")
            !PASSWORD_REGEX.matches(password) -> Resource.Error(
                "Password must contain at least one uppercase, lowercase, number, and special character"
            )
            else -> Resource.Success(Unit)
        }
    }

    fun validateEmail(email: String): Resource<Unit> {
        return if (!EMAIL_REGEX.matches(email)) {
            Resource.Error("Invalid email format")
        } else {
            Resource.Success(Unit)
        }
    }

    fun validateForm(form: SignupForm): Boolean {
        val usernameValidation = validateUsername(form.username)
        val passwordValidation = validatePassword(form.password)
        val emailValidation = validateEmail(form.email)

        return usernameValidation is Resource.Success &&
                passwordValidation is Resource.Success &&
                emailValidation is Resource.Success &&
                form.password == form.confirmPassword
    }
}

// Extension functions for easier use in composables
fun String.isValidEmail(): Boolean {
    return ValidationUtils.validateEmail(this) is Resource.Success
}

fun String.isValidPassword(): Boolean {
    return ValidationUtils.validatePassword(this) is Resource.Success
}

fun String.isValidUsername(): Boolean {
    return ValidationUtils.validateUsername(this) is Resource.Success
}

fun validateEmail(email: String): Boolean {
    return ValidationUtils.validateEmail(email) is Resource.Success
}

fun validateForm(form: SignupForm): Boolean {
    return ValidationUtils.validateForm(form)
}