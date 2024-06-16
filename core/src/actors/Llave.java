package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Llave extends Actor {
    private Animation<TextureRegion> animacion;
    private float stateTime;
    private Vector2 posicion;
    private Rectangle hitbox;

    public Llave(float x, float y) {
        this.posicion = new Vector2(x, y);
        this.hitbox = new Rectangle(x, y, 16,16);
        hitbox.setPosition(posicion.x, posicion.y);
        // Cargar la hoja de sprites
        Texture spritesheet = new Texture("llave.png");

        // Crear la animación de la llave
        animacion = crearAnimacion(spritesheet, 0, 12, 0.1f);


        stateTime = 0f;
    }

    private Animation<TextureRegion> crearAnimacion(Texture spritesheet, int fila, int frames, float duracionFrame) {
        TextureRegion[] regiones = new TextureRegion[frames];
        int anchoFrame = 32;
        int altoFrame = 32;

        for (int i = 0; i < frames; i++) {
            regiones[i] = new TextureRegion(spritesheet, i * anchoFrame, fila * altoFrame, anchoFrame, altoFrame);
        }

        return new Animation<>(duracionFrame, regiones);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    public void render(SpriteBatch batch) {
        // Obtener el frame actual de la animación
        TextureRegion frame = animacion.getKeyFrame(stateTime, true);

        // Dibujar el frame en la posición del actor
        batch.draw(frame, posicion.x,posicion.y);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

}
