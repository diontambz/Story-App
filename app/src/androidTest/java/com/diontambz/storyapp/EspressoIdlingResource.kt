package com.diontambz.storyapp

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"
    private val mCountingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        mCountingIdlingResource.increment()
    }

    fun decrement() {
        if (!mCountingIdlingResource.isIdleNow) {
            mCountingIdlingResource.decrement()
        }
    }

    fun getEspressoIdlingResource(): IdlingResource {
        return mCountingIdlingResource
    }
}