package src_basic;

import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 * Used to Serialize a Javafx Color
 */
public class SerializableColor implements Serializable {

  private double red;
  private double green;
  private double blue;
  private double opacity;

  public SerializableColor(Color color) {
    this.red = color.getRed();
    this.green = color.getGreen();
    this.blue = color.getBlue();
    this.opacity = color.getOpacity();
  }

  public Color getColor() {
    return Color.color(red, green, blue, opacity);
  }

  public void setColor(Color color) {
    this.red = color.getRed();
    this.green = color.getGreen();
    this.blue = color.getBlue();
    this.opacity = color.getOpacity();
  }
}
