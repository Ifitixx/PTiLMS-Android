package com.pizzy.ptilms.ui.profile

import android.net.Uri
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class ProfileFormState(
    val username: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val sex: String = "",
    val role: String = "",
    val profilePictureUri: Uri? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    fun isValid(): Boolean {
        return username.isNotBlank() && phoneNumber.isNotBlank() && dateOfBirth.isNotBlank() && sex.isNotBlank()
    }

    fun parsedDateOfBirth(): LocalDate? {
        return try {
            LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_DATE)
        } catch (e: DateTimeParseException) {
            null
        }
    }
}

fun ProfileFormState.updateState(event: ProfileFormEvent): ProfileFormState {
    return when (event) {
        is ProfileFormEvent.UsernameChanged -> copy(username = event.username)
        is ProfileFormEvent.PhoneNumberChanged -> copy(phoneNumber = event.phoneNumber)
        is ProfileFormEvent.DateOfBirthChanged -> copy(dateOfBirth = event.dateOfBirth)
        is ProfileFormEvent.SexChanged -> copy(sex = event.sex)
        is ProfileFormEvent.RoleChanged -> copy(role = event.role)
        is ProfileFormEvent.ProfilePictureChanged -> copy(profilePictureUri = event.uri)
        ProfileFormEvent.Submit -> this
    }
}