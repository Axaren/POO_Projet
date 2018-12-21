package basic.game_objects;

import basic.Squad;
import basic.pathfinding.Graph;
import basic.pathfinding.Node;
import basic.sprites.RectangleSprite;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Spaceship extends GameObject {

  public static double speed = 10;
  public static double creationTime = 1;
  public static int attackPower = 1;
  public static int height = 10;
  public static int width = 10;

  private Squad squad;
  private LinkedList<Node> path;
  private int nextNodeRemainingDistance;


  public Spaceship(int x, int y, Color color, Squad squad, LinkedList<Node> path) {
    super(new RectangleSprite(x, y, 0, width, height, color));
    this.squad = squad;
    this.path = path;
  }

  public void setPath(LinkedList<Node> path) {
    this.path = path;
  }

  public Planet getDestination() {
    return squad.getDestination();
  }

  private void updatePosition() {
    sprite.updatePosition();
  }

  @Override
  public void update() {
    updatePosition();
    nextNodeRemainingDistance -= Spaceship.speed;
    if (nextNodeRemainingDistance <= 0) {
      Node currentNode = path.removeFirst();
      if (!path.isEmpty()) {
        Node nextNode = path.peek();
        nextNodeRemainingDistance = currentNode.distanceTo(nextNode);
        double newAngle = Math.atan2(sprite.getY() - Graph.getRealNodeY(nextNode),
            sprite.getX() - Graph.getRealNodeX(nextNode));
        sprite.setSpeed((int) (Spaceship.speed * Math.cos(newAngle)),
            (int) (Spaceship.speed * Math.sin(newAngle)));
      } else {
        onDestinationReached();
      }
    }
  }

  @Override
  public void render(GraphicsContext gc) {
    sprite.render(gc);
  }

  private void onDestinationReached() {
    squad.getDestination().onAttack();
    squad.remove(this);
  }

  public Color getColor() {
    return ((RectangleSprite) sprite).getColor();
  }

}
