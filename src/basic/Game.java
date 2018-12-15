package basic;

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
import javafx.stage.Stage;

public class Game extends Application {

  private Player player1, player2;

  private final static int WIDTH = 800;
  private final static int HEIGHT = 600;
  private final static int NB_PLANETS = 10;
  private final static int MIN_PLANET_DISTANCE = 50;

  private Image background;
  private ArrayList<Planet> planets;
  private ArrayList<Spaceship> spaceships;
  private List<List<Node>> map;
  
  private int cpt;
  
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

    planets = new ArrayList<>();
    generatePlanets();
    map = mapToGrid();
    PathFinder pathFinder = new PathFinder();
    //List<Node> pathFound = pathFinder.findPath(map,map.get(0).get(0),map);
    //pathFinder.printPath(map,pathFound);


    stage.setScene(scene);
    stage.show();

    EventHandler<MouseEvent> mouseHandler = (e -> {
    	for(Planet p : planets)
    		if(p.onPlanet(e.getX(), e.getY())){
				p.creationSpaceship();
    		}
    });

    scene.setOnMouseDragged(mouseHandler);
    scene.setOnMousePressed(mouseHandler);

    scene.setOnKeyPressed(e -> {
    });

    new AnimationTimer() {

      private long previousTime;
      private double planetProductionAcc;

      public void handle(long now) {
        double delta = now - previousTime / 1_000_000_000.0;
        planetProductionAcc += delta;
        gc.drawImage(background, 0, 0);
        previousTime = now;
        planets.forEach(p -> p.render(gc));
        for (List<Node> line : map) {
          line.forEach(node -> node.render(gc));
        }
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
            ((xPos - p.getSprite().x) * (xPos - p.getSprite().x)) + ((yPos - p.getSprite().y) * (
                yPos - p.getSprite().y)));
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

  public List<List<Node>> mapToGrid() {
    int nodeColumnsNumber = 30;
    int nodeLinesNumber = 25;

    int nodesHorizontalDistance = WIDTH / nodeColumnsNumber;
    int nodesVerticalDistance = HEIGHT / nodeLinesNumber;

    List<List<Node>> map = new ArrayList<>(nodeColumnsNumber * nodeLinesNumber);

    for (int y = 0; y < HEIGHT; y += nodesVerticalDistance) {
      List<Node> column = new ArrayList<>(nodeLinesNumber);
      for (int x = 0; x < WIDTH; x += nodesHorizontalDistance) {
        int finalX = x;
        int finalY = y;
        boolean blocked = planets.stream()
            .anyMatch(planet -> planet.getSprite().contains(finalX, finalY));
        column.add(new Node(x, y, blocked));
      }
      map.add(column);
    }
    return map;
  }
}
