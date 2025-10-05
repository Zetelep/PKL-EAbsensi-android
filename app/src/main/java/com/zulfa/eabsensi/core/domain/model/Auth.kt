package com.zulfa.eabsensi.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDomain(
    val id: String,
    val name: String,
    val username: String,
    val role: String
): Parcelable

@Parcelize
data class LoginDomain(
    val message: String,
    val token: String,
    val user: UserDomain
): Parcelable