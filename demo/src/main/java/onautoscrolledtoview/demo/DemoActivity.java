package onautoscrolledtoview.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class DemoActivity extends Activity {
  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final RecyclerView recyclerView = new RecyclerView(this);
    recyclerView.setId(R.id.my_recyclerview);
    recyclerView.setLayoutManager(new LinearLayoutManager(
        this,
        getIntent().getIntExtra(
            KEY_LINEARLAYOUTMANAGER_ORIENTATION, LinearLayoutManager.VERTICAL
        ),
        false));
    recyclerView.setAdapter(new DemoRecyclerViewAdapter());
    recyclerView.scrollToPosition(getIntent().getIntExtra(KEY_SCROLL_TO_POSITION, 0));
    this.<ViewGroup>findViewById(android.R.id.content).addView(
        recyclerView, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
  }

  public static Intent newCallingIntent(
      final Context context, final int scrollToPosition, final int linearLayoutManagerOrientation) {
    return new Intent(context, DemoActivity.class)
        .putExtra(KEY_SCROLL_TO_POSITION, scrollToPosition)
        .putExtra(KEY_LINEARLAYOUTMANAGER_ORIENTATION, linearLayoutManagerOrientation);
  }

  private static final String KEY_SCROLL_TO_POSITION = "KEY_SCROLL_TO_POSITION";
  private static final String KEY_LINEARLAYOUTMANAGER_ORIENTATION = "KEY_LINEARLAYOUTMANAGER_ORIENTATION";
}
