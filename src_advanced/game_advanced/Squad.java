package game_advanced;

import game_advanced.game_objects.Spaceship;
import game_advanced.game_objects.Spaceship.SpaceshipType;
import game_advanced.game_objects.planets.Planet;
import game_advanced.pathfinding.Graph;
import game_advanced.pathfinding.Node;
import game_advanced.pathfinding.PathFinder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a squadron of spaceships Makes spaceships take off its origin planet and calculates
 * their game_basic.pathfinding.
 */
public class Squad implements Serializable {

  private ArrayList<Spaceship> spaceships;
  private Planet origin;
  private Planet destination;
  private PathFinder pathFinder;
  private LinkedList<Node> currentPath;
  private int waitingSpaceships;
  private LaunchParameters parameters;
  private double spaceshipsUpdateAcc;
  private double launchAcc;

  /**
   * @param origin planet where the squad is coming from
   * @param destination targeted planet either ally or enemy
   * @param squadSize desired number of spaceships in the squad
   * @param pathFinder class holding the map as a 2D grid of nodes, used to calculate
   * game_basic.pathfinding
   */
  public Squad(Planet origin, Planet destination, int squadSize, PathFinder pathFinder) {
    this.origin = origin;
    this.destination = destination;
    this.spaceships = new ArrayList<>();
    this.pathFinder = pathFinder;
    this.waitingSpaceships = squadSize;
    spaceshipsUpdateAcc = 0;
    launchAcc = 0;

    double originX = origin.getSprite().getX();
    double originY = origin.getSprite().getY();
    double destinationX = destination.getSprite().getX();
    double destinationY = destination.getSprite().getY();

    double takeOffAngle = Math.atan2(destinationY - originY, destinationX - originX);

    SpaceshipType type = origin.getProducedType();

    Node squadStartNode = getClosestNode(this.origin, originX, originY,
        takeOffAngle, type);
    Node squadDestNode = getClosestNode(this.destination, destinationX,
        destinationY, Math.atan2(originY - destinationY, originX - destinationX), type);

    currentPath = (LinkedList<Node>) pathFinder.findPath(squadStartNode, squadDestNode);
    if (currentPath == null) {
      return;
    }

    double spaceshipsDistance = type.sprite.getCircumscribedCircleRadius() * 2;
    double deltaTheta = Math.acos(
        ((spaceshipsDistance * spaceshipsDistance - origin.getRadius() * origin.getRadius()) * 2) /
            (origin.getRadius() * origin.getRadius() * -2));
    int maxPerWave = (int) (Math.PI / deltaTheta);
    double wavesTimeInterval = spaceshipsDistance / type.speed;

    parameters = new LaunchParameters(squadStartNode, squadDestNode, spaceshipsDistance,
        takeOffAngle, deltaTheta, maxPerWave, wavesTimeInterval);
    launchWave();
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
  private Node getClosestNode(Planet planet, double x, double y, double angle, SpaceshipType type) {
    int xOffset = (int) (planet.getRadius() + type.sprite.getCircumscribedCircleRadius() * 2 * Math
        .cos(angle));
    int yOffset = (int) (planet.getRadius() + type.sprite.getCircumscribedCircleRadius() * 2 * Math
        .sin(angle));
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

  public void update(double delta) {
    if (waitingSpaceships > 0) {
      launchAcc += delta;
      if (launchAcc >= parameters.wavesTimeInterval) {
        launchWave();
        launchAcc -= parameters.wavesTimeInterval;
      }
    }
    spaceshipsUpdateAcc += delta;
    if (spaceshipsUpdateAcc >= 0.1) {
      Iterator<Spaceship> it = spaceships.iterator();
      while (it.hasNext()) {
        Spaceship s = it.next();
        s.update(spaceshipsUpdateAcc);
        if (s.getSquad() == null) {
          it.remove();
        }
      }
      spaceshipsUpdateAcc -= 0.1;
    }
  }

  public void render(GraphicsContext gc) {
    spaceships.forEach(spaceship -> spaceship.render(gc));
  }

  private void launchWave() {
    int i = 0;
    double startingAngle = (parameters.takeOffAngle - Math.PI / 2) % Math.PI * 2;
    double theta;
    double startingDistance = origin.getRadius()
        + origin.getProducedType().sprite.getCircumscribedCircleRadius();

    while (i < parameters.maxWaveSize && waitingSpaceships > 0) {
      theta = (startingAngle + parameters.deltaTheta * i) % Math.PI * 2;
      double xPos = origin.getSprite().getX() + Math.cos(theta) * startingDistance;
      double yPos = origin.getSprite().getY() + Math.sin(theta) * startingDistance;

      Spaceship newSpaceship = new Spaceship(xPos, yPos, origin.getColor(), this, currentPath,
          origin.getProducedType());
      add(newSpaceship);

      i++;
      waitingSpaceships--;
    }
    origin.setNbSpaceship(origin.getNbSpaceship() - i);
  }

  private class LaunchParameters implements Serializable {

    public Node startNode;
    public Node destNode;
    public double spaceshipsDistance;
    public double takeOffAngle;
    public double deltaTheta;
    public int maxWaveSize;
    public double wavesTimeInterval;

    public LaunchParameters(Node startNode, Node destNode, double spaceshipsDistance,
        double takeOffAngle, double deltaTheta, int maxWaveSize, double wavesTimeInterval) {
      this.startNode = startNode;
      this.destNode = destNode;
      this.spaceshipsDistance = spaceshipsDistance;
      this.takeOffAngle = takeOffAngle;
      this.deltaTheta = deltaTheta;
      this.maxWaveSize = maxWaveSize;
      this.wavesTimeInterval = wavesTimeInterval;

    }
  }
}
