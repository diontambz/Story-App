package com.diontambz.storyapp.data

import com.diontambz.storyapp.data.network.api.ApiService
import com.diontambz.storyapp.data.network.response.BaseResponse
import com.diontambz.storyapp.data.network.response.LoginResponse
import com.diontambz.storyapp.data.network.response.StoryResponse
import com.diontambz.storyapp.data.request.LoginRequest
import com.diontambz.storyapp.data.request.RegisterRequest
import com.diontambz.storyapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyStoryResponse = DataDummy.generateDummyStoryLocation()
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyAddStoryResponse = DataDummy.generateDummyAddStoryResponse()

    override suspend fun register(request: RegisterRequest): BaseResponse {
        return dummyRegisterResponse
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun getStory(token: String, page: Int, size: Int): StoryResponse {
        return dummyStoryResponse
    }

    override suspend fun getStoryLocation(
        token: String,
        location: Int,
    ): StoryResponse {
        return dummyStoryResponse
    }

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): BaseResponse {
        return dummyAddStoryResponse
    }
}