package com.pizzy.ptilms.ui.profile

import android.net.Uri

sealed class ProfileFormEvent {
    data class UsernameChanged(val username: String) : ProfileFormEvent()
    data class PhoneNumberChanged(val phoneNumber: String) : ProfileFormEvent()
    data class DateOfBirthChanged(val dateOfBirth: String) : ProfileFormEvent()
    data class SexChanged(val sex: String) : ProfileFormEvent()
    data class RoleChanged(val role: String) : ProfileFormEvent()
    data class ProfilePictureChanged(val uri: Uri?) : ProfileFormEvent()
    object Submit : ProfileFormEvent()
}