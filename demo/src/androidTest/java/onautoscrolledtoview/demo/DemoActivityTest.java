package onautoscrolledtoview.demo;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public final class DemoActivityTest {
  @Rule
  public final ActivityTestRule<DemoActivity> activityRule =
      new ActivityTestRule<>(DemoActivity.class);

  @Test
  public void element0IsVisible() {
    onView(withText("\n[0]\n")).check(matches(isCompletelyDisplayed()));
  }

  @Test
  public void element999DoesNotExist() {
    onView(withText("\n[999]\n")).check(doesNotExist());
  }

  @Test
  public void element999ExistsWithAutoScroll() {
  }
}
