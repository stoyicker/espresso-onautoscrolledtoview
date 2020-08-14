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
    @Getter
    @Builder.Default
    @NonNull
    private Matcher<View> scrollableViewMatcher = isRoot();
    @Getter
    @Builder.Default
    @NonNull
    private DirectionalPxDelta directionalPxDeltaPerScroll =
        new DirectionalPxDelta(100F, DirectionalPxDelta.Towards.BOTTOM);
    @Getter
    @Builder.Default
    private long timeoutAfter = 20 * 1000;
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

  public static ViewInteraction onAutoScrolledToView(
      final @NonNull Matcher<View> targetViewMatcher) {
    return onAutoScrolledToView(targetViewMatcher, Options.builder()
        .build());
  }

  public static ViewInteraction onAutoScrolledToView(
      final @NonNull Matcher<View> targetViewMatcher,
      final @NonNull OnAutoScrolledToView.Options options) {
    return new OnAutoScrolledToViewImpl(targetViewMatcher, options).viewInteraction();
  }
}
