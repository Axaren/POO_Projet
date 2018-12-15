package basic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageSprite extends Sprite {

  private Image image;

  public ImageSprite(Image image, int x, int y, int z) {
    super(x, y, z);
    this.image = image;
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.drawImage(image, x, y);
  }

  @Override
  public boolean contains(int x, int y) {
    return false;
  }
}
