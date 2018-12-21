package basic;

import basic.game_objects.Planet;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Player {

  private Color color;
  private PlayerType type;

  public Player(Color color, PlayerType type, Planet startPlanet) {
    this.color = color;
    this.type = type;
    this.controlledPlanets = new ArrayList<>(1);
    this.controlledPlanets.add(startPlanet);
    this.controlledSquads = new ArrayList<>();

    startPlanet.setPlayer(this);
  }

  private ArrayList<Planet> controlledPlanets;
  private ArrayList<Squad> controlledSquads;

  public ArrayList<Planet> getControlledPlanets() {
    return controlledPlanets;
  }

  public Color getColor() {
    return color;
  }

  public PlayerType getType() {
    return type;
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
    return color.equals(player.color)
        && this.type == player.type;
  }

  public void addSquad(Squad newSquad) {
    controlledSquads.add(newSquad);
  }

  public enum PlayerType {
    HUMAN,
    AI
  }

}
