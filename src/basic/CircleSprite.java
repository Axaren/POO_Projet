package basic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class CircleSprite extends Sprite {

  private int radius;
  private Color color;

  public CircleSprite(int x, int y, int z, int radius, Color color) {
    super(x, y, z);
    this.radius = radius;
    this.color = color;
  }

  public int getRadius() {
    return radius;
  }

  public Color getColor() {
    return color;
  }

  @Override
  public void render(GraphicsContext gc) {
    Stop[] stops = new Stop[]{new Stop(0, Color.LIGHTSALMON), new Stop(1, color)};
    LinearGradient paint = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    gc.setFill(paint);
    gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
  }
}
