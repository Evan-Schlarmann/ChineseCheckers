package com.example.chinesecheckers;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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
public class SignUpTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityScenarioRule<SignUp> activityRule = new ActivityScenarioRule<>(SignUp.class);

    /*@Test
    public void correctSignUp() {

        String username = "aa7";
        String password = "aa";
        String confirmPassword = "aa";

        // Type in testString and send request
        onView(withId(R.id.usernameText2))
                .perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordText2))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordText))
                .perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.roleSpinner))
                .perform(click());
        onData(hasToString(startsWith("user")))
                .perform(click());

        onView(withId(R.id.signUpButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that it is now as the login page.
        onView(withId(R.id.loginButton)).check(matches(withText("login")));
    }*/

    @Test
    public void exitSignUp() {

        onView(withId(R.id.cancelSignUp)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that it is now as the login page.
        onView(withId(R.id.loginButton)).check(matches(withText("login")));
    }

    @Test
    public void failedSignUp(){
        String username = "aaa";
        String password = "aa";
        String confirmPassword = "ab";

        // Type in testString and send request
        onView(withId(R.id.usernameText2))
                .perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.passwordText2))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.confirmPasswordText))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.roleSpinner))
                .perform(click());
        onData(hasToString(startsWith("user")))
                .perform(click());

        onView(withId(R.id.signUpButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that it is now as the login page.
        onView(withId(R.id.usernameText2)).check(matches(hasErrorText("username is taken")));
    }
}