package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Escalera extends Actor {
    private float stateTime;
    private Vector2 posicion;
    private Rectangle hitbox;

    public Escalera(float x, float y) {
        this.posicion = new Vector2(x, y);
        this.hitbox = new Rectangle(x, y, 16,16);
        hitbox.setPosition(posicion.x, posicion.y);

        stateTime = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }
    public Rectangle getHitbox() {
        return hitbox;
    }
}
