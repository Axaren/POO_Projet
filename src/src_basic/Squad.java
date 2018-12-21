package src_basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import src_basic.game_objects.Planet;
import src_basic.game_objects.Spaceship;
import src_basic.pathfinding.Graph;
import src_basic.pathfinding.Node;
import src_basic.pathfinding.PathFinder;

/**
 * Represents a squadron of spaceships Makes spaceships take off its origin planet and calculates
 * their pathfinding.
 */
public class Squad implements Serializable {

  private ArrayList<Spaceship> spaceships;
  private Planet origin;
  private Planet destination;
  private PathFinder pathFinder;
  private LinkedList<Node> currentPath;
  private int waitingSpaceships;

  /**
   * @param origin planet where the squad is coming from
   * @param destination targeted planet either ally or enemy
   * @param squadSize desired number of spaceships in the squad
   * @param pathFinder class holding the map as a 2D grid of nodes, used to calculate pathfinding
   */
  public Squad(Planet origin, Planet destination, int squadSize, PathFinder pathFinder) {
    this.origin = origin;
    this.destination = destination;
    this.spaceships = new ArrayList<>();
    this.pathFinder = pathFinder;
    this.waitingSpaceships = squadSize;

    int originX = origin.getSprite().getX();
    int originY = origin.getSprite().getY();
    int destinationX = destination.getSprite().getX();
    int destinationY = destination.getSprite().getY();

    double takeOffAngle = Math.atan2(destinationY - originY, destinationX - originX);

    Node squadStartNode = getClosestNode(this.origin, (double) originX, (double) originY,
        takeOffAngle);
    Node squadDestNode = getClosestNode(this.destination, (double) destinationX,
        (double) destinationY, Math.atan2(originY - destinationY, originX - destinationX));

    currentPath = (LinkedList<Node>) pathFinder.findPath(squadStartNode, squadDestNode);
    if (currentPath == null) {
      return;
    }

    for (double theta = takeOffAngle - Math.PI / 2 % Math.PI * 2;
        theta < takeOffAngle + Math.PI / 2 % Math.PI * 2; theta += Math.PI / 8) {
      Node startNode = getClosestNode(origin, originX, originY, theta);
      spaceships.add(new Spaceship(Graph.getRealNodeX(startNode), Graph.getRealNodeY(startNode),
          origin.getColor(), this, currentPath));
      waitingSpaceships--;
      if (waitingSpaceships <= 0) {
        break;
      }
    }
    origin.getPlayer().addSquad(this);

  }

  public Planet getOrigin() {
    return origin;
  }

  public LinkedList<Node> getCurrentPath() {
    return currentPath;
  }

  /**
   * @param x x coordinate of the origin planet
   * @param y y coordinate of the origin planet
   * @param angle angle from the origin to the destination
   * @return The node closest from the point at (x,y)
   */
  private Node getClosestNode(Planet planet, double x, double y, double angle) {
    int xOffset = (int) (planet.getRadius() + Spaceship.WIDTH * 2 * Math.cos(angle));
    int yOffset = (int) (planet.getRadius() + Spaceship.HEIGHT * 2 * Math.sin(angle));
    int startNodeX = (int) Graph.getGraphX(x + xOffset);
    int startNodeY = (int) Graph.getGraphY(y + yOffset);
    return pathFinder.getMap().get(startNodeY).get(startNodeX);
  }

  @Override
  public String toString() {
    return "Squad{" +
        "spaceships=" + spaceships +
        ", origin=" + origin +
        ", destination=" + destination +
        ", waitingSpaceships=" + waitingSpaceships +
        '}';
  }

  public Planet getDestination() {
    return destination;
  }

  public void add(Spaceship spaceship) {
    spaceships.add(spaceship);
  }

  public void changeDestination(Planet destination) {
    if (!this.destination.equals(destination)) {
      this.destination = destination;
    }
  }

  public ArrayList<Spaceship> getSpaceships() {
    return spaceships;
  }

  public void remove(Spaceship spaceship) {
    spaceships.remove(spaceship);
  }
}
