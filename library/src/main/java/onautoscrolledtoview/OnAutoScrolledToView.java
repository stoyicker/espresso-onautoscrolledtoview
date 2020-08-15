package onautoscrolledtoview;

import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeUnit;

import lombok.Builder;
import lombok.Getter;
import onautoscrolledtoview.internal.OnAutoScrolledToViewImpl;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

@Keep
public final class OnAutoScrolledToView {
  @Builder
  public static final class Options {
    /**
     * A {@link Matcher<View>} that describes which view to perform the swipe on. In most scenarios
     * you should not care about this, but you may be interested in using it in particular cases
     * such as when you have more than one RecyclerView in the viewport at a time or when testing
     * an app in multi-window mode.
     *
     * @see <a href="https://developer.android.com/training/testing/espresso/recipes#targeting-non-default-windows">Espresso recipes - Target non-default windows | Android Developers</a>
     */
    @Getter
    @Builder.Default
    @NonNull
    private Matcher<View> scrollableViewMatcher = isRoot();
    /**
     * Describes the direction looked after when scrolling, as well as how long each swipe should
     * be. Too long of a per-swipe delta may leave the viewport in an unexpected state after the
     * target View is found. Too short of a per-swipe delta may require an overly long timeout in
     * order to get to scroll until the target view.
     *
     * The direction describes which direction where the target View is expected to be found. When
     * using {@link DirectionalPxDelta.Towards#START} and {@link DirectionalPxDelta.Towards#END}
     * LTR/RTL is resolved automatically.
     *
     * @see #timeoutAfter
     */
    @Getter
    @Builder.Default
    @NonNull
    private DirectionalPxDelta directionalPxDeltaPerScroll =
        new DirectionalPxDelta(100F, DirectionalPxDelta.Towards.BOTTOM);
    /**
     * Specifies a timeout (along {@link #timeoutAfterUnit}. If the given {@link Matcher} has not
     * been satisfied in the specified amount of time, a {@link androidx.test.espresso.NoMatchingViewException}
     * will be thrown corresponding to the View described by said matcher. Bear in mind that, in
     * cases where {@link #directionalPxDeltaPerScroll} describes a delta that is too small, too
     * short of a timeout may stop enough scrolling from happening before the timeout is reached.
     *
     * @see #directionalPxDeltaPerScroll
     * @see #timeoutAfterUnit
     */
    @Getter
    @Builder.Default
    private long timeoutAfter = 20 * 1000;
    /**
     * {@link TimeUnit} for {@link #timeoutAfter}.
     *
     * @see #timeoutAfter
     */
    @Getter
    @Builder.Default
    @NonNull
    private TimeUnit timeoutAfterUnit = TimeUnit.MILLISECONDS;
  }

  public static final class DirectionalPxDelta {
    @Getter
    private final @Px
    float pxDelta;
    @Getter
    private final Towards towards;

    /**
     * Uses the default px delta.
     *
     * @param towards The direction where the target view is expected to be found.
     */
    public DirectionalPxDelta(final Towards towards) {
      this(Options.builder().build().getDirectionalPxDeltaPerScroll().pxDelta, towards);
    }

    public DirectionalPxDelta(final @Px float pxDelta, final Towards towards) {
      this.pxDelta = pxDelta;
      this.towards = towards;
    }

    public enum Towards {
      START,
      TOP,
      END,
      BOTTOM
    }
  }

  /**
   * An overload of {@link #onAutoScrolledToView(Matcher, Options)} with default configuration
   * values.
   */
  public static ViewInteraction onAutoScrolledToView(
      final @NonNull Matcher<View> targetViewMatcher) {
    return onAutoScrolledToView(targetViewMatcher, Options.builder()
        .build());
  }

  /**
   * Returns {@link androidx.test.espresso.Espresso#onView(Matcher)} with the given
   * {@link Matcher<View>} after scrolling until a view hierarchy in which such condition is met, or
   * when an indicated timeout is reached, whatever happens first.
   * <p>
   * This works synchronously; the given {@link Matcher<View>} is guaranteed to be satisfied when
   * this method returns.
   *
   * @param targetViewMatcher A {@link Matcher<View>} representing the condition that is sought
   *                          after.
   * @param options           Customizable configuration for the scrolling. See fields in {@link Options} for
   *                          defaults.
   * @return {@link androidx.test.espresso.Espresso#onView(Matcher)} with the given
   * {@link Matcher<View>}.
   * @see Options#builder()
   */
  public static ViewInteraction onAutoScrolledToView(
      final @NonNull Matcher<View> targetViewMatcher,
      final @NonNull OnAutoScrolledToView.Options options) {
    return new OnAutoScrolledToViewImpl(targetViewMatcher, options).viewInteraction();
  }
}
