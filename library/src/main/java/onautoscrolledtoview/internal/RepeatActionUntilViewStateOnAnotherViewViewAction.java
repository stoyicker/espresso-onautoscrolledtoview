package onautoscrolledtoview.internal;

import android.view.View;
import android.view.ViewGroup;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class RepeatActionUntilViewStateOnAnotherViewViewAction implements ViewAction {
  private final Matcher<View> targetViewMatcher;
  private final Matcher<View> abortViewMatcher;
  private final ViewAction action;
  private final int maxAttempts;
  private final boolean throwIfException;

  RepeatActionUntilViewStateOnAnotherViewViewAction(
      final Matcher<View> targetViewMatcher,
      final Matcher<View> abortViewMatcher,
      final ViewAction action,
      int maxAttempts,
      boolean throwIfException) {
    this.targetViewMatcher = targetViewMatcher;
    this.abortViewMatcher = abortViewMatcher;
    this.action = action;
    this.maxAttempts = maxAttempts;
    this.throwIfException = throwIfException;
  }

  @Override
  public Matcher<View> getConstraints() {
    return action.getConstraints();
  }

  @Override
  public String getDescription() {
    final Description targetDescription = new StringDescription();
    final Description abortDescription = new StringDescription();
    targetViewMatcher.describeTo(targetDescription);
    abortViewMatcher.describeTo(abortDescription);
    return String.format(
        Locale.ROOT,
        "%s until %s is reached, or aborted after reaching %s or %d iterations",
        action.getDescription(),
        targetDescription,
        abortDescription,
        maxAttempts);
  }

  @Override
  public void perform(final UiController uiController, final View view) {
    uiController.loopMainThreadUntilIdle();
    int attempts = 1;
    do {
      for (final View each : plusChildrenRecursively(view)) {
        if (targetViewMatcher.matches(each)) {
          return;
        }
      }
      action.perform(uiController, view);
      uiController.loopMainThreadUntilIdle();
      attempts++;
    } while (attempts <= maxAttempts && !abortViewMatcher.matches(view));
    if (!throwIfException) {
      return;
    }
    final PerformException.Builder exceptionBuilder = new PerformException.Builder()
        .withActionDescription(getDescription())
        .withViewDescription(HumanReadables.describe(view));
    if (attempts >= maxAttempts) {
      throw exceptionBuilder
          .withCause(
              new MaxAttemptsReachedException(
                  String.format(
                      Locale.ROOT,
                      "Reached maxAttempts to achieve view state after %d attempts",
                      maxAttempts)))
          .build();
    } else {
      throw exceptionBuilder
          .withCause(new AbortViewStateReachedException(
              String.format(
                  Locale.ROOT,
                  "Met abort view state %s",
                  abortViewMatcher.toString())))
          .build();
    }
  }

  public static class MaxAttemptsReachedException extends RuntimeException {
    MaxAttemptsReachedException(final String message) {
      super(message);
    }
  }

  public static class AbortViewStateReachedException extends RuntimeException {
    AbortViewStateReachedException(final String message) {
      super(message);
    }
  }

  private static Set<View> plusChildrenRecursively(final View view) {
    final int childCount = (view instanceof ViewGroup) ? ((ViewGroup) view).getChildCount() : 0;
    final Set<View> ret = new HashSet<>(childCount + 1);
    ret.add(view);
    for (int i = 0; i < childCount; i++) {
      ret.addAll(plusChildrenRecursively(((ViewGroup) view).getChildAt(i)));
    }
    return ret;
  }
}
