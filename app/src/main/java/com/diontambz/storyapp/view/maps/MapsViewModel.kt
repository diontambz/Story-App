package com.diontambz.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.diontambz.storyapp.data.model.User
import com.diontambz.storyapp.data.repository.StoryRepository

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStoryLocation(token: String) = repository.getStoryLocation(token)

    fun getUser(): LiveData<User> {
        return repository.getUserData()
    }
}