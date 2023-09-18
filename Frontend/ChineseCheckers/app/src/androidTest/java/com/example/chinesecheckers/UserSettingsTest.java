package com.example.chinesecheckers;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.chinesecheckers.models.UserModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserSettingsTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityScenarioRule<LoginPage> activityRule = new ActivityScenarioRule<>(LoginPage.class);

    /**
     * This test logs in as admin and changes a users username and password.
     */
    @Test
    public void AlterActiveUser(){
        String username = "admin1";
        String password = "123";
        String newUsername = "newusername2";
        String newPassword = "newpass2";

        onView(withId(R.id.usernameText))
                .perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordText))
                .perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.profileSettingsButton)).perform(click());

        // Put thread to sleep to allow view to generate.
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // test username change.
        onView(withId(R.id.activeUserChangeUsername)).perform(click());
        onView(withId(R.id.newUsernameField))
                .perform(typeText(newUsername), closeSoftKeyboard());
        onView(withId(R.id.confirmUsernameDialog)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.activeProfileUsername)).check(matches(withText("newusername2")));

        // test password change.
        onView(withId(R.id.activeUserChangePass)).perform(click());
        onView(withId(R.id.newUsernameField))
                .perform(typeText(newPassword), closeSoftKeyboard());
        onView(withId(R.id.confirmUsernameDialog)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.signOutButton)).perform(click());

        onView(withId(R.id.usernameText))
                .perform(typeText(newUsername), closeSoftKeyboard());
        onView(withId(R.id.passwordText))
                .perform(typeText(newPassword), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.profileSettingsButton)).perform(click());

        // Put thread to sleep to allow view to generate.
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // revert username change.
        onView(withId(R.id.activeUserChangeUsername)).perform(click());
        onView(withId(R.id.newUsernameField))
                .perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.confirmUsernameDialog)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.activeProfileUsername)).check(matches(withText("admin1")));

        // revert password change.
        onView(withId(R.id.activeUserChangePass)).perform(click());
        onView(withId(R.id.newUsernameField))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.confirmUsernameDialog)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
    }
}
