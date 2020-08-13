package onautoscrolledtoview.demo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

final class DemoRecyclerViewViewHolder extends RecyclerView.ViewHolder {
  DemoRecyclerViewViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  void setText(CharSequence charSequence) {
    ((TextView) itemView).setText(charSequence);
  }
}
