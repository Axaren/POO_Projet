package game_advanced.ai;

import game_advanced.Player;
import game_advanced.game_objects.planets.Planet;
import game_advanced.pathfinding.PathFinder;
import java.util.List;
import javafx.scene.paint.Color;

public class AI extends Player {

  private static double AI_ACTION_INTERVAL = 1;
  private AIAlgorithm algorithm;
  private double accumulator;

  public AI(Color color, Planet startPlanet, AIAlgorithm algorithm) {
    super(color, startPlanet);
    this.algorithm = algorithm;
    accumulator = 0;
  }

  public void onActionRequest(List<Planet> map, PathFinder pathFinder, double delta) {
    accumulator += delta;
    if (accumulator >= AI_ACTION_INTERVAL) {
      switch (algorithm) {
        case RANDOM:
          for (Planet planet : controlledPlanets) {
            planet.createSquad(50,
                map.stream().filter(p -> p.getPlayer() != this).findFirst()
                    .orElseThrow(NullPointerException::new),
                pathFinder);
          }
          break;
        case CLOSEST:
          for (Planet planet : controlledPlanets) {
            double planetX = planet.getSprite().getX();
            double planetY = planet.getSprite().getY();
            double closestSquaredDistance = Double.MAX_VALUE;
            Planet closestPlanet = null;
            for (Planet p : map) {
              double pX = p.getSprite().getX();
              double pY = p.getSprite().getY();
              double squaredDistance =
                  (planetX - pX) * (planetX - pX) + (planetY - pY) * (planetY - pY)
                      - (planet.getRadius() - p.getRadius()) * (planet.getRadius() - p.getRadius());

              if (squaredDistance < closestSquaredDistance) {
                closestSquaredDistance = squaredDistance;
                closestPlanet = p;
              }

              planet.createSquad(50, closestPlanet, pathFinder);
            }
          }
          break;
      }
      accumulator -= AI_ACTION_INTERVAL;
    }
  }

  public enum AIAlgorithm {
    RANDOM,
    CLOSEST,
  }
}
