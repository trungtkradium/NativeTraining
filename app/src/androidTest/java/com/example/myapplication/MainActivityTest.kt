package com.example.myapplication

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
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
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo())).check(
            matches(hasDescendant(withText(title))))
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo())).check(
            matches(hasDescendant(withText(description))))
        onView(withId(R.id.check_box_completed)).check(matches(isNotChecked()))

        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
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
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo())).check(
            matches(hasDescendant(withText(title))))
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo())).check(
            matches(hasDescendant(withText(description))))
        onView(withId(R.id.check_box_completed)).check(matches(isNotChecked()))
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        title = "$title Update"
        description = "$description Update"
        onView(withId(R.id.add_task_title_edit_text)).perform(ViewActions.replaceText(title))
        onView(withId(R.id.add_task_description_edit_text)).perform(
            ViewActions.replaceText(
                description
            )
        )
        onView(withId(R.id.check_box_completed_edit_task)).perform(click())
        onView(withId(R.id.save_task_fab)).perform(click())

        onView(isRoot()).perform(waitFor(500))
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo())).check(
            matches(hasDescendant(withText(title))))
        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo())).check(
            matches(hasDescendant(withText(description))))
        onView(withId(R.id.check_box_completed)).check(matches(isChecked()))

        onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.delete_task)).perform(click())
    }
}

fun waitFor(delay: Long): ViewAction? {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = isRoot()
        override fun getDescription(): String = "wait for $delay milliseconds"
        override fun perform(uiController: UiController, v: View?) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }
}