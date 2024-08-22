package com.diontambz.storyapp.utils

import com.diontambz.storyapp.data.model.Login
import com.diontambz.storyapp.data.model.Story
import com.diontambz.storyapp.data.network.response.BaseResponse
import com.diontambz.storyapp.data.network.response.LoginResponse
import com.diontambz.storyapp.data.network.response.StoryResponse

object DataDummy {

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(
            false, "token", Login(
                "id", "name", "token"
            )
        )
    }

    fun generateDummyRegisterResponse(): BaseResponse {
        return BaseResponse(
            false, "success"
        )
    }

    fun generateDummyStoryWithSize(size: Int): List<Story> {
        val item = arrayListOf<Story>()

        for (i in 0 until size) {
            val story = Story(
                "story-50aWewCMHWO1I9Hm",
                "NANANG",
                "trs",
                "https://story-api.dicoding.dev/images/stories/photos-1697550345487_8jKv4fbM.jpg",
                "2023-10-17T13:45:45.488Z",
                -7.35766,
                112.6798683
            )
            item.add(story)
        }
        return item
    }

    fun generateDummyStory(): List<Story> {
        val item = arrayListOf<Story>()

        for (i in 0 until 10) {
            val story = Story(
                "story-50aWewCMHWO1I9Hm",
                "NANANG",
                "trs",
                "https://story-api.dicoding.dev/images/stories/photos-1697550345487_8jKv4fbM.jpg",
                "2023-10-17T13:45:45.488Z",
                -7.35766,
                112.6798683
            )
            item.add(story)
        }
        return item
    }

    fun generateDummyStoryLocation(): StoryResponse {
        val item: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "story-50aWewCMHWO1I9Hm",
                "NANANG",
                "trs",
                "https://story-api.dicoding.dev/images/stories/photos-1697550345487_8jKv4fbM.jpg",
                "2023-10-17T13:45:45.488Z",
                -7.35766,
                112.6798683
            )
            item.add(story)
        }
        return StoryResponse(
            false, "success", item
        )
    }

    fun generateDummyAddStoryResponse(): BaseResponse {
        return BaseResponse(
            false, "success"
        )
    }
}