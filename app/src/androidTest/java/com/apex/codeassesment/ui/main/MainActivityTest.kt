import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apex.codeassesment.R
import com.apex.codeassesment.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testLoadSavedUser() {
        Thread.sleep(2000)

        onView(withId(R.id.main_name))
            .check(matches(isDisplayed()))

        onView(withId(R.id.main_see_details_button))
            .check(matches(isDisplayed()))

        onView(withId(R.id.main_see_details_button))
            .perform(click())

    }
}