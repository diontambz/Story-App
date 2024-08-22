package com.diontambz.storyapp.view.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.diontambz.storyapp.data.Result
import com.diontambz.storyapp.data.network.response.StoryResponse
import com.diontambz.storyapp.data.repository.StoryRepository
import com.diontambz.storyapp.utils.DataDummy
import com.diontambz.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStoryLocation = DataDummy.generateDummyStoryLocation()
    private val token = "TOKEN"

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when get Story Location is success`() {
        val expectedStory = MutableLiveData<Result<StoryResponse>>()
        expectedStory.value = Result.Success(dummyStoryLocation)

        Mockito.`when`(storyRepository.getStoryLocation(token)).thenReturn(expectedStory)

        val actualStory = mapsViewModel.getStoryLocation(token).getOrAwaitValue()
        Mockito.verify(storyRepository).getStoryLocation(token)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
    }
}