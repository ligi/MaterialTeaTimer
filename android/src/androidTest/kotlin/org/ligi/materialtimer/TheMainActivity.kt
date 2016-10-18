package org.ligi.materialtimer

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
import android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.ligi.materialteatimer.MainActivity
import org.ligi.materialteatimer.R


class TheMainActivity {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        activityTestRule.activity.runOnUiThread { activityTestRule.activity.window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_DISMISS_KEYGUARD) }
    }

    @Test
    fun thatActivityShouldLaunch() {
        ScreenShotTaker.takeScreenShot(activityTestRule.activity,"main")
    }

    @Test
    fun thatInfoOpens() {
        onView(withId(R.id.menuInfo)).perform(click())

        ScreenShotTaker.takeScreenShot(activityTestRule.activity,"info")
    }
}
