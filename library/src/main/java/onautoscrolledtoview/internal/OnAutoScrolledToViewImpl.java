package onautoscrolledtoview.internal;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;

import onautoscrolledtoview.OnAutoScrolledToView;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

public final class OnAutoScrolledToViewImpl {
  private final Matcher<View> targetViewMatcher;
  private final Matcher<View> scrollableViewMatcher;
  private final OnAutoScrolledToView.DirectionalPxDelta directionalPxDeltaPerScroll;
  private final long timeoutAfterMs;

  public OnAutoScrolledToViewImpl(
      final Matcher<View> targetViewMatcher,
      final OnAutoScrolledToView.Options options) {
    this.targetViewMatcher = targetViewMatcher;
    this.scrollableViewMatcher = options.getScrollableViewMatcher();
    this.directionalPxDeltaPerScroll = options.getDirectionalPxDeltaPerScroll();
    this.timeoutAfterMs = options.getTimeoutAfterUnit()
        .toMillis(options.getTimeoutAfter());
  }

  public ViewInteraction viewInteraction() {
    final long timeout = System.currentTimeMillis() + timeoutAfterMs;
    final ViewInteraction targetViewInteraction = onView(targetViewMatcher);
    final ViewInteraction scrollableViewInteraction = onView(scrollableViewMatcher);
    final ViewAssertion targetViewAssertion = matches(targetViewMatcher);
    final ViewAction scrollViewAction = actionWithAssertions(new GeneralSwipeAction(
        Swipe.FAST,
        GeneralLocation.VISIBLE_CENTER,
        resolveDeltaFromPositionWithLtr(
            GeneralLocation.VISIBLE_CENTER, directionalPxDeltaPerScroll),
        Press.FINGER
    ));
    while (true) {
      try {
        targetViewInteraction.check(targetViewAssertion);
        return targetViewInteraction;
      } catch (final NoMatchingViewException noMatchingViewException) {
        if (System.currentTimeMillis() > timeout) {
          throw noMatchingViewException;
        } else {
          scrollableViewInteraction.perform(scrollViewAction);
        }
      }
    }

  }

  private static CoordinatesProvider resolveDeltaFromPositionWithLtr(
      @SuppressWarnings("SameParameterValue") final CoordinatesProvider src,
      final OnAutoScrolledToView.DirectionalPxDelta directionalPxDelta) {
    float dx;
    final float dy;
    switch (directionalPxDelta.getTowards()) {
      case START:
        dx = directionalPxDelta.getPxDelta();
        dy = 0;
        break;
      case TOP:
        dx = 0;
        dy = directionalPxDelta.getPxDelta();
        break;
      case END:
        dx = -directionalPxDelta.getPxDelta();
        dy = 0;
        break;
      case BOTTOM:
        dx = 0;
        dy = -directionalPxDelta.getPxDelta();
        break;
      default:
        throw new IllegalArgumentException("Illegal towards: " + directionalPxDelta.getTowards());
    }
    final boolean isRtl = InstrumentationRegistry.getInstrumentation()
        .getTargetContext()
        .getResources()
        .getConfiguration()
        .getLayoutDirection() == 1;
    if (isRtl) {
      dx *= -1;
    }
    return new AbsoluteTranslationCoordinatesProvider(src, dx, dy);
  }
}
