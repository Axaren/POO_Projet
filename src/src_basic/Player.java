package src_basic;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import src_basic.game_objects.Planet;

public class Player implements Serializable {

  private transient Color color;
  private SerializableColor serializableColor;
  private PlayerType type;
  private ArrayList<Planet> controlledPlanets;
  private ArrayList<Squad> controlledSquads;

  public Player(Color color, PlayerType type, Planet startPlanet) {
    this.color = color;
    this.serializableColor = new SerializableColor(color);
    this.type = type;
    this.controlledPlanets = new ArrayList<>(1);
    this.controlledPlanets.add(startPlanet);
    this.controlledSquads = new ArrayList<>();

    startPlanet.setPlayer(this);
  }

  public Player(Player player) {
    this.color = player.color;
    this.serializableColor = new SerializableColor(player.color);
    this.type = player.type;
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

  @Override
  public String toString() {
    return "Player{" +
        "color=" + color +
        ", type=" + type +
        ", controlledPlanets=" + controlledPlanets +
        ", controlledSquads=" + controlledSquads +
        '}';
  }

  public void addSquad(Squad newSquad) {
    controlledSquads.add(newSquad);
  }

  public enum PlayerType {
    HUMAN,
    AI
  }

}
