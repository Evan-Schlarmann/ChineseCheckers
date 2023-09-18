package com.example.chinesecheckers;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PrivateLobbyChatTest {

    @Rule
    public ActivityScenarioRule<LoginPage> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginPage.class);

    @Test
    public void privateLobbyChatTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.usernameText),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("a"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.passwordText),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("a"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.loginButton), withText("login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.joinPrivateLobby), withText("private"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                4),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.createPrivateLobby), withText("Create Private Lobby"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.chatWebsocketButton), withText("Open Chat"),
                        childAtPosition(
                                allOf(withId(R.id.privateLobbyFrame),
                                        childAtPosition(
                                                withId(R.id.rea),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.MessageLobbyChat),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.SendMessageLobbyChat), withText("Send"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                2),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.MessageListLobbyChat), withText("\na: hello"),
                        withParent(withParent(withId(R.id.lobbyChatView))),
                        isDisplayed()));
        textView.check(matches(withText("\na: hello")));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.MessageLobbyChat), withText("hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.MessageLobbyChat), withText("hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("bubbles"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.MessageLobbyChat), withText("bubbles"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.SendMessageLobbyChat), withText("Send"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                2),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.MessageLobbyChat), withText("bubbles"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("war crime"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.MessageLobbyChat), withText("war crime"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.MessageListLobbyChat), withText("\na: hello\na: bubbles"),
                        withParent(withParent(withId(R.id.lobbyChatView))),
                        isDisplayed()));
        textView2.check(matches(withText("\na: hello\na: bubbles")));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.MessageLobbyChat), withText("war crime"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.MessageLobbyChat), withText("war crime"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("war crime\nwar crime"));

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.MessageLobbyChat), withText("war crime\nwar crime"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText15.perform(closeSoftKeyboard());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.SendMessageLobbyChat), withText("Send"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lobbyChatView),
                                        0),
                                2),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.MessageListLobbyChat), withText("\na: hello\na: bubbles\na: war crime\nwar crime"),
                        withParent(withParent(withId(R.id.lobbyChatView))),
                        isDisplayed()));
        textView3.check(matches(withText("\na: hello\na: bubbles\na: war crime\nwar crime")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
