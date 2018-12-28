package game_advanced.sprites;

import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * This class is used to represent a Sprite that is circle shaped
 */

public class CircleSprite extends ShapeSprite implements Serializable {

  private int radius;

  public CircleSprite(double x, double y, int z, int radius, Color color) {
    super(x, y, z, color);
    this.radius = radius;
  }

  public CircleSprite(CircleSprite circleSprite) {
    super(circleSprite);
    this.radius = circleSprite.radius;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
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

  @Override
  public double getCircumscribedCircleRadius() {
    return radius;
  }
}
