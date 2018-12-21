package src_basic.sprites;

import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import src_basic.SerializableColor;

/**
 * This class is used to represent a Sprite with a rectangular shape
 */
public class RectangleSprite extends Sprite implements Serializable {

  private int width;
  private int height;

  private transient Color color;
  private SerializableColor serializableColor;
  public RectangleSprite(RectangleSprite rectangleSprite) {
    super(rectangleSprite);
    this.width = rectangleSprite.width;
    this.height = rectangleSprite.height;
    this.color = rectangleSprite.color;
    this.serializableColor = new SerializableColor(color);
  }

  public RectangleSprite(int x, int y, int z, int width, int height, Color color) {
    super(x, y, z);
    this.width = width;
    this.height = height;
    this.color = color;
    this.serializableColor = new SerializableColor(color);
  }

  public void setColor(Color color) {
    this.color = color;
    this.serializableColor.setColor(color);
  }

  public Color getColor() {
    return color;
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.setFill(color);
    gc.setStroke(color);
    gc.fillRect(x - width, y - height, width, height);
  }

  /**
   * Checks if the point is contained inside the rectangle
   *
   * @param x coordinate
   * @param y coordinate
   * @return True if the point a coordinates (x,y) is inside the rectangle
   */
  @Override
  public boolean contains(int x, int y) {
    return x > this.x && x < this.x + width
        && y < this.y && y > this.y + height;
  }

  @Override
  public String toString() {
    return "RectangleSprite{" +
        "width=" + width +
        ", height=" + height +
        ", color=" + color +
        ", x=" + x +
        ", y=" + y +
        ", z=" + z +
        '}';
  }
}
