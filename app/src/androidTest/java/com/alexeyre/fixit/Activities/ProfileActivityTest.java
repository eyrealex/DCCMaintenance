package com.alexeyre.fixit.Activities;


import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.alexeyre.fixit.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Rule
    public ActivityTestRule<UserLoginActivity> mActivityTestRule = new ActivityTestRule<>(UserLoginActivity.class);

    @Test
    public void profileActivityTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email_et)));
        appCompatEditText.perform(typeText("eyrealex97@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password_et)));
        appCompatEditText2.perform(typeText("Password123!!"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.login_btn), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        materialButton.perform(scrollTo(), click());
        SystemClock.sleep(8000);

        ViewInteraction circleImageView = onView(
                allOf(withId(R.id.profile_pic),
                        childAtPosition(
                                allOf(withId(R.id.layout_header),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        circleImageView.perform(click());
        SystemClock.sleep(8000);

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.editBtn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());
        SystemClock.sleep(3000);

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.profile_number_tv)));
        textInputEditText.perform(replaceText("0856667777"));
        SystemClock.sleep(3000);

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.editBtn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView2.perform(click());
        SystemClock.sleep(3000);

        ViewInteraction textView = onView(
                allOf(withId(R.id.profile_name_banner)));
        textView.check(matches(withText("Alex Eyre")));
        SystemClock.sleep(3000);

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
