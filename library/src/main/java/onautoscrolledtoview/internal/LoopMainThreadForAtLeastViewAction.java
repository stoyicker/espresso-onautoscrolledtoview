package onautoscrolledtoview.internal;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;

import java.util.Locale;

final class LoopMainThreadForAtLeastViewAction implements ViewAction {
  private final long atLeastMs;

  LoopMainThreadForAtLeastViewAction(long atLeastMs) {
    this.atLeastMs = atLeastMs;
  }

  @Override
  public Matcher<View> getConstraints() {
    return new IsAnything<>();
  }

  @Override
  public String getDescription() {
    return String.format(Locale.ROOT, "loop main thread for at least %d ms", atLeastMs);
  }

  @Override
  public void perform(final UiController uiController, final View view) {
    uiController.loopMainThreadForAtLeast(atLeastMs);
  }
}
