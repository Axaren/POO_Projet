package basic;

import basic.Player.PlayerType;
import basic.game_objects.GameObject;
import basic.game_objects.Planet;
import basic.game_objects.Spaceship;
import basic.pathfinding.Graph;
import basic.pathfinding.Node;
import basic.pathfinding.PathFinder;
import basic.sprites.CircleSprite;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game extends Application {

  private final static int WIDTH = 800;
  private final static int HEIGHT = 600;
  private final static int NB_PLANETS = 20;
  private final static int MIN_PLANET_DISTANCE = 50;
  private ArrayList<Player> players;
  private Image background;
  private ArrayList<GameObject> objects;
  private ArrayList<Planet> planets;


  private ArrayList<Squad> squads;
  private PathFinder pathFinder;

  private int cpt;
  private int percentage;

  /**
   * @param name File path
   * @return Complete file path of the ressource
   */
  private static String getRessourcePathByName(String name) {
    return Game.class.getResource('/' + name).toString();
  }

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage stage) {

    stage.setTitle("Projet POO");
    stage.setResizable(false);

    Group root = new Group();
    Scene scene = new Scene(root);
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    root.getChildren().add(canvas);

    GraphicsContext gc = canvas.getGraphicsContext2D();

    background = new Image(getRessourcePathByName("images/space.jpg"), WIDTH, HEIGHT, false, false);

    cpt = 0;
    percentage = 0;

    objects = new ArrayList<>();
    planets = new ArrayList<>();
    squads = new ArrayList<>();
    players = new ArrayList<>();

    generatePlanets();
    Player player1 = new Player(Color.BLUE, PlayerType.HUMAN, planets.get(0));
    players.add(player1);
    generateGrid();
    //squads.add(new Squad(player1.getControlledPlanets().get(0),planets.get(5),1,pathFinder));

    stage.setScene(scene);
    stage.show();

    EventHandler<MouseEvent> mouseHandler = (e -> {
      //planets.stream().filter(p -> p.onPlanet((int)e.getX(),(int)e.getY())).findFirst()
      // .ifPresent(planet -> planet.createSquad(TODO: pass origin, destination, pathfinder));
    });

    scene.setOnMouseDragged(mouseHandler);
    scene.setOnMousePressed(mouseHandler);

    scene.setOnScroll(e -> percentage++);

    scene.setOnKeyPressed(e -> {
    });

    new AnimationTimer() {

      private long previousTime;
      private double planetProductionAcc;

      public void handle(long now) {
        double delta = (now - previousTime) / 1_000_000_000.0;
        planetProductionAcc += delta;
        gc.drawImage(background, 0, 0);
        objects.forEach(s -> s.render(gc));

        if (planetProductionAcc >= 0.5) {
          for (Squad squad : squads) {
            squad.getSpaceships().forEach(Spaceship::update);
          }
        }

        for (Squad squad : squads) {
          squad.getSpaceships().forEach(spaceship -> spaceship.render(gc));
        }

        if (planetProductionAcc >= 0.5) {
          for (Planet p : planets) {
            if (p.getPlayer() != null) {
              p.update();
            }
          }
          planetProductionAcc = 0;
        }

        previousTime = now;
        planets.forEach(p -> p.render(gc));

      }

      @Override
      public void start() {
        previousTime = System.nanoTime();
        planetProductionAcc = 0;
        super.start();
      }
    }.start();
  }

  public void generatePlanets() {
    Random random = new Random();
    int failedTries = 0;
    for (int i = 0; i < NB_PLANETS; i++) {
      int radius = random.nextInt(Planet.MAX_RADIUS - Planet.MIN_RADIUS) + Planet.MIN_RADIUS;
      int xPos = random.nextInt(WIDTH - radius * 2) + radius;
      int yPos = random.nextInt(HEIGHT - radius * 2) + radius;
      int nbSpaceships = random.nextInt(radius) + radius;
      boolean isIllegal = false;
      Planet newPlanet = new Planet(radius, xPos, yPos, nbSpaceships);
      for (Planet p : planets) {
        double distance = Math.sqrt(
            ((xPos - p.getSprite().getX()) * (xPos - p.getSprite().getX())) + (
                (yPos - p.getSprite().getY()) * (
                    yPos - p.getSprite().getY())));
        distance -= radius + p.getRadius();
        if (distance < MIN_PLANET_DISTANCE) {
          i--;
          isIllegal = true;
          failedTries++;
          if (failedTries > 5) {
            return;
          }
          break;
        } else {
          failedTries = 0;
        }

      }

      if (!isIllegal) {
        planets.add(newPlanet);
      }
    }
  }

  private void generateGrid() {
    int nbLines = HEIGHT / Graph.NODE_VERTICAL_DISTANCE;
    int nbColumns = WIDTH / Graph.NODE_HORIZONTAL_DISTANCE;
    List<List<Node>> map = new ArrayList<>(nbLines * nbColumns);

    for (int y = 0; y < nbLines; y++) {
      List<Node> column = new ArrayList<>();
      for (int x = 0; x < nbColumns; x++) {
        int finalX = x * Graph.NODE_HORIZONTAL_DISTANCE;
        int finalY = y * Graph.NODE_VERTICAL_DISTANCE;
        boolean blocked = planets.stream().anyMatch(planet -> {
          CircleSprite biggerPlanetSprite = new CircleSprite((CircleSprite) planet.getSprite());
          biggerPlanetSprite.setRadius(
              planet.getRadius() + Spaceship.WIDTH >= Spaceship.HEIGHT ? Spaceship.WIDTH
                  : Spaceship.HEIGHT);
          return biggerPlanetSprite.contains(finalX, finalY);
        });
        column.add(new Node(x, y, blocked));
      }
      map.add(column);
    }
    pathFinder = new PathFinder(map);
  }
}
