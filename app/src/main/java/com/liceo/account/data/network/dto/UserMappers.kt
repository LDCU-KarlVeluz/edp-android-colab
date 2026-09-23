package com.liceo.account.data.network.dto

import com.liceo.account.domain.model.User

// Turns the server's messy shape into the app's clean shape.
fun UserDto.toDomain(): User = User(
    id = id ?: "",
    fullName = fullname?.trim() ?: "(no name)",
    email = email?.trim() ?: "",
    birthdate = birthdate ?: "(not set)"
)
