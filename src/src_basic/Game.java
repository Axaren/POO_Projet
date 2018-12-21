package src_basic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
import src_basic.Player.PlayerType;
import src_basic.game_objects.Planet;
import src_basic.game_objects.Spaceship;
import src_basic.pathfinding.Graph;
import src_basic.pathfinding.Node;
import src_basic.pathfinding.PathFinder;
import src_basic.sprites.CircleSprite;

public class Game extends Application implements Serializable {

  private final static int WIDTH = 800;
  private final static int HEIGHT = 600;
  private final static int NB_PLANETS = 20;
  private final static int MIN_PLANET_DISTANCE = 50;

  private final String SAVE_FILE = "save.txt";

  private transient Image background;

  private ArrayList<Player> players;
  private ArrayList<Planet> planets;
  private ArrayList<Squad> squads;

  private PathFinder pathFinder;
  private Planet selectedPlanet;


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
    Player player1 = new Player(Color.BLUE, PlayerType.HUMAN, planets.get(0));
    players.add(player1);
    Player ai = new Player(Color.RED, PlayerType.AI, planets.get(planets.size() - 1));
    players.add(ai);
    generateGrid();
    stage.setScene(scene);
    stage.show();

    EventHandler<MouseEvent> mouseHandler = (e -> {
      if (debugMode) {
        System.out.println("Clicked at coordinates (" + e.getX() + "," + e.getY() + ")");
      }
      if (selectedPlanet != null) {
        planets.stream()
            .filter(p -> p.onPlanet((int) e.getX(), (int) e.getY()) && !p.equals(selectedPlanet))
            .findFirst()
            .ifPresent(planet -> selectedPlanet.createSquad(percentage, planet, pathFinder));
      } else {
        selectedPlanet = planets.stream().filter(
            p -> p.onPlanet((int) e.getX(), (int) e.getY()) && p.getPlayer() == players.get(0))
            .findFirst().orElse(null);
      }
    });

    scene.setOnMouseDragged(mouseHandler);
    scene.setOnMousePressed(mouseHandler);

    scene.setOnScroll(e -> percentage++);

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
      private double planetProductionAcc;
      private double spaceShipAcc;

      public void handle(long now) {
        double delta = (now - previousTime) / 1_000_000_000.0;
        planetProductionAcc += delta;
        spaceShipAcc += delta;
        gc.drawImage(background, 0, 0);

        if (spaceShipAcc >= 0.25) {
          for (Squad squad : squads) {
            squad.getSpaceships().forEach(Spaceship::update);
          }
          for (Squad squad : squads) {
            squad.getSpaceships().removeIf(spaceship -> spaceship.getSquad() == null);
          }
          spaceShipAcc = 0;
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

        planets.forEach(p -> p.render(gc));

        if (debugMode) {
          pathFinder.renderMap(gc);
        }
        previousTime = now;

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
              planet.getRadius() + (Spaceship.WIDTH >= Spaceship.HEIGHT ? Spaceship.WIDTH
                  : Spaceship.HEIGHT));
          return biggerPlanetSprite.contains(finalX, finalY);
        });
        column.add(new Node(x, y, blocked));
      }
      map.add(column);
    }
    pathFinder = new PathFinder(map);
  }

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
        Player player = (Player) obj;
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
        Squad saved = (Squad) obj;
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
