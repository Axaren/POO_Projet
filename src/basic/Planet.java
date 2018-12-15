package basic;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Planet extends GameObject {

  static final Color DEFAULT_COLOR = Color.CHOCOLATE;
  static final int MAX_RADIUS = 50;
  static final int MIN_RADIUS = 25;
  private final static int MIN_FONT_SIZE = 8;
  private final static int MAX_FONT_SIZE = 20;

  private int nbSpaceship;
  private int power;
  private Player player;
  private int productionRate;

  public Planet(int radius, Player player, int xPos, int yPos) {
    super(new CircleSprite(xPos, yPos, 0, radius, player.getColor()));
    this.player = player;
    this.nbSpaceship = 0;
    this.productionRate = radius;
  }

  public Planet(int radius, int xPos, int yPos, int nbSpaceship) {
    super(new CircleSprite(xPos, yPos, 0, radius, DEFAULT_COLOR));
    this.player = null;
    this.nbSpaceship = nbSpaceship;
    this.power = nbSpaceship * Spaceship.attackPower;
    this.productionRate = radius;
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
    return getRadius() == planet.getRadius() &&
        nbSpaceship == planet.nbSpaceship &&
        power == planet.power &&
        productionRate == planet.productionRate &&
        (player == null && planet.player == null || (player != null && player
            .equals(planet.player)))
        &&
        sprite.equals(planet.sprite);
  }

  public int getRadius() {
    return ((CircleSprite) sprite).getRadius();
  }

  public Color getColor() {
    return ((CircleSprite) sprite).getColor();
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

  //Production d'un vaisseau dans la planète
  public void productionSpaceship(){  
  	nbSpaceship++;
  	power += Spaceship.attackPower;
  }
  
  //Création d'un vaisseau pour décollage
  /*public void creationSpaceship(Squad squad){
		  nbSpaceship -= squad.getSpaceships().size();
		  power -= Spaceship.attackPower*squad.getSpaceships().size();
  }*/
  
  public void creationSpaceship(){
	  nbSpaceship --;
	  power -= Spaceship.attackPower;
}
  
  public boolean onPlanet(double x, double y){
    return x > sprite.x && x < sprite.x + this.getRadius() && y > sprite.y && y < sprite.y + this
        .getRadius();
  }
  
  
  @Override
  public void render(GraphicsContext gc) {
    renderPlanet(gc);
    renderPower(gc);
  }

  private void renderPlanet(GraphicsContext gc) {
    sprite.render(gc);
  }

  private void renderPower(GraphicsContext gc) {
    int radiusRange = MAX_RADIUS - MIN_RADIUS;
    int fontSizeRange = MAX_FONT_SIZE - MIN_FONT_SIZE;
    int fontSize = (((getRadius() - MIN_RADIUS) * fontSizeRange) / radiusRange) + MIN_FONT_SIZE;
    Font powerFont = Font.font("Helvetica", FontWeight.NORMAL, fontSize);
    FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(powerFont);
    gc.setFont(powerFont);
    gc.setFill(Color.WHITE);
    gc.setStroke(Color.WHITE);
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setLineWidth(1);
    int textXPos = sprite.x;
    int textYPos = sprite.y;
    gc.fillText(String.valueOf(power), textXPos, textYPos, getRadius());
    gc.strokeText(String.valueOf(power), textXPos, textYPos);
  }
}
