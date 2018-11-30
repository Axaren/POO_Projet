package basic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Spaceship extends GameObject{

  static double speed = 10;
  static double creationTime;
  static int attackPower = 1;
  static int height = 10;
  static int width = 10;

  public Spaceship(int x, int y, Color color) {
	  super(new RectangleSprite(x,y,0, width, height, color));
	  sprite.setSpeed(10, 0);
  }

  public double getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(double creationTime) {
    this.creationTime = creationTime;
  }
  
  public void updatePosition(){
	  sprite.updatePosition();
  }
  
  @Override
  public void render(GraphicsContext gc) {
    sprite.render(gc);
  }

}
