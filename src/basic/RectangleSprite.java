package basic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RectangleSprite extends Sprite {

  private int width;
  private int height;
  private Color color;

  public RectangleSprite(int x, int y, int z, int width, int height, Color color) {
    super(x, y, z);
    this.width = width;
    this.height = height;
    this.color = color;
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.setFill(color);
    gc.setStroke(color);
    gc.fillRect(x, y, width, height);
  }

  @Override
  public boolean contains(int x, int y) {
    return x > this.x && x < this.x + width
        && y < this.y && y > this.y + height;
  }
}
