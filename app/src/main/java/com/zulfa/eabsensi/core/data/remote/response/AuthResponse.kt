package com.zulfa.eabsensi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("username")
    val username: String
)

data class LoginResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("token")
    val token: String
)

data class LoginRequest(
    val username: String,
    val password: String
)