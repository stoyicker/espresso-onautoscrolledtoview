package onautoscrolledtoview.demo;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class DemoActivity extends Activity {
  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final RecyclerView recyclerView = new RecyclerView(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(new DemoRecyclerViewAdapter());
    setContentView(recyclerView);
  }
}
