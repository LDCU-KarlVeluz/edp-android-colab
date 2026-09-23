package com.liceo.account.data

import com.liceo.account.core.AppResult
import com.liceo.account.data.network.NetworkModule
import com.liceo.account.data.network.UserApiService
import com.liceo.account.data.network.dto.NewUserDto
import com.liceo.account.data.network.dto.UserDto
import com.liceo.account.data.network.dto.toDomain
import com.liceo.account.domain.model.User
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class UserRepository(
    private val api: UserApiService = NetworkModule.userApi
) {
    // GIVEN (read it, do not change it)
    // MockAPI may answer 404 instead of [] when a search finds nobody.
    // This turns that 404 into an empty list, so "nobody found" is not an error.
    private suspend fun findUsers(email: String): List<UserDto> =
        try {
            api.findByEmail(email)
        } catch (e: HttpException) {
            if (e.code() == 404) emptyList() else throw e
        }

    // GIVEN (read it, do not change it)
    // Runs your block and turns every network exception into a named Failure.
    // The LAST line of your block is the result that comes back.
    private inline fun <T> safeCall(block: () -> AppResult<T>): AppResult<T> =
        try {
            block()
        } catch (e: UnknownHostException) {
            AppResult.Failure.NoInternet
        } catch (e: SocketTimeoutException) {
            AppResult.Failure.Timeout
        } catch (e: HttpException) {
            AppResult.Failure.Unknown("Server error ${e.code()}")
        } catch (e: SerializationException) {
            AppResult.Failure.Unknown("The server sent data we could not read.")
        } catch (e: IOException) {
            AppResult.Failure.NoInternet
        }

    // Finds the user with this email, then checks the password.
    suspend fun login(email: String, password: String): AppResult<User> = safeCall {
        val matches = findUsers(email.trim())
        val found = matches.firstOrNull {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        }
        if (found == null) AppResult.Failure.WrongLogin else AppResult.Success(found.toDomain())
    }

    // Creates the account — but only if nobody already uses this email.
    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        birthdate: String
    ): AppResult<User> = safeCall {
        val taken = findUsers(email.trim()).any { it.email.equals(email.trim(), ignoreCase = true) }
        if (taken) {
            AppResult.Failure.EmailTaken
        } else {
            val saved = api.createUser(
                NewUserDto(
                    fullname = fullName.trim(),
                    email = email.trim(),
                    password = password,
                    birthdate = birthdate.trim()
                )
            )
            AppResult.Success(saved.toDomain())
        }
    }
}
