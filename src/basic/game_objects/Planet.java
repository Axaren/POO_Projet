package basic.game_objects;

import basic.Player;
import basic.Squad;
import basic.pathfinding.PathFinder;
import basic.sprites.CircleSprite;
import basic.sprites.Sprite;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Represents a planet in the game
 *
 * @see basic.game_objects.GameObject
 */
public class Planet extends GameObject {

  /**
   * DEFAULT_COLOR is the color of neutral planets
   */
  public static final Color DEFAULT_COLOR = Color.CHOCOLATE;
  public static final int MAX_RADIUS = 50;
  public static final int MIN_RADIUS = 25;
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
    this.productionRate = radius / 10;
  }

  public Planet(int radius, int xPos, int yPos, int nbSpaceship) {
    super(new CircleSprite(xPos, yPos, 0, radius, DEFAULT_COLOR));
    this.player = null;
    this.productionRate = radius / 10;
    this.nbSpaceship = nbSpaceship;
    this.power = this.nbSpaceship * Spaceship.ATTACK_POWER;
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

  public void setColor(Color color) {
    ((CircleSprite) sprite).setColor(color);
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

  /**
   * Sets the planet's player and changes its color to match the player's
   * @param player a player in the game
   */
  public void setPlayer(Player player) {
    this.player = player;
    setColor(player.getColor());
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

  /**
   *
   * @param percentage percentage of spaceships to deploy
   * @param destination the target of the attack
   * @param pathFinder the pathfinder Object used to calculate the path of the spaceships in the map
   * @see basic.pathfinding.PathFinder
   */
  public void createSquad(int percentage, Planet destination, PathFinder pathFinder) {
    int newSquadSize = nbSpaceship * percentage / 100;
    Squad newSquad = new Squad(this, destination, newSquadSize, pathFinder);
    player.addSquad(newSquad);
  }

  /**
   * Checks if a point is contained in the planet
   * @param x coordinate
   * @param y coordinate
   * @return true if the point at (x,y) is contained in the planet
   */
  public boolean onPlanet(int x, int y) {
    return sprite.contains(x, y);
  }


  /**
   * Produces spaceships each tick
   */
  @Override
  public void update() {
    nbSpaceship += this.productionRate / Spaceship.CREATION_TIME;
    power = this.nbSpaceship * Spaceship.ATTACK_POWER;
  }

  /**
   * Renders the planet on the screen and displays its current power
   * @param gc The GraphicsContext of the canvas
   * @see javafx.scene.canvas.GraphicsContext
   */
  @Override
  public void render(GraphicsContext gc) {
    renderPlanet(gc);
    renderPower(gc);
  }

  private void renderPlanet(GraphicsContext gc) {
    sprite.render(gc);
  }

  /**
   * Displays the current planet's power at its center.
   * Adapts the font size with the planet's radius
   * @param gc The GraphicsContext of the Canvas
   * @see javafx.scene.canvas.GraphicsContext
   */
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
    int textXPos = sprite.getX();
    int textYPos = sprite.getY();
    gc.fillText(String.valueOf(power), textXPos, textYPos, getRadius());
    gc.strokeText(String.valueOf(power), textXPos, textYPos);
  }

  /**
   * Decreases the number of spaceships by the attack power of Spaceships
   */
  private void onAttack() {
    nbSpaceship -= Spaceship.ATTACK_POWER;
  }

  /**
   * Moves the spaceship in the planet if it's an ally.
   * @param spaceship the landing spaceship
   */
  public void onLanding(Spaceship spaceship) {
    if (spaceship.getColor() == player.getColor()) {
      nbSpaceship++;
    } else {
      onAttack();
    }
  }
}
