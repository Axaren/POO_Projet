package basic;

public class Planet {

  private int radius;
  private int nbSpaceship;
  private int power;
  private Player player;
  private Sprite sprite;
  private int productionRate;

  public Planet(int radius, Player player, int xPos, int yPos) {
    this.radius = radius;
    this.player = player;
    this.nbSpaceship = 0;
    this.productionRate = radius * 2;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Planet planet = (Planet) o;
    return radius == planet.radius &&
        nbSpaceship == planet.nbSpaceship &&
        power == planet.power &&
        productionRate == planet.productionRate &&
        player.equals(planet.player) &&
        sprite.equals(planet.sprite);
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public int getNbSpaceship() {
    return nbSpaceship;
  }

  public void setNbSpaceship(int nbSpaceship) {
    this.nbSpaceship = nbSpaceship;
  }

  public int getPower() {
    return power;
  }

  public void setPower(int power) {
    this.power = power;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Sprite getSprite() {
    return sprite;
  }

  public void setSprite(Sprite sprite) {
    this.sprite = sprite;
  }

  public int getProductionRate() {
    return productionRate;
  }

  public void setProductionRate(int productionRate) {
    this.productionRate = productionRate;
  }

}
