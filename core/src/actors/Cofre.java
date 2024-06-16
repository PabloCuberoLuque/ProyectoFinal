package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cofre extends Actor {
    private Animation<TextureRegion> animacionAbierto;
    private Animation<TextureRegion> animacionCerrado;
    private Vector2 posicion;
    private Rectangle hitbox;
    private float stateTime;
    private boolean abierto;

    public Cofre(float x, float y) {
        this.stateTime = 0f;
        this.abierto = false;
        this.posicion = new Vector2(x, y);
        this.hitbox = new Rectangle(x, y, 16,16);

        // Cargar la hoja de sprites
        Texture spritesheet = new Texture(Gdx.files.internal("Chests.png"));

        animacionAbierto= crearAnimacion(spritesheet, 1, 5, 0.1f);
        animacionCerrado = crearAnimacion(spritesheet, 0, 5, 0.1f);

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

    public void intentarAbrir(Player jugador,Boolean tieneLlave) {
        if (tieneLlave && !abierto) {
            abierto = true;
            animacionAbierto.getKeyFrame(0f, true);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (abierto) {
            stateTime += delta;
        }
    }

    public void render(Batch batch) {
        TextureRegion frame;
        if (abierto) {
            frame = animacionAbierto.getKeyFrame(stateTime, false);
        } else {
            frame = animacionCerrado.getKeyFrame(stateTime, false);
        }
        batch.draw(frame, posicion.x, posicion.y);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean isAbierto() {
        return abierto;
    }
}
