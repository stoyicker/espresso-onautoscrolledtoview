package onautoscrolledtoview.demo;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.Locale;

import onautoscrolledtoview.OnAutoScrolledToView;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public final class LTRHorizontalFromStartDemoActivityTest {
  @Rule
  public final TestRule activityTestRule = new ActivityTestRule<DemoActivity>(DemoActivity.class) {
    @Override
    protected void beforeActivityLaunched() {
      final Resources resources = InstrumentationRegistry.getInstrumentation()
          .getTargetContext()
          .getResources();
      final Configuration configuration = resources.getConfiguration();
      configuration.setLayoutDirection(new Locale("es"));
      resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    protected Intent getActivityIntent() {
      return DemoActivity.newCallingIntent(
          InstrumentationRegistry.getInstrumentation().getTargetContext(),
          0,
          LinearLayoutManager.HORIZONTAL);
    }
  };

  @Before
  public void beforeEach() {
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();
  }

  @Test
  public void element0IsVisible() {
    onView(withText("\n[0]\n")).check(matches(isCompletelyDisplayed()));
  }

  @Test
  public void element99DoesNotExist() {
    onView(withText("\n[99]\n")).check(doesNotExist());
  }

  @Test
  public void element99ExistsWithAutoScroll() {
    OnAutoScrolledToView.onAutoScrolledToView(
        withText("\n[99]\n"),
        OnAutoScrolledToView.Options.builder()
            .directionalPxDeltaPerScroll(
                new OnAutoScrolledToView.DirectionalPxDelta(
                    OnAutoScrolledToView.DirectionalPxDelta.Towards.END))
            .build())
        .check(matches(isDisplayed()));
  }
}
