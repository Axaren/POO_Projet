package basic;

import javafx.scene.canvas.GraphicsContext;

public class RectangleSprite extends Sprite {

  private int width;
  private int height;

  public RectangleSprite(double x, double y, int z, int width, int height) {
    super(x, y, z);
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.fillRect(x, y, width, height);
  }
}
