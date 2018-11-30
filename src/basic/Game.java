package basic;

import java.util.ArrayList;
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
  private ArrayList<GameObject> objects;
  private ArrayList<Planet> planets;
  
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
    
    objects = new ArrayList<>();
    planets = new ArrayList<>();
    generatePlanets();

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
        objects.forEach(s -> s.render(gc));

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
    for (int i = 0; i < NB_PLANETS; i++) {
      int radius = random.nextInt(Planet.MAX_RADIUS - Planet.MIN_RADIUS) + Planet.MIN_RADIUS;
      int xPos = random.nextInt(WIDTH - radius * 2) + radius;
      int yPos = random.nextInt(HEIGHT - radius * 2) + radius;
      boolean isIllegal = false;
      Planet newPlanet = new Planet(radius, xPos, yPos);
      for (Planet p : planets) {
        double distance = Math.sqrt(
            ((xPos - p.getSprite().x) * (xPos - p.getSprite().x)) + ((yPos - p.getSprite().y) * (
                yPos - p.getSprite().y)));
        distance -= radius + p.getRadius();
        if (distance < MIN_PLANET_DISTANCE) {
          i--;
          isIllegal = true;
          break;
        }

      }

      if (!isIllegal) {
        planets.add(newPlanet);
        objects.add(newPlanet);
      }
    }
  }
}
