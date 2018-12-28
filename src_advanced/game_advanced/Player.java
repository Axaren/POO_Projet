package game_advanced;

import game_advanced.game_objects.planets.Planet;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Player implements Serializable {

  private transient Color color;
  protected ArrayList<Planet> controlledPlanets;
  protected ArrayList<game_advanced.Squad> controlledSquads;
  private game_advanced.SerializableColor serializableColor;

  public Player(Color color, Planet startPlanet) {
    this.color = color;
    this.serializableColor = new game_advanced.SerializableColor(color);
    this.controlledPlanets = new ArrayList<>(1);
    this.controlledPlanets.add(startPlanet);
    this.controlledSquads = new ArrayList<>();

    startPlanet.setPlayer(this);
  }

  public Player(Player player) {
    this.color = player.color;
    this.serializableColor = new game_advanced.SerializableColor(player.color);
    this.controlledPlanets = player.controlledPlanets;
    this.controlledSquads = player.controlledSquads;
  }

  public SerializableColor getSerializableColor() {
    return serializableColor;
  }

  public ArrayList<Planet> getControlledPlanets() {
    return controlledPlanets;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
    this.serializableColor.setColor(color);
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
        && controlledPlanets.equals(player.controlledPlanets)
        && controlledSquads.equals(player.controlledSquads);
  }

  @Override
  public String toString() {
    return "Player{" +
        "color=" + color +
        ", controlledPlanets=" + controlledPlanets +
        ", controlledSquads=" + controlledSquads +
        '}';
  }

  public void addSquad(Squad newSquad) {
    controlledSquads.add(newSquad);
  }

}
