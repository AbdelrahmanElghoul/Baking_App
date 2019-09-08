package com.example.bakingapp;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    private IdlingResource mIdlingResource;
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule=
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdleResource(){
        mIdlingResource=mainActivityActivityTestRule.getActivity().idlingResource;
        Espresso.registerIdlingResources(mIdlingResource);
    }
    @Test
    public void RecipesAdapterTest(){


        onView(withId(R.id.recipe_name_recycler_view))
           .perform(RecyclerViewActions.scrollToHolder(isInTheMiddle()));

        // Checks that the OrderActivity opens with the correct tea name displayed
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
    }
    @After
    public void unregisterIdleResource(){
        Espresso.unregisterIdlingResources(mIdlingResource);
    }

    private static Matcher<recipesNameAdapter.mViewHolder> isInTheMiddle() {
        return new TypeSafeMatcher<recipesNameAdapter.mViewHolder>() {
            @Override
            protected boolean matchesSafely(recipesNameAdapter.mViewHolder customHolder) {
                return customHolder.getIsInTheMiddle();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item in the middle");
            }
        };

}
}
