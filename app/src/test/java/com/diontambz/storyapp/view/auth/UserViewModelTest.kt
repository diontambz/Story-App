package com.diontambz.storyapp.view.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.diontambz.storyapp.data.Result
import com.diontambz.storyapp.data.network.response.BaseResponse
import com.diontambz.storyapp.data.network.response.LoginResponse
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
class UserViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var userViewModel: UserViewModel
    private val dummyLogin = DataDummy.generateDummyLoginResponse()
    private val dummyRegister = DataDummy.generateDummyRegisterResponse()

    private val name = "Name"
    private val email = "email@gmail.com"
    private val password = "password"

    @Before
    fun setUp() {
        userViewModel = UserViewModel(storyRepository)
    }

    @Test
    fun `when user Login is Success`() {
        val expectedUser = MutableLiveData<Result<LoginResponse>>()
        expectedUser.value = Result.Success(dummyLogin)
        Mockito.`when`(storyRepository.userLogin(email, password)).thenReturn(expectedUser)

        val actualUser = userViewModel.userLogin(email, password).getOrAwaitValue()

        Mockito.verify(storyRepository).userLogin(email, password)
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Success)
    }

    @Test
    fun `when user Register is Success`() {
        val expectedUser = MutableLiveData<Result<BaseResponse>>()
        expectedUser.value = Result.Success(dummyRegister)
        Mockito.`when`(storyRepository.userRegister(name, email, password)).thenReturn(expectedUser)

        val actualUser = userViewModel.userRegister(name, email, password).getOrAwaitValue()

        Mockito.verify(storyRepository).userRegister(name, email, password)
        Assert.assertNotNull(actualUser)
        Assert.assertTrue(actualUser is Result.Success)
    }
}