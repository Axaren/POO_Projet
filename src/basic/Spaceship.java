package basic;


public class Spaceship {

  static double speed = 10;
  private double creationTime;
  static int attackPower = 10;
  private RectangleSprite sprite;

  public Spaceship(double creationTime) {
    this.creationTime = creationTime;
  }

  public double getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(double creationTime) {
    this.creationTime = creationTime;
  }
}
