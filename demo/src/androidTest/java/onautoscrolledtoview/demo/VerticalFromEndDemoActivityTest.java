package onautoscrolledtoview.demo;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static onautoscrolledtoview.OnAutoScrolledToView.onAutoScrolledToView;

public final class VerticalFromEndDemoActivityTest {
  @Rule
  public final TestRule activityTestRule = new ActivityTestRule<DemoActivity>(DemoActivity.class) {
    @Override
    protected Intent getActivityIntent() {
      return DemoActivity.newCallingIntent(
          InstrumentationRegistry.getInstrumentation().getTargetContext(),
          99,
          LinearLayoutManager.VERTICAL);
    }
  };

  @Before
  public void beforeEach() {
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();
  }

  @Test
  public void element99IsVisible() {
    onView(withText("\n[99]\n")).check(matches(isCompletelyDisplayed()));
  }

  @Test
  public void element0DoesNotExist() {
    onView(withText("\n[0]\n")).check(doesNotExist());
  }

  @Test
  public void element0ExistsWithAutoScroll() {
    onAutoScrolledToView(withText("\n[0]\n"), withId(R.id.my_recyclerview)).check(
        matches(isDisplayed()));
  }

  @Test
  public void elementMinus1DoesNotExistWithAutoScroll() {
    onAutoScrolledToView(withText("\n[-1]\n"), withId(R.id.my_recyclerview)).check(
        doesNotExist());
  }
}
