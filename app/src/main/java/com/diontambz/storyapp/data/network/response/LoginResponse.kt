package com.diontambz.storyapp.data.network.response

import com.diontambz.storyapp.data.model.Login
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("loginResult") val loginResult: Login?,
)
