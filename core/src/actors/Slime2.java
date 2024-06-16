package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class Slime2 extends Actor {
    private int vida;
    private int xp;
    private int ataque;
    private Vector2 posicion;
    private float tiempoEstado;
    private float duracionAtaque = 0.2f;
    private Rectangle hitbox;

    private Animation<TextureRegion> animacionIdleArriba;
    private Animation<TextureRegion> animacionIdleAbajo;
    private Animation<TextureRegion> animacionIdleDerecha;
    private Animation<TextureRegion> animacionIdleIzquierda;
    private Animation<TextureRegion> animacionCaminarArriba;
    private Animation<TextureRegion> animacionCaminarAbajo;
    private Animation<TextureRegion> animacionCaminarDerecha;
    private Animation<TextureRegion> animacionCaminarIzquierda;
    private Animation<TextureRegion> animacionAtacarArriba;
    private Animation<TextureRegion> animacionAtacarAbajo;
    private Animation<TextureRegion> animacionAtacarDerecha;
    private Animation<TextureRegion> animacionAtacarIzquierda;
    private Animation<TextureRegion> animacionMuerte;

    private boolean moviendoArriba;
    private boolean moviendoAbajo;
    private boolean moviendoIzquierda;
    private boolean moviendoDerecha;
    private boolean experienciaObtenida;

    private final float velocidad = 20f;
    private Random random;
    private float tiempoMovimiento;
    private float duracionMovimiento;
    private TiledMapTileLayer collisionLayer;

    public enum EstadoSlime {
        IDLE, CAMINANDO, ATACANDO, MUERTO
    }

    public enum DireccionSlime {
        ARRIBA, ABAJO, IZQUIERDA, DERECHA
    }

    private EstadoSlime estadoActual;
    private DireccionSlime direccionActual;
    private boolean muerto;
    private boolean daño;
    private long lastHitTime;
    private static final long INVULNERABILITY_DURATION = 2000;

    public Slime2(float x, float y, TiledMapTileLayer collisionLayer) {
        this.vida = 20;
        this.xp = 8;
        this.ataque = 6;
        this.estadoActual = EstadoSlime.IDLE;
        this.direccionActual = DireccionSlime.ABAJO;
        this.hitbox = new Rectangle(x, y, 16, 16);
        this.muerto = false;
        this.posicion = new Vector2(x, y);
        this.daño = false;

        // Inicializar el generador de números aleatorios
        this.random = new Random();

        // Inicializar el tiempo y duración del movimiento aleatorio
        this.tiempoMovimiento = 0f;
        this.duracionMovimiento = random.nextFloat() * 3 + 1; // Duración aleatoria entre 1 y 4 segundos

        // Cargar la hoja de sprites
        Texture spritesheet = new Texture(Gdx.files.internal("Slime2.png"));

        // Crear animaciones desde la hoja de sprites
        animacionIdleArriba = crearAnimacion(spritesheet, 2, 3, 0.1f);
        animacionIdleAbajo = crearAnimacion(spritesheet, 0, 3, 0.1f);
        animacionIdleDerecha = crearAnimacion(spritesheet, 1, 3, 0.1f);
        animacionIdleIzquierda = voltearHorizontalmente(animacionIdleDerecha);

        animacionCaminarArriba = crearAnimacion(spritesheet, 5, 3, 0.1f);
        animacionCaminarAbajo = crearAnimacion(spritesheet, 3, 3, 0.1f);
        animacionCaminarDerecha = crearAnimacion(spritesheet, 4, 3, 0.1f);
        animacionCaminarIzquierda = voltearHorizontalmente(animacionCaminarDerecha);

        animacionAtacarArriba = crearAnimacion(spritesheet, 8, 3, 0.1f);
        animacionAtacarAbajo = crearAnimacion(spritesheet, 6, 3, 0.1f);
        animacionAtacarDerecha = crearAnimacion(spritesheet, 7, 3, 0.1f);
        animacionAtacarIzquierda = voltearHorizontalmente(animacionAtacarDerecha);

        animacionMuerte = crearAnimacion(spritesheet, 12, 3, 0.1f);

        this.collisionLayer = collisionLayer;

    }

    public boolean isInvulnerable() {
        return TimeUtils.timeSinceMillis(lastHitTime) < INVULNERABILITY_DURATION;
    }

    public long getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(long lastHitTime) {
        this.lastHitTime = lastHitTime;
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

    private Animation<TextureRegion> voltearHorizontalmente(Animation<TextureRegion> animacion) {
        TextureRegion[] keyFrames = animacion.getKeyFrames();
        TextureRegion[] voltearFrames = new TextureRegion[keyFrames.length];

        for (int i = 0; i < keyFrames.length; i++) {
            voltearFrames[i] = new TextureRegion(keyFrames[i]);
            voltearFrames[i].flip(true, false);
        }

        return new Animation<>(animacion.getFrameDuration(), voltearFrames);
    }

    private boolean canMoveToTile(float newX, float newY) {
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();

        int tileX = (int) (newX / tileWidth);
        int tileY = (int) (newY / tileHeight);

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX+1, tileY+1);
        return cell != null && cell.getTile() != null;
    }

    public void act(float delta) {
        tiempoEstado += delta;

        switch (estadoActual) {
            case CAMINANDO:
                if (moviendoArriba) {
                    moverArriba(delta);
                } else if (moviendoAbajo) {
                    moverAbajo(delta);
                } else if (moviendoIzquierda) {
                    moverIzquierda(delta);
                } else if (moviendoDerecha) {
                    moverDerecha(delta);
                }

                // Actualizar el tiempo de movimiento
                tiempoMovimiento += delta;
                if (tiempoMovimiento >= duracionMovimiento) {
                    estadoActual = EstadoSlime.IDLE;
                    tiempoMovimiento = 0f;
                    duracionMovimiento = random.nextFloat() * 3 + 1;
                }
                break;
            case ATACANDO:
                if (tiempoEstado >= duracionAtaque) {
                    estadoActual = EstadoSlime.IDLE;
                    tiempoEstado = 0f;
                }
                break;
            case IDLE:
                // Cambiar a estado de caminar después de cierto tiempo
                tiempoMovimiento += delta;
                if (tiempoMovimiento >= duracionMovimiento) {
                    estadoActual = EstadoSlime.CAMINANDO;
                    tiempoMovimiento = 0f;
                    duracionMovimiento = random.nextFloat() * 3 + 1;
                    moverAleatoriamente();
                }
                break;
            default:
                break;
        }
        hitbox.setPosition(posicion.x, posicion.y);
    }

    private void cambiarDireccion() {
        DireccionSlime nuevaDireccion;
        do {
            nuevaDireccion = DireccionSlime.values()[random.nextInt(DireccionSlime.values().length)];
        } while (nuevaDireccion == direccionActual);

        direccionActual = nuevaDireccion;
    }

    private void moverArriba(float delta) {
        float newY = posicion.y + velocidad * delta;
        if (canMoveToTile(posicion.x, newY)) {
            posicion.y = newY;
            direccionActual = DireccionSlime.ARRIBA;
        } else {
            cambiarDireccion();
        }
    }

    private void moverAbajo(float delta) {
        float newY = posicion.y - velocidad * delta;
        if (canMoveToTile(posicion.x, newY)) {
            posicion.y = newY;
            direccionActual = DireccionSlime.ABAJO;
        } else {
            cambiarDireccion();
        }
    }

    private void moverIzquierda(float delta) {
        float newX = posicion.x - velocidad * delta;
        if (canMoveToTile(newX, posicion.y)) {
            posicion.x = newX;
            direccionActual = DireccionSlime.IZQUIERDA;
        } else {
            cambiarDireccion();
        }
    }

    private void moverDerecha(float delta) {
        float newX = posicion.x + velocidad * delta;
        if (canMoveToTile(newX, posicion.y)) {
            posicion.x = newX;
            direccionActual = DireccionSlime.DERECHA;
        } else {
            cambiarDireccion();
        }
    }

    private void moverAleatoriamente() {
        int direccion = random.nextInt(4);
        moviendoArriba = false;
        moviendoAbajo = false;
        moviendoIzquierda = false;
        moviendoDerecha = false;

        switch (direccion) {
            case 0:
                moviendoArriba = true;
                break;
            case 1:
                moviendoAbajo = true;
                break;
            case 2:
                moviendoIzquierda = true;
                break;
            case 3:
                moviendoDerecha = true;
                break;
        }
    }

    public void render(SpriteBatch batch) {
        tiempoEstado += Gdx.graphics.getDeltaTime();
        TextureRegion frameActual = getFrameActual();

        // Dibujar el frame actual en la posición del slime
        batch.draw(frameActual, posicion.x, posicion.y);
    }

    private TextureRegion getFrameActual() {
        switch (estadoActual) {
            case CAMINANDO:
                switch (direccionActual) {
                    case ARRIBA:
                        return animacionCaminarArriba.getKeyFrame(tiempoEstado, true);
                    case ABAJO:
                        return animacionCaminarAbajo.getKeyFrame(tiempoEstado, true);
                    case IZQUIERDA:
                        return animacionCaminarIzquierda.getKeyFrame(tiempoEstado, true);
                    case DERECHA:
                        return animacionCaminarDerecha.getKeyFrame(tiempoEstado, true);
                }
            case ATACANDO:
                switch (direccionActual) {
                    case ARRIBA:
                        return animacionAtacarArriba.getKeyFrame(tiempoEstado, true);
                    case ABAJO:
                        return animacionAtacarAbajo.getKeyFrame(tiempoEstado, true);
                    case IZQUIERDA:
                        return animacionAtacarIzquierda.getKeyFrame(tiempoEstado, true);
                    case DERECHA:
                        return animacionAtacarDerecha.getKeyFrame(tiempoEstado, true);
                }
            case IDLE:
                switch (direccionActual) {
                    case ARRIBA:
                        return animacionIdleArriba.getKeyFrame(tiempoEstado, true);
                    case ABAJO:
                        return animacionIdleAbajo.getKeyFrame(tiempoEstado, true);
                    case IZQUIERDA:
                        return animacionIdleIzquierda.getKeyFrame(tiempoEstado, true);
                    case DERECHA:
                        return animacionIdleDerecha.getKeyFrame(tiempoEstado, true);
                }
            case MUERTO:
                return animacionMuerte.getKeyFrame(tiempoEstado, false);
            default:
                return animacionIdleAbajo.getKeyFrame(tiempoEstado, true);
        }
    }

    public void recibirDaño(int cantidad, Player jugador) {
        if (!muerto) {
            this.vida -= cantidad;
            if (this.vida <= 0) {
                this.muerto = true;
                this.morir(jugador);
            }
        }
    }

    public boolean morir(Player jugador) {
        boolean muerte = false;
        if (muerto) {
            tiempoEstado = 0f;
            estadoActual = EstadoSlime.MUERTO;
            animacionMuerte.getKeyFrame(tiempoEstado, true);
            if (!experienciaObtenida) {
                jugador.ganarExperiencia(this.xp);
                experienciaObtenida = true; // Marcar que ya se obtuvo la experiencia
            }
            remove(); // Eliminar el actor

            muerte = true;
        }
        return muerte;
    }

    public void atacar() {
        estadoActual = EstadoSlime.ATACANDO;
        tiempoEstado = 0f; // Reiniciar el tiempo de animación
    }

    public void detenerAtaque() {
        estadoActual = EstadoSlime.IDLE;
        tiempoEstado = 0f; // Reiniciar el tiempo de animación
    }

    public int getVida() {
        return vida;
    }

    public int getXp() {
        return xp;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public void setPosicion(Vector2 posicion) {
        this.posicion = posicion;
    }

    public void dispose() {
        disposeAnimation(animacionIdleArriba);
        disposeAnimation(animacionIdleAbajo);
        disposeAnimation(animacionIdleDerecha);
        disposeAnimation(animacionIdleIzquierda);
        disposeAnimation(animacionCaminarArriba);
        disposeAnimation(animacionCaminarAbajo);
        disposeAnimation(animacionCaminarDerecha);
        disposeAnimation(animacionCaminarIzquierda);
        disposeAnimation(animacionAtacarArriba);
        disposeAnimation(animacionAtacarAbajo);
        disposeAnimation(animacionAtacarDerecha);
        disposeAnimation(animacionAtacarIzquierda);
        disposeAnimation(animacionMuerte);
    }

    private void disposeAnimation(Animation<TextureRegion> animation) {
        if (animation != null) {
            for (TextureRegion frame : animation.getKeyFrames()) {
                frame.getTexture().dispose();
            }
        }
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getAtaque() {
        return ataque;
    }

    public boolean isDaño() {
        return daño;
    }

    public void setDaño(boolean daño) {
        this.daño = daño;
    }

    public boolean isMuerto() {
        return muerto;
    }

    public void setMuerto(boolean muerto) {
        this.muerto = muerto;
    }
}
