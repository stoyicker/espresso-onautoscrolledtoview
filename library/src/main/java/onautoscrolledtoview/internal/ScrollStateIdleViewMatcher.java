package onautoscrolledtoview.internal;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

final class ScrollStateIdleViewMatcher extends TypeSafeMatcher<View> {
  @Override
  protected boolean matchesSafely(View item) {
    try {
      Class.forName(
          "androidx.recyclerview.widget.RecyclerView",
          false,
          getClass().getClassLoader());
    } catch (ClassNotFoundException ignored) {
      return true;
    }
    if (item instanceof RecyclerView) {
      return ((RecyclerView) item).getScrollState() == RecyclerView.SCROLL_STATE_IDLE;
    }
    return true;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("Is not a recycler view or its scroll state is idle");
  }
}
