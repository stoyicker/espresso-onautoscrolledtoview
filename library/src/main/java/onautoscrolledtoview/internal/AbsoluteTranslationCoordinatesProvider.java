package onautoscrolledtoview.internal;

import android.view.View;

import androidx.test.espresso.action.CoordinatesProvider;

final class AbsoluteTranslationCoordinatesProvider implements CoordinatesProvider {
  private final CoordinatesProvider originalCoordinatesProvider;
  private final float dx;
  private final float dy;

  AbsoluteTranslationCoordinatesProvider(
      final CoordinatesProvider originalCoordinatesProvider,
      final float dx,
      final float dy) {
    this.originalCoordinatesProvider = originalCoordinatesProvider;
    this.dx = dx;
    this.dy = dy;
  }

  @Override
  public float[] calculateCoordinates(final View view) {
    final float[] originalCoordinates = originalCoordinatesProvider.calculateCoordinates(view);
    return new float[]{originalCoordinates[0] + dx, originalCoordinates[1] + dy};
  }
}
