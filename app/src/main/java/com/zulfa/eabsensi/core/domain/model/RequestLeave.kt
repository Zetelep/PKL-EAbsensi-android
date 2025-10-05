package com.zulfa.eabsensi.core.domain.model

data class RequestLeaveDomain(
    val id: String,
    val userId: String,
    val date: String,
    val notes: String,
    val status: String,
    val message: String
)