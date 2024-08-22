package com.diontambz.storyapp.view.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.diontambz.storyapp.data.model.User
import com.diontambz.storyapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = repository.addStory(token, file, description, lat, lon)

    fun getUser(): LiveData<User> {
        return repository.getUserData()
    }
}