package onautoscrolledtoview.demo;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

public final class VerticalFromEndDemoActivityTest extends DemoActivityTest {
  public VerticalFromEndDemoActivityTest() {
    super(new ActivityTestRule<DemoActivity>(DemoActivity.class) {
      @Override
      protected Intent getActivityIntent() {
        return DemoActivity.newCallingIntent(
            InstrumentationRegistry.getInstrumentation().getTargetContext(),
            999,
            LinearLayoutManager.VERTICAL);
      }
    });
  }
}
