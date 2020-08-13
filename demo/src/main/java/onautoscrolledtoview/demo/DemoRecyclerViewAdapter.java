package onautoscrolledtoview.demo;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

final class DemoRecyclerViewAdapter extends RecyclerView.Adapter<DemoRecyclerViewViewHolder> {
  @NonNull
  @Override
  public DemoRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new DemoRecyclerViewViewHolder(new TextView(parent.getContext()));
  }

  @Override
  public void onBindViewHolder(@NonNull DemoRecyclerViewViewHolder holder, int position) {
    holder.setText("\n[" + position + "]\n");
  }

  @Override
  public int getItemCount() {
    return Integer.MAX_VALUE;
  }
}
