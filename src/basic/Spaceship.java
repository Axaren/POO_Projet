package basic;


public class Spaceship {

  static double speed = 10;
  static double creationTime = 1;
  static int attackPower = 1;
  private Squad squad;

  public Spaceship(Squad squad) {
    this.squad = squad;
  }

  public Planet getDestination() {
    return squad.getDestination();
  }

}
