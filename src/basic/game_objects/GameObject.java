package basic.game_objects;

import basic.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class is used to define a displayable object in the game.
 */

public abstract class GameObject implements Comparable<GameObject> {

  Sprite sprite;

  public Sprite getSprite() {
    return sprite;
  }

  public GameObject(Sprite sprite) {
    this.sprite = sprite;
  }

  /**
   * Used to update a GameObject's state
   */
  public abstract void update();

  /**
   * Displays the GameObject on the screen
   *
   * @param gc The GraphicsContext of the canvas
   */
  public abstract void render(GraphicsContext gc);

  /**
   * @see Sprite compareTo for more details
   * @param gameObject an other GameObject
   * @return An integer representing the relative order when compared to gameObject
   */
  @Override
  public int compareTo(GameObject gameObject) {
    return sprite.compareTo(gameObject.sprite);
  }
}
