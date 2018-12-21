package src_basic.game_objects;

import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import src_basic.sprites.Sprite;

/**
 * This class is used to define a displayable object in the game.
 */

public abstract class GameObject implements Comparable<GameObject>, Serializable {

  Sprite sprite;

  public GameObject(Sprite sprite) {
    this.sprite = sprite;
  }

  public Sprite getSprite() {
    return sprite;
  }

  /**
   * Used to update a GameObject's state
   */
  public abstract void update();

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
