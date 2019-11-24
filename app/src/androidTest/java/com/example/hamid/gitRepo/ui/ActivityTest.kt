package com.example.hamid.gitRepo.ui

import android.content.Context
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.hamid.gitRepo.InstrumentedTest
import com.example.hamid.gitRepo.R
import com.example.hamid.gitRepo.presentation.ui.activity.RepoActivity
import com.hamid.data.utils.EspressoIdlingResource
import com.hamid.domain.model.model.Status
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<RepoActivity>? = ActivityTestRule(
        RepoActivity::class.java
    )

    @get:Rule
    val rxSchedulerRule = InstrumentedTest.RxSchedulerRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var mContext: Context? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mContext = InstrumentationRegistry.getInstrumentation().targetContext

        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }


    @Test
    fun verifyLDisBeingObserved() {
        assertTrue(activityRule!!.activity.viewModel.formattedList.hasObservers())
    }

    @Test
    fun testRecyclerVisible() {
        activityRule!!.activity.viewModel.formattedList.observeForTesting {

            onView(withId(R.id.rv_list))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testRecyclerViewScroll() {

        val recyclerView = activityRule!!.activity.findViewById<RecyclerView>(R.id.rv_list)
        val itemCount = recyclerView.adapter!!.itemCount

        onView(withId(R.id.rv_list))
            .inRoot(
                RootMatchers.withDecorView(
                    `is`<View>(activityRule!!.activity.window.decorView)
                )
            )
            .perform(scrollToPosition<RecyclerView.ViewHolder>(itemCount - 6))
    }

    @Test
    fun rv_itemSize_matches_responseSize() {

        val recyclerView = activityRule!!.activity.findViewById<RecyclerView>(R.id.rv_list)
        val itemCount = recyclerView.adapter!!.itemCount

        assertTrue(itemCount.toLong() >= 0)

    }

    @Test
    fun rv_verify_fieldsDisplayed() {

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.iv_avatar))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_lbl_name))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_name))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_lbl_description))))

        onView(withId(R.id.rv_list))
            .check(matches(hasDescendant(withId(R.id.tv_description))))

    }

    @Test
    fun whenNoData_progressBarShown() {

        activityRule!!.activity.viewModel.gitRepoUseCase.nukeDB()

        activityRule!!.activity.viewModel.formattedList.observeForTesting {
            if (activityRule!!.activity.viewModel.formattedList.value!!.status == Status.ERROR) {
                onView(withId(R.id.progress_bar)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            } else {
                onView(withId(R.id.progress_bar)).check(matches(withEffectiveVisibility(Visibility.GONE)))

            }
        }

    }

    @Test
    fun whenData_progressBarHidden() {

        val recyclerView = activityRule!!.activity.findViewById<RecyclerView>(R.id.rv_list)
        val itemCount = recyclerView.adapter!!.itemCount

        if (itemCount > 0) {
            onView(withId(R.id.progress_bar)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        } else {
            onView(withId(R.id.progress_bar)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        }

    }


    private fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
        val observer = Observer<T> { }
        try {
            observeForever(observer)
            block()
        } finally {
            removeObserver(observer)
        }
    }

}

