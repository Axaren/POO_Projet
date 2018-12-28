package game_advanced.game_objects.planets;

import game_advanced.Player;
import game_advanced.game_objects.Spaceship.SpaceshipType;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SickPlanet extends Planet {

  public SickPlanet(int radius, Player player, int xPos, int yPos,
      SpaceshipType producedType) {
    super(radius, player, xPos, yPos, producedType);
  }

  public SickPlanet(int radius, int xPos, int yPos, int nbSpaceship,
      SpaceshipType producedType) {
    super(radius, xPos, yPos, nbSpaceship, producedType);
  }

  public SickPlanet(Planet planet) {
    super(planet);
  }

  @Override
  public void update(double delta) {
    Random random = new Random();
    setProductionRate(getProductionRate() + (random.nextInt(2) - 1));
    if (getProductionRate() <= 0) {
      setProductionRate(1);
    }
    super.update(delta);
  }

  @Override
  protected void setPowerTextColor(GraphicsContext gc) {
    gc.setFill(Color.GREEN);
    gc.setStroke(Color.GREEN);
  }
}
