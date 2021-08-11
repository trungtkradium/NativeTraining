package com.example.myapplication

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
internal class MainActivityTest {

    lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }


    @Test
    fun testAddNewTask() {
        onView(withId(R.id.add_task_fab)).perform(click())
        val title = "New Tittle Todo ${UUID.randomUUID()}"
        val description = "New Description Todo ${UUID.randomUUID()}"
        onView(withId(R.id.add_task_title_edit_text)).perform(ViewActions.typeText(title))
        onView(withId(R.id.add_task_description_edit_text)).perform(ViewActions.typeText(description))
        onView(withId(R.id.add_task_description_edit_text)).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.save_task_fab)).perform(click())

        onView(withText(title)).check(matches(isDisplayed()))
        onView(withText(description)).check(matches(isDisplayed()))
        onView(withText(title)).perform(click())
        onView(withId(R.id.delete_task)).perform(click())
    }

    @Test
    fun testViewTaskTypePage() {
        onView(withText(R.string.all_task_title)).check(matches(isDisplayed()))

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.completed_task_title)).perform((click()))
        onView(withText(R.string.completed_task_title)).check(matches(isDisplayed()))

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.incomplete_task_title)).perform((click()))
        onView(withText(R.string.incomplete_task_title)).check(matches(isDisplayed()))
    }

    @Test
    fun testUpdateTask() {
        onView(withId(R.id.add_task_fab)).perform(click())
        var title = "New Tittle Todo ${UUID.randomUUID()}"
        var description = "New Description Todo ${UUID.randomUUID()}"
        onView(withId(R.id.add_task_title_edit_text)).perform(ViewActions.typeText(title))
        onView(withId(R.id.add_task_description_edit_text)).perform(ViewActions.typeText(description))
        onView(withId(R.id.add_task_description_edit_text)).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.save_task_fab)).perform(click())
        onView(withText(title)).check(matches(isDisplayed()))
        onView(withText(description)).check(matches(isDisplayed()))
        onView(withText(title)).perform(click())
        
        title = "$title Update"
        description = "$description Update"
        onView(withId(R.id.add_task_title_edit_text)).perform(ViewActions.replaceText(title))
        onView(withId(R.id.add_task_description_edit_text)).perform(ViewActions.replaceText(description))
        onView(withId(R.id.check_box_completed_edit_task)).perform(click())
        onView(withId(R.id.save_task_fab))
        onView(withText(title)).check(matches(isDisplayed()))
        onView(withText(description)).check(matches(isDisplayed()))
        onView(withText(title)).perform(click())
        onView(withId(R.id.delete_task)).perform(click())
    }
}