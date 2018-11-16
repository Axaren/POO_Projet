package basic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Sprite {

  private Image image;
  private double x;
  private double y;
  private double xSpeed;
  private double ySpeed;
  private double width;
  private double height;
  private double maxX;
  private double maxY;

  public Sprite(String path, double width, double height) {
    image = new Image(path, width, height, false, false);
    this.width = width;
    this.height = height;
  }

  public Sprite(Sprite s) {
    image = s.image;
    width = s.width;
    height = s.height;
    maxX = s.maxX;
    maxY = s.maxY;
  }

  public double width() {
    return width;
  }

  public double height() {
    return height;
  }

  public void setPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void setSpeed(double xSpeed, double ySpeed) {
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
  }

  public void updatePosition() {
    x += xSpeed;
    y += ySpeed;
  }

  public void render(GraphicsContext gc) {
    gc.drawImage(image, x, y);
  }

  public boolean intersects(Sprite s) {
    return ((x >= s.x && x <= s.x + s.width) || (s.x >= x && s.x <= x + width))
        && ((y >= s.y && y <= s.y + s.height) || (s.y >= y && s.y <= y + height));
  }

  public String toString() {
    return "Sprite<" + x + ", " + y + ">";
  }

}
