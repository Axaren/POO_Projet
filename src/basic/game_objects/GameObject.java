package basic.game_objects;

import basic.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject implements Comparable<GameObject> {

  Sprite sprite;

  public Sprite getSprite() {
    return sprite;
  }

  public GameObject(Sprite sprite) {
    this.sprite = sprite;
  }

  public abstract void update();

  public abstract void render(GraphicsContext gc);

  @Override
  public int compareTo(GameObject gameObject) {
    return sprite.compareTo(gameObject.sprite);
  }
}
