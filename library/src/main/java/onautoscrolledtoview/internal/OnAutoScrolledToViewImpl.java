package onautoscrolledtoview.internal;

import android.graphics.PointF;
import android.view.View;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;

import org.hamcrest.Matcher;

import onautoscrolledtoview.OnAutoScrolledToView;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.action.ViewActions.repeatedlyUntil;
import static org.hamcrest.Matchers.allOf;

public final class OnAutoScrolledToViewImpl implements Runnable {
  private final Matcher<View> targetViewMatcher;
  private final Matcher<View> scrollableViewMatcher;
  private final Matcher<View> abortViewMatcher;
  private final OnAutoScrolledToView.AxisPxDelta axisPxDeltaPerScroll;
  private final int maxAttempts;
  private final boolean throwIfException;

  public OnAutoScrolledToViewImpl(
      final Matcher<View> targetViewMatcher,
      final Matcher<View> scrollableViewMatcher,
      final OnAutoScrolledToView.Options options,
      final boolean throwIfException) {
    this.targetViewMatcher = targetViewMatcher;
    this.scrollableViewMatcher = scrollableViewMatcher;
    this.abortViewMatcher = options.getAbortViewMatcher();
    this.axisPxDeltaPerScroll = options.getAxisPxDeltaPerScroll();
    this.maxAttempts = options.getMaxAttempts();
    this.throwIfException = throwIfException;
  }

  @Override
  public void run() {
    final PointF resolvedTranslation = resolveTranslation();
    final ViewAction scrollViewAction = new RepeatActionUntilViewStateOnAnotherViewViewAction(
        targetViewMatcher,
        allOf(scrollableViewMatcher, abortViewMatcher),
        actionWithAssertions(
            new GeneralSwipeAction(
                Swipe.FAST,
                GeneralLocation.VISIBLE_CENTER,
                new AbsoluteTranslationCoordinatesProvider(
                    GeneralLocation.VISIBLE_CENTER,
                    resolvedTranslation.x,
                    resolvedTranslation.y
                ),
                Press.FINGER
            )
        ),
        maxAttempts,
        throwIfException
    );
    onView(scrollableViewMatcher).perform(scrollViewAction, repeatedlyUntil(
        new LoopMainThreadForAtLeastViewAction(1),
        new ScrollStateIdleViewMatcher(),
        Integer.MAX_VALUE
    ));
  }

  private PointF resolveTranslation() {
    switch (axisPxDeltaPerScroll.getAxis()) {
      case VERTICAL:
        return new PointF(0F, axisPxDeltaPerScroll.getPxDelta());
      case HORIZONTAL:
        return new PointF(axisPxDeltaPerScroll.getPxDelta(), 0F);
      default:
        throw new IllegalArgumentException(
            "Unsupported axis " + axisPxDeltaPerScroll.getAxis().name());
    }
  }
}
