package basic;


public class Spaceship {

  private double speed;
  private double creationTime;
  private int attackPower;

  public Spaceship(double speed, double creationTime, int attackPower) {
    this.speed = speed;
    this.creationTime = creationTime;
    this.attackPower = attackPower;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public double getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(double creationTime) {
    this.creationTime = creationTime;
  }

  public int getAttackPower() {
    return attackPower;
  }

  public void setAttackPower(int attackPower) {
    this.attackPower = attackPower;
  }
}
