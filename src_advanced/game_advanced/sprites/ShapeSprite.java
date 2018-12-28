package game_advanced.sprites;

import game_advanced.SerializableColor;
import java.io.Serializable;
import javafx.scene.paint.Color;

public abstract class ShapeSprite extends Sprite implements Serializable {

  protected transient Color color;
  private SerializableColor serializableColor;

  public ShapeSprite(ShapeSprite sprite) {
    super(sprite.x, sprite.y, sprite.z);
    color = sprite.color;
    serializableColor = sprite.serializableColor;
  }

  public ShapeSprite(double x, double y, int z, Color color) {
    super(x, y, z);
    this.color = color;
    this.serializableColor = new SerializableColor(color);
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
    this.serializableColor.setColor(color);
  }

  public abstract double getCircumscribedCircleRadius();
}
