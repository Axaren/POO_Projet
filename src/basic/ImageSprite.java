package basic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ImageSprite extends Sprite {

  private Image image;

  public ImageSprite(Image image, double x, double y, int z) {
    super(x, y, z);
    this.image = image;
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.drawImage(image, x, y);
  }
}
