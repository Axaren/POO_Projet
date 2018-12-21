package src_basic.sprites;

import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import src_basic.SerializableColor;

/**
 * This class is used to represent a Sprite that is circle shaped
 */

public class CircleSprite extends Sprite implements Serializable {

  private int radius;
  private transient Color color;
  private SerializableColor serializableColor;
  public CircleSprite(int x, int y, int z, int radius, Color color) {
    super(x, y, z);
    this.radius = radius;
    this.color = color;
    serializableColor = new SerializableColor(color);
  }

  public CircleSprite(CircleSprite circleSprite) {
    super(circleSprite);
    this.radius = circleSprite.radius;
    this.color = circleSprite.color;
    this.serializableColor = new SerializableColor(color);
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

  public void setColor(Color color) {
    this.color = color;
    this.serializableColor.setColor(color);
  }

  /**
   * Sets the fill to a gradient then displays the circle
   *
   * @param gc The GraphicsContext of the canvas
   */
  @Override
  public void render(GraphicsContext gc) {
    Stop[] stops = new Stop[]{new Stop(0, color.darker()), new Stop(1, color)};
    LinearGradient paint = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    gc.setFill(paint);
    gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
  }

  /**
   * Checks if a point is inside the circle
   *
   * @param x coordinate
   * @param y coordinate
   * @return true if point at coordinates (x,y) is inside the circle
   */
  @Override
  public boolean contains(int x, int y) {
    return Math.sqrt((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y)) <= radius;
  }

  @Override
  public String toString() {
    return "CircleSprite{" +
        "radius=" + radius +
        ", color=" + color +
        ", x=" + x +
        ", y=" + y +
        ", z=" + z +
        '}';
  }
}
