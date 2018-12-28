package game_advanced.game_objects;

import game_advanced.Squad;
import game_advanced.game_objects.planets.Planet;
import game_advanced.pathfinding.Graph;
import game_advanced.pathfinding.Node;
import game_advanced.sprites.CircleSprite;
import game_advanced.sprites.RectangleSprite;
import game_advanced.sprites.ShapeSprite;
import java.io.Serializable;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a Spaceship in the game_basic
 */
public class Spaceship extends GameObject implements Serializable {

  /**
   * As there is only 1 type of spaceship all stats are fixed and common to all spaceships
   */
  private SpaceshipType type;
  private Squad squad;
  private transient LinkedList<Node> path;
  private int nextNodeRemainingDistance;

  public Spaceship(double x, double y, Color color, Squad squad, LinkedList<Node> path,
      SpaceshipType type) {
    super(type.sprite);
    type.sprite.setPosition(x, y);
    type.sprite.setColor(color);
    this.squad = squad;
    this.path = new LinkedList<>(path);
  }

  public Squad getSquad() {
    return squad;
  }

  public void setPath(LinkedList<Node> path) {
    this.path = path;
  }

  public Planet getDestination() {
    return squad.getDestination();
  }

  /**
   * Updates the spaceship position and follows its assigned path. Changes its course each time it
   * reaches a new checkpoint and removes the current destination. Notifies the destination planet
   * when it reaches it.
   */
  @Override
  public void update(double delta) {
    if (squad != null) {
      sprite.updatePosition(delta);
      nextNodeRemainingDistance -= type.speed;
      if (!path.isEmpty() && nextNodeRemainingDistance <= 0) {
        Node currentNode = path.removeFirst();
        if (!path.isEmpty()) {
          Node nextNode = path.peek();
          nextNodeRemainingDistance = currentNode.distanceTo(nextNode);
          double newAngle = Math.atan2(Graph.getRealNodeY(nextNode) - sprite.getY(),
              Graph.getRealNodeX(nextNode) - sprite.getX());
          sprite.setSpeed((type.speed * Math.cos(newAngle)),
              (type.speed * Math.sin(newAngle)));
        } else {
          onDestinationReached();
        }
      }
    }
  }

  @Override
  public String toString() {
    return "Spaceship{" +
        "type=" + type +
        ", squad=" + squad +
        ", path=" + path +
        ", nextNodeRemainingDistance=" + nextNodeRemainingDistance +
        ", sprite=" + sprite +
        '}';
  }

  @Override
  public void render(GraphicsContext gc) {
    sprite.render(gc);
  }

  /**
   * When the spaceship reaches its destination, informs the planet and removes itself from its
   * squad, then waits for the garbage collector to destroy it
   */
  private void onDestinationReached() {
    squad.getDestination().onLanding(this);
    squad = null;
  }

  public Color getColor() {
    return ((RectangleSprite) sprite).getColor();
  }

  public void setColor(Color color) {
    ((RectangleSprite) sprite).setColor(color);
  }

  /**
   * Defines all attributes of a spaceship of a certain type
   */
  public enum SpaceshipType {
    ZERG(new RectangleSprite(0, 0, 0, 5, 5, Color.BLACK), 1, 10, 1),
    TANK(new CircleSprite(0, 0, 0, 5, Color.BLACK), 2, 5, 2);

    public ShapeSprite sprite;
    public int attackPower;
    public double speed;
    public double productionCost;

    SpaceshipType(ShapeSprite sprite, int attackPower, double speed, double productionCost) {
      this.sprite = sprite;
      this.attackPower = attackPower;
      this.speed = speed;
      this.productionCost = productionCost;
    }
  }

}
