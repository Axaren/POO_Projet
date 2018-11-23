package basic;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject implements Comparable<GameObject> {

  protected Sprite sprite;

  public GameObject(Sprite sprite) {
    this.sprite = sprite;
  }

  public abstract void render(GraphicsContext gc);

  @Override
  public int compareTo(GameObject gameObject) {
    return sprite.compareTo(gameObject.sprite);
  }
}
