package basic;

import basic.Player.PlayerType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Game extends Application {
	
  private Player player1, player2;

  private final static int WIDTH = 800;
  private final static int HEIGHT = 600;

  private Image background;
  private ArrayList<GameObject> objects;
  private ArrayList<Planet> planets;
  
  private int cpt;
  
  public static String getRessourcePathByName(String name) {
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
    gc.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
    gc.setFill(Color.RED);
    gc.setStroke(Color.RED);
    gc.setLineWidth(1);

    background = new Image(getRessourcePathByName("images/space.jpg"), WIDTH, HEIGHT, false, false);
    Planet planet1 = new Planet(100, 200, 200);
    Planet planet2 = new Planet(100, 300, 300);

    objects = new ArrayList<>();
    objects.add(planet1);
    objects.add(planet2);
    
    planets = new ArrayList<>();
    planets.add(planet1);
    planets.add(planet2);

    cpt = 0;

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
      public void handle(long arg0) {
        gc.drawImage(background, 0, 0);
        cpt++; 
        
        if(cpt%100==0){
            Collections.sort(planets);
            planets.forEach( p -> p.productionSpaceship());
        }


        Collections.sort(objects);
        objects.forEach(s -> s.render(gc));

      }
    }.start();
  }
}
