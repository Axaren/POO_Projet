package src_basic.sprites;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * This class is used to represent a sprite that has an image
 */
public class ImageSprite extends Sprite {

  private Image image;

  public ImageSprite(Image image, int x, int y, int z) {
    super(x, y, z);
    this.image = image;
  }

  public ImageSprite(ImageSprite imageSprite) {
    super(imageSprite);
    this.image = imageSprite.image;
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.drawImage(image, x, y);
  }

  /**
   * Checks if point is contained inside the image
   *
   * @param x coordinate
   * @param y coordinate
   * @return True if the point is inside the image
   */
  @Override
  public boolean contains(int x, int y) {
    return x > this.x && x < this.x + image.getWidth()
        && y < this.y && y > this.y + image.getHeight();
  }

  @Override
  public String toString() {
    return "ImageSprite{" +
        "image=" + image +
        ", x=" + x +
        ", y=" + y +
        ", z=" + z +
        '}';
  }
}
