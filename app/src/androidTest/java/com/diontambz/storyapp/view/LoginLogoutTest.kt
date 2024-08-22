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
import com.diontambz.storyapp.view.auth.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginLogoutTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun loginLogoutTest() {
        onView(withId(R.id.et_email)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.et_pass)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_menu)).perform(click())
        onView(withId(R.id.fab_log_out)).perform(click())
    }
}