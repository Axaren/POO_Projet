package game_advanced.game_objects;

import game_advanced.sprites.Sprite;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class is used to define a displayable object in the game_basic.
 */

public abstract class GameObject implements Comparable<GameObject>, Serializable {

  protected Sprite sprite;

  public GameObject(Sprite sprite) {
    this.sprite = sprite;
  }

  public Sprite getSprite() {
    return sprite;
  }

  /**
   * Used to update a GameObject's state
   * @param delta
   */
  public abstract void update(double delta);

  @Override
  public String toString() {
    return "GameObject{" +
        sprite.toString() +
        '}';
  }

  /**
   * Displays the GameObject on the screen
   *
   * @param gc The GraphicsContext of the canvas
   */
  public abstract void render(GraphicsContext gc);

  /**
   * @param gameObject an other GameObject
   * @return An integer representing the relative order when compared to gameObject
   * @see Sprite compareTo for more details
   */
  @Override
  public int compareTo(GameObject gameObject) {
    return sprite.compareTo(gameObject.sprite);
  }
}
