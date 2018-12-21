package basic.sprites;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class CircleSprite extends Sprite {

  private int radius;

  public void setColor(Color color) {
    this.color = color;
  }

  private Color color;
  public CircleSprite(int x, int y, int z, int radius, Color color) {
    super(x, y, z);
    this.radius = radius;
    this.color = color;
  }

  public CircleSprite(CircleSprite circleSprite) {
    super(circleSprite);
    this.radius = circleSprite.radius;
    this.color = circleSprite.color;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public Color getColor() {
    return color;
  }

  @Override
  public void render(GraphicsContext gc) {
    Stop[] stops = new Stop[]{new Stop(0, color.darker()), new Stop(1, color)};
    LinearGradient paint = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    gc.setFill(paint);
    gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
  }

  @Override
  public boolean contains(int x, int y) {
    return Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y)) <= radius;
  }
}
