package game_advanced;

import game_advanced.ai.AI;
import game_advanced.ai.AI.AIAlgorithm;
import game_advanced.game_objects.Spaceship;
import game_advanced.game_objects.Spaceship.SpaceshipType;
import game_advanced.game_objects.planets.Planet;
import game_advanced.game_objects.planets.SickPlanet;
import game_advanced.pathfinding.Graph;
import game_advanced.pathfinding.Node;
import game_advanced.pathfinding.PathFinder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * Main class of the game_basic Contains the game_basic loop.
 */
public class Game extends Application implements Serializable {

  private final static int WIDTH = 800;
  private final static int HEIGHT = 600;
  private final static int NB_PLANETS = 20;
  private final static int MAX_SPACESHIP_SIZE = 20;
  private final static int MIN_PLANET_DISTANCE = Planet.MAX_RADIUS;

  private final String SAVE_FILE = "save.txt";

  private transient Image background;

  private ArrayList<game_advanced.Player> players;
  private ArrayList<Planet> planets;
  private ArrayList<game_advanced.Squad> squads;

  private PathFinder pathFinder;
  private Planet selectedOrigin;


  private transient int cpt;
  private transient int percentage;

  private boolean debugMode;

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
    percentage = 50;

    planets = new ArrayList<>();
    squads = new ArrayList<>();
    players = new ArrayList<>();

    generatePlanets();
    game_advanced.Player player1 = new game_advanced.Player(Color.BLUE, planets.get(0));
    players.add(player1);
    game_advanced.Player ai = new AI(Color.RED, planets.get(1), AIAlgorithm.CLOSEST);
    players.add(ai);
    generateGrid();
    stage.setScene(scene);
    stage.show();

    scene.setOnMousePressed(e -> {
      if (debugMode) {
        System.out.println("Clicked at coordinates (" + e.getX() + "," + e.getY() + ")");
      }
      if (selectedOrigin == null) {
        selectedOrigin = planets.stream().filter(
            p -> p.onPlanet((int) e.getX(), (int) e.getY())
                && p.getPlayer() == player1)
            .findFirst().orElse(null);
      }
    });

    scene.setOnMouseReleased(e -> {
      System.out.println("Released at coordinates (" + e.getX() + "," + e.getY() + ")");
      if (selectedOrigin != null) {
        planets.stream()
            .filter(p -> p.onPlanet((int) e.getX(), (int) e.getY()) && !p.equals(selectedOrigin))
            .findFirst()
            .ifPresent(planet -> {
              int planetPercentage = e.isShiftDown() ? 100 : percentage;
              squads.add(selectedOrigin.createSquad(planetPercentage, planet, pathFinder));
              selectedOrigin = null;
            });
      }

    });

    scene.setOnScroll(e -> {
      percentage += e.getDeltaY() % 100;
    });

    scene.setOnKeyPressed(e -> {
      if (debugMode) {
        System.out.println("Key pressed -> " + e.getCode().getName());
      }
      switch (e.getCode()) {
        case W:
          saveGame();
          break;
        case X:
          loadGame();
          break;
        case T:
          debugMode = !debugMode;
          break;
      }
    });

    new AnimationTimer() {

      private long previousTime;

      public void handle(long now) {
        double delta = (now - previousTime) / 1_000_000_000.0;
        update(delta);
        render();
        previousTime = now;
      }

      @Override
      public void start() {
        previousTime = System.nanoTime();
        super.start();
      }

      private void update(double delta) {
        planets.forEach(planet -> planet.update(delta));
        players.stream().filter(player -> player instanceof AI).collect(Collectors.toList())
            .forEach(ai -> ((AI) ai).onActionRequest(planets, pathFinder, delta));
        squads.forEach(squad -> squad.update(delta));
      }

      private void render() {
        gc.drawImage(background, 0, 0);
        planets.forEach(p -> p.render(gc));
        if (debugMode) {
          pathFinder.renderMap(gc);
        }
        squads.forEach(squad -> squad.render(gc));
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
      boolean isSick = random.nextBoolean();
      SpaceshipType producedType = SpaceshipType.values()[random
          .nextInt(SpaceshipType.values().length)];
      boolean isIllegal = false;

      Planet newPlanet;
      if (isSick) {
        newPlanet = new SickPlanet(radius, xPos, yPos, nbSpaceships, producedType);
      } else {
        newPlanet = new Planet(radius, xPos, yPos, nbSpaceships, producedType);
      }

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
          Planet biggerPlanet = new Planet(planet);
          biggerPlanet.setRadius(biggerPlanet.getRadius()
              + MAX_SPACESHIP_SIZE);
          return biggerPlanet.onPlanet(finalX, finalY);
        });
        column.add(new Node(x, y, blocked));
      }
      map.add(column);
    }
    pathFinder = new PathFinder(map);
  }

  /**
   * Saves the game_basic state to a file defined in SAVE_GAME
   */
  public void saveGame() {
    try {
      FileOutputStream saveFile = new FileOutputStream(SAVE_FILE);
      ObjectOutputStream saveStream = new ObjectOutputStream(saveFile);

      saveStream.writeObject(players);
      saveStream.writeObject(planets);
      saveStream.writeObject(squads);
      saveStream.writeObject(pathFinder);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadGame() {
    try {
      FileInputStream saveFile = new FileInputStream(SAVE_FILE);
      ObjectInputStream loadStream = new ObjectInputStream(saveFile);

      ArrayList<?> savedPlayers = (ArrayList<?>) loadStream.readObject();
      players.clear();
      for (Object obj : savedPlayers) {
        game_advanced.Player player = (game_advanced.Player) obj;
        player.setColor(((Player) obj).getSerializableColor().getColor());
        players.add(player);
      }

      planets.clear();
      ArrayList<?> savedPlanets = (ArrayList<?>) loadStream.readObject();
      for (Object obj : savedPlanets) {
        Planet saved = (Planet) obj;
        if (saved.getPlayer() != null) {
          saved.setColor(saved.getPlayer().getSerializableColor().getColor());
        } else {
          saved.setColor(Planet.DEFAULT_COLOR);
        }
        planets.add(saved);

      }

      squads.clear();
      ArrayList<?> savedSquads = (ArrayList<?>) loadStream.readObject();
      for (Object obj : savedSquads) {
        game_advanced.Squad saved = (game_advanced.Squad) obj;
        for (Spaceship spaceship : saved.getSpaceships()) {
          spaceship.setColor(saved.getOrigin().getPlayer().getSerializableColor().getColor());
        }
        squads.add((Squad) obj);
      }

      pathFinder = (PathFinder) loadStream.readObject();
      background = new Image(getRessourcePathByName("images/space.jpg"), WIDTH, HEIGHT, false,
          false);

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

}
