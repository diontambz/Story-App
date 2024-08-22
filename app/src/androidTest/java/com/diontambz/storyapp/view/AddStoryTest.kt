package com.diontambz.storyapp.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.diontambz.storyapp.EspressoIdlingResource
import com.diontambz.storyapp.R
import com.diontambz.storyapp.view.add_story.AddStoryActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AddStoryTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(AddStoryActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun addStoryTest() {
        onView(withId(R.id.btn_gallery)).perform(click())
        onView(withId(R.id.switch_location)).perform(click())
        onView(withId(R.id.et_desc)).perform(typeText("Ini adalah cerita"), closeSoftKeyboard())
        onView(withId(R.id.btn_upload)).perform(click())
        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
    }
}