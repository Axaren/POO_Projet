package basic;

import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.paint.Color;

public class Player {

  private Color color;
  private PlayerType type;
  private ArrayList<Planet> controlledPlanets;
  private ArrayList<Squad> controlledSquads;

  public Player(Color color, PlayerType type, Planet startPlanet) {
    this.color = color;
    this.type = type;
    this.controlledPlanets = new ArrayList<>(1);
    this.controlledPlanets.add(startPlanet);
    this.controlledSquads = new ArrayList<>();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return Objects.equals(color, player.color);
  }

  public enum PlayerType {
    HUMAN,
    AI
  }

}
