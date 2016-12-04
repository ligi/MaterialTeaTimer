package org.ligi.materialtimer

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import org.junit.Rule
import org.junit.Test
import org.ligi.materialteatimer.MainActivity
import org.ligi.materialteatimer.R
import org.ligi.trulesk.TruleskActivityRule


class TheMainActivity {

    @get:Rule
    val rule = TruleskActivityRule(MainActivity::class.java)

    @Test
    fun thatActivityShouldLaunch() {
        rule.screenShot("main")
    }

    @Test
    fun thatInfoOpens() {
        onView(withId(R.id.menuInfo)).perform(click())

        rule.screenShot("info")
    }
}
