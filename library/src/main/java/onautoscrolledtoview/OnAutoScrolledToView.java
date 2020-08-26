package onautoscrolledtoview;

import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;

import lombok.Builder;
import lombok.Getter;
import onautoscrolledtoview.internal.OnAutoScrolledToViewImpl;
import onautoscrolledtoview.internal.RepeatActionUntilViewStateOnAnotherViewViewAction;

import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.Matchers.not;

@Keep
public final class OnAutoScrolledToView {
  @Builder(toBuilder = true)
  public static final class Options {
    /**
     * Describes each scroll to be performed on the view by combining the pointer displacement and
     * the axis it has to be performed on. Too long of a per-swipe delta may leave the viewport in
     * an unexpected state after the target View is found. Too short of a per-swipe delta may cause
     * your view to bounce because of {@link #maxAttempts} being reached before getting to the
     * bottom.
     * <p>
     * The axis describes which direction where the target View is expected to be found. When the
     * axis is {@link AxisPxDelta.Axis#VERTICAL}, the scroll happens first downwards and then
     * upwards. When the axis is {@link AxisPxDelta.Axis#HORIZONTAL}, the scroll happens towards the
     * end and then towards the start (LTR is resolved automatically).
     *
     * @see #maxAttempts
     */
    @Getter
    @Builder.Default
    @NonNull
    private AxisPxDelta axisPxDeltaPerScroll = new AxisPxDelta(AxisPxDelta.Axis.VERTICAL);
    /**
     * Specifies a per-direction exit condition in the form of a maximum number of scrolls. If
     * the given <code>targetViewMatcher</code> has not been satisfied after scrolling this many
     * times in a direction, no further scrolls are attempted in that direction, following in to
     * either try the opposite direction or give up and return if both directions have been tested.
     *
     * @see #axisPxDeltaPerScroll
     */
    @Getter
    @Builder.Default
    private int maxAttempts = 100;
    /**
     * Specifies an additional per-direction exit condition. This can be used to speed up cases in
     * which you can reliably determine that <code>targetViewMatcher</code> is not going to be met
     * before {@link #maxAttempts} is reached.
     *
     * @see #maxAttempts
     */
    @Getter
    @Builder.Default
    @NonNull
    private Matcher<View> abortViewMatcher = not(new IsAnything<View>());
  }

  public static final class AxisPxDelta {
    private static final float DEFAULT_DELTA_PX = 100F;
    @Getter
    @Px
    private final float pxDelta;
    @Getter
    private final Axis axis;

    /**
     * Uses the default px delta.
     *
     * @param axis The direction where the target view is expected to be found.
     */
    public AxisPxDelta(final Axis axis) {
      this(DEFAULT_DELTA_PX, axis);
    }

    public AxisPxDelta(final @Px float pxDelta, final Axis axis) {
      this.pxDelta = pxDelta;
      this.axis = axis;
    }

    AxisPxDelta reverse() {
      return new AxisPxDelta(-pxDelta, axis);
    }

    public enum Axis {
      VERTICAL,
      HORIZONTAL
    }
  }

  /**
   * An overload of {@link #onAutoScrolledToView(Matcher, Matcher, Options)} with default
   * configuration values.
   */
  public static ViewInteraction onAutoScrolledToView(
      final @NonNull Matcher<View> targetViewMatcher,
      final @NonNull Matcher<View> scrollableViewMatcher) {
    return onAutoScrolledToView(
        targetViewMatcher,
        scrollableViewMatcher,
        Options.builder().build());
  }

  /**
   * Returns {@link androidx.test.espresso.Espresso#onView(Matcher)} with
   * <code>targetViewMatcher</code> after scrolling until a view hierarchy in which such condition
   * is met.
   * <p>
   * This works synchronously; if the given <code>targetViewMatcher</code> would ever be satisfiable
   * from the current viewport and with the given <code>options</code>, it is guaranteed to be
   * satisfied when this method returns.
   *
   * @param targetViewMatcher A {@link Matcher} representing the condition that is sought after.
   * @param scrollableViewMatcher A {@link Matcher} representing the {@link View} to scroll on. It
   *                              and its children (recursively) are checked against
   *                              <code>targetViewMatcher</code>.
   * @param options           Customizable configuration for the scrolling. See fields in
   *                          {@link Options} for defaults.
   * @return {@link androidx.test.espresso.Espresso#onView(Matcher)} with
   * <code>targetViewMatcher</code>.
   * @see Options#builder()
   */
  public static ViewInteraction onAutoScrolledToView(
      final @NonNull Matcher<View> targetViewMatcher,
      final @NonNull Matcher<View> scrollableViewMatcher,
      final @NonNull OnAutoScrolledToView.Options options) {
    try {
      new OnAutoScrolledToViewImpl(targetViewMatcher, scrollableViewMatcher, options, true).run();
    } catch (final PerformException performException) {
      final Throwable cause = performException.getCause();
      if (cause instanceof
          RepeatActionUntilViewStateOnAnotherViewViewAction.MaxAttemptsReachedException ||
          cause instanceof
              RepeatActionUntilViewStateOnAnotherViewViewAction.AbortViewStateReachedException) {
        new OnAutoScrolledToViewImpl(
            targetViewMatcher,
            scrollableViewMatcher,
            options.toBuilder()
                .axisPxDeltaPerScroll(options.axisPxDeltaPerScroll.reverse())
                .build(),
            false).run();
      }
    }
    return onView(targetViewMatcher);
  }
}
