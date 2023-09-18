package com.example.chinesecheckers;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.CursorMatchers.withRowString;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;

import android.widget.ImageButton;

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
public class AdminControlsTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityScenarioRule<LoginPage> activityRule = new ActivityScenarioRule<>(LoginPage.class);

    /**
     * This test logs in as admin and changes a users username and password.
     */
    @Test
    public void AlterUser(){
        String username = "admin1";
        String password = "123";
        String newUsername = "newusername";
        String newPassword = "newpass";
        String revertUsername = "oldusername";
        String revertPassword = "oldpass";

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

        onView(withId(R.id.adminHomePageButton)).perform(click());

        // Put thread to sleep to allow view to generate.
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.adminUserControls)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.userList)).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(UserModel.class)), withName(is("oldusername")))).perform(click());


        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that it is now as the login page.
        onView(withId(R.id.adminProfileUsername)).check(matches(withText("oldusername")));
        onView(withId(R.id.adminProfileRole)).check(matches(withText("user")));
        onView(withId(R.id.profilePassword)).check(matches(withText("oldpass")));

        // test password change.
        onView(withId(R.id.adminUserChangePass)).perform(click());
        onView(withId(R.id.newUsernameField))
                .perform(typeText(newPassword), closeSoftKeyboard());
        onView(withId(R.id.confirmUsernameDialog)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.profilePassword)).check(matches(withText("newpass")));

        // revert test password change.
        onView(withId(R.id.adminUserChangePass)).perform(click());
        onView(withId(R.id.newUsernameField))
                .perform(typeText(revertPassword), closeSoftKeyboard());
        onView(withId(R.id.confirmUsernameDialog)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.profilePassword)).check(matches(withText("oldpass")));

        // test username change.
        onView(withId(R.id.adminUserChangeUsername)).perform(click());
        onView(withId(R.id.newUsernameField))
                .perform(typeText(newUsername), closeSoftKeyboard());
        onView(withId(R.id.confirmUsernameDialog)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.adminProfileUsername)).check(matches(withText("newusername")));

        // revert back to old username
        onView(withId(R.id.adminUserChangeUsername)).perform(click());
        onView(withId(R.id.newUsernameField))
                .perform(typeText(revertUsername), closeSoftKeyboard());
        onView(withId(R.id.confirmUsernameDialog)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.adminProfileUsername)).check(matches(withText("oldusername")));

        // Use the back button
        ViewInteraction imageButton = onView(
                Matchers.allOf(
                        withContentDescription("Navigate up"),
                        isDisplayed()
                )
        );
        imageButton.perform(click());

        // Use the back button
        imageButton = onView(
                Matchers.allOf(
                        withContentDescription("Navigate up"),
                        isDisplayed()
                )
        );
        imageButton.perform(click());

        // Use the back button
        imageButton = onView(
                Matchers.allOf(
                        withContentDescription("Navigate up"),
                        isDisplayed()
                )
        );
        imageButton.perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        // Verify that the intent is the homepage.
        onView(withId(R.id.HomePageHeader)).check(matches(withText("Chinese Checkers")));

    }

    public static Matcher<UserModel> withName(Matcher<? super Object> nameMatcher){
        return new TypeSafeMatcher<UserModel>(){
            @Override
            public boolean matchesSafely(UserModel user) {
                return nameMatcher.matches(user.getUsername());
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}
