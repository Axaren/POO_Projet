package basic;

import basic.game_objects.Planet;
import basic.game_objects.Spaceship;
import basic.pathfinding.Graph;
import basic.pathfinding.Node;
import basic.pathfinding.PathFinder;
import java.util.ArrayList;
import java.util.LinkedList;

public class Squad {

  private ArrayList<Spaceship> spaceships;
  private Planet origin;
  private Planet destination;
  private PathFinder pathFinder;
  private LinkedList<Node> currentPath;
  private int waitingSpaceships;

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

    Node squadStartNode = getSquadStartNode(originX, originY, takeOffAngle);
    Node squadDestNode = getSquadDestNode(destinationX, destinationY,
        takeOffAngle + Math.PI % Math.PI * 2);

    currentPath = (LinkedList<Node>) pathFinder.findPath(squadStartNode, squadDestNode);

    spaceships.add(
        new Spaceship(Graph.getRealNodeX(squadDestNode), Graph.getRealNodeY(squadStartNode),
            origin.getColor(), this, currentPath));
    origin.getPlayer().addSquad(this);

  }

  private Node getSquadStartNode(int originX, int originY, double takeOffAngle) {
    int xOffset = (int) (origin.getRadius() * Spaceship.width * 2 * Math.cos(takeOffAngle));
    int yOffset = (int) (origin.getRadius() * Spaceship.height * 2 * Math.sin(takeOffAngle));
    int startNodeX = Graph.getGraphX(originX + xOffset);
    int startNodeY = Graph.getGraphY(originY + yOffset);
    return pathFinder.getMap().get(startNodeY).get(startNodeX);
  }

  private Node getSquadDestNode(int destinationX, int destinationY, double attackAngle) {
    int offset = 0;
    int destNodeX = Graph.getGraphX(
        (destinationX + (int) (destination.getRadius() * offset * Math.sin(attackAngle))));
    int destNodeY = Graph.getGraphY(
        (destinationY + (int) (destination.getRadius() * offset * Math.cos(attackAngle))));
    Node destNode = pathFinder.getMap().get(destNodeY).get(destNodeX);

    while (destNode.isBlocked()) {
      offset++;
      destNodeX = Graph.getGraphX(
          (destinationX + (int) (destination.getRadius() * offset * Math.sin(attackAngle))));
      destNodeY = Graph.getGraphY(
          (destinationY + (int) (destination.getRadius() * offset * Math.cos(attackAngle))));
      destNode = pathFinder.getMap().get(destNodeY).get(destNodeX);
    }
    return destNode;
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
