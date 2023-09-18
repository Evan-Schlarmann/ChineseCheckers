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

import com.example.chinesecheckers.models.FriendRequestModel;
import com.example.chinesecheckers.models.UserModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FriendshipTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityScenarioRule<LoginPage> activityRule = new ActivityScenarioRule<>(LoginPage.class);

    /**
     * This test sens a friend request from aa to admin.
     */
    @Test
    public void A_FriendUser(){
        String username = "aa";
        String password = "aa";

        String friend = "admin";

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

        onView(withId(R.id.userListButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.userList)).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(UserModel.class)), withName(is(friend)))).perform(click());


        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that it is now at the user's profile.
        onView(withId(R.id.profileUsername)).check(matches(withText(friend)));
        onView(withId(R.id.profileRole)).check(matches(withText("user")));

        // send friend request.
        onView(withId(R.id.friendRequestButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.friendRequestButton)).check(matches(withText("Pending Friend Request.")));
    }

    /**
     * This test logs in as aa and check their outgoing request then deletes it.
     */
    @Test
    public void B_DeleteOutgoingFriendship(){
        String username = "aa";
        String password = "aa";

        String friend = "admin";

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
        onView(withId(R.id.activeUserFriendList)).perform(click());
        onView(withId(R.id.friendRequestListButton)).perform(click());
        onView(withId(R.id.outgoingFriendRequests)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.friendRequestList)).check(matches(isDisplayed()));
        // use the delete button.
        onData(allOf(is(instanceOf(FriendRequestModel.class)), withAccepterName(is(friend))))
                .onChildView(withId(R.id.delete_friend_request_btn))
                .perform(click());
    }

    /**
     * logs in and sends friend request from admin to aa.
     */
    @Test
    public void C_FriendUser(){
        String username = "admin";
        String password = "123";

        String friend = "aa";

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

        onView(withId(R.id.userListButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.userList)).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(UserModel.class)), withName(is(friend)))).perform(click());


        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that it is now as the login page.
        onView(withId(R.id.profileUsername)).check(matches(withText(friend)));
        onView(withId(R.id.profileRole)).check(matches(withText("user")));

        // send friend request.
        onView(withId(R.id.friendRequestButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.friendRequestButton)).check(matches(withText("Pending Friend Request.")));
    }

    /**
     * This test logs in as aa and check their incoming friend request from admin and accepts it.
     */
    @Test
    public void D_AcceptIncomingFriendship(){
        String username = "aa";
        String password = "aa";

        String friend = "admin";

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
        onView(withId(R.id.activeUserFriendList)).perform(click());
        onView(withId(R.id.friendRequestListButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.friendRequestList)).check(matches(isDisplayed()));
        // use the delete button.
        onData(allOf(is(instanceOf(FriendRequestModel.class)), withRequesterName(is(friend))))
                .onChildView(withId(R.id.accept_friend_request_btn))
                .perform(click());

        // Use the back button
        ViewInteraction imageButton = onView(
                Matchers.allOf(
                        withContentDescription("Navigate up"),
                        isDisplayed()
                )
        );
        imageButton.perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.friendList)).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(UserModel.class)), withName(is(friend))))
                .perform(click());

        // Verify that it is now as the login page.
        onView(withId(R.id.profileUsername)).check(matches(withText(friend)));
        onView(withId(R.id.profileRole)).check(matches(withText("user")));
        onView(withId(R.id.friendRequestButton)).check(matches(withText("Remove Friend")));

        onView(withId(R.id.friendRequestButton)).perform(click());
        onView(withId(R.id.friendRequestButton)).check(matches(withText("Send Friend Request")));
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

    public static Matcher<FriendRequestModel> withAccepterName(Matcher<? super Object> friendshipMatcher){
        return new TypeSafeMatcher<FriendRequestModel>(){
            @Override
            public boolean matchesSafely(FriendRequestModel friendship) {
                return friendshipMatcher.matches(friendship.getAccepter().getUsername());
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    public static Matcher<FriendRequestModel> withRequesterName(Matcher<? super Object> friendshipMatcher){
        return new TypeSafeMatcher<FriendRequestModel>(){
            @Override
            public boolean matchesSafely(FriendRequestModel friendship) {
                return friendshipMatcher.matches(friendship.getRequester().getUsername());
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}
