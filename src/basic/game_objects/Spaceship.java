package basic.game_objects;

import basic.Squad;
import basic.pathfinding.Graph;
import basic.pathfinding.Node;
import basic.sprites.RectangleSprite;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a Spaceship in the game
 */
public class Spaceship extends GameObject {

  /**
   * As there is only 1 type of spaceship all stats are fixed and common to all spaceships
   */
  public final static double SPEED = 10;
  public final static double CREATION_TIME = 1;
  public final static int ATTACK_POWER = 1;
  public final static int HEIGHT = 10;
  public final static int WIDTH = 10;

  private Squad squad;
  private LinkedList<Node> path;
  private int nextNodeRemainingDistance;


  public Spaceship(int x, int y, Color color, Squad squad, LinkedList<Node> path) {
    super(new RectangleSprite(x, y, 0, WIDTH, HEIGHT, color));
    this.squad = squad;
    this.path = path;
  }

  public void setPath(LinkedList<Node> path) {
    this.path = path;
  }

  public Planet getDestination() {
    return squad.getDestination();
  }

  /**
   * Moves the spaceship according to its speed vector.
   */
  private void updatePosition() {
    sprite.updatePosition();
  }

  /**
   * Updates the spaceship position and follows its assigned path. Changes its course each time it
   * reaches a new checkpoint in its path.
   */
  @Override
  public void update() {
    if (squad != null) {
      updatePosition();
      nextNodeRemainingDistance -= Spaceship.SPEED;
      if (nextNodeRemainingDistance <= 0) {
        Node currentNode = path.removeFirst();
        if (!path.isEmpty()) {
          Node nextNode = path.peek();
          nextNodeRemainingDistance = currentNode.distanceTo(nextNode);
          double newAngle = Math.atan2(sprite.getY() - Graph.getRealNodeY(nextNode),
              sprite.getX() - Graph.getRealNodeX(nextNode));
          sprite.setSpeed((int) (Spaceship.SPEED * Math.cos(newAngle)),
              (int) (Spaceship.SPEED * Math.sin(newAngle)));
        } else {
          onDestinationReached();
        }
      }
    }
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
    squad.remove(this);
    squad = null;
  }

  public Color getColor() {
    return ((RectangleSprite) sprite).getColor();
  }

}
