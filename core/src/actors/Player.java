package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;

public class Player extends Actor {
    private int vida;
    private int nivel;
    private int ataque;
    private int experiencia;
    private int experienciaSiguienteNivel;
    private Vector2 position;

    private Animation<TextureRegion> animacionIdleArriba;
    private Animation<TextureRegion> animacionIdleAbajo;
    private Animation<TextureRegion> animacionIdleDerecha;
    private Animation<TextureRegion> animacionIdleIzquierda;
    private Animation<TextureRegion> animacionCorrerArriba;
    private Animation<TextureRegion> animacionCorrerAbajo;
    private Animation<TextureRegion> animacionCorrerDerecha;
    private Animation<TextureRegion> animacionCorrerIzquierda;
    private Animation<TextureRegion> animacionAtacarArriba;
    private Animation<TextureRegion> animacionAtacarAbajo;
    private Animation<TextureRegion> animacionAtacarDerecha;
    private Animation<TextureRegion> animacionAtacarIzquierda;
    private Animation<TextureRegion> animacionMuerte;
    private float tiempoAtaque = 0f;
    private float tiempoDuracionAtaque = 0.2f;
    private Rectangle hitbox;
    private Rectangle hitboxEspada;
    private float espadaOffsetX;
    private float espadaOffsetY;
    private float espadaWidth;
    private float espadaHeight;
    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;

    private float speedRunning = 100f; // Velocidad de correr del jugador

    private boolean tocando;
    private boolean jugadorMuerto;
    private float tiempoAnimacionMuerte;
    private boolean atacando;

    private float tiempoEstado;
    public EstadoJugador estadoActual;
    private DireccionJugador direccionActual;

    private TiledMapTileLayer collisionLayer;

    public enum EstadoJugador {
        IDLE, CORRIENDO, ATACANDO, MUERTO
    }

    public enum DireccionJugador {
        ARRIBA, ABAJO, IZQUIERDA, DERECHA
    }

    private long lastHitTime; // Tiempo en milisegundos desde la última vez que recibió daño
    private static final long INVULNERABILITY_DURATION = 2000;
    private Sound ataqueSound;
    private Sound lvlUp;

    public Player(float x, float y, TiledMapTileLayer collisionLayer) {
        this.vida = 100;
        this.nivel = 1;
        this.ataque = 3;
        this.experiencia = 0;
        this.experienciaSiguienteNivel = 20;
        this.position = new Vector2(x, y);
        this.tiempoEstado = 0f;
        this.estadoActual = EstadoJugador.IDLE;
        this.direccionActual = DireccionJugador.ABAJO;
        this.hitbox = new Rectangle(x, y, 16, 16);
        espadaOffsetX = 20; // Ajusta la posición X de la espada
        espadaOffsetY = 0; // Ajusta la posición Y de la espada
        espadaWidth = 16; // Ancho de la hitbox de la espada
        espadaHeight = 16; // Alto de la hitbox de la espada
        this.hitboxEspada = new Rectangle(x + espadaOffsetX, y + espadaOffsetY, espadaWidth, espadaHeight);
        this.tocando = false;
        this.jugadorMuerto = false;
        this.tiempoAnimacionMuerte = 0f;
        this.lastHitTime = 0;
        this.atacando=false;
        ataqueSound = Gdx.audio.newSound(Gdx.files.internal("ataque.mp3"));
        lvlUp = Gdx.audio.newSound(Gdx.files.internal("nivel.mp3"));


        // Cargar la hoja de sprites
        Texture spritesheet = new Texture(Gdx.files.internal("Player.png"));

        // Crear animaciones desde la hoja de sprites
        animacionIdleArriba = crearAnimacion(spritesheet, 2, 3, 0.1f);
        animacionIdleAbajo = crearAnimacion(spritesheet, 0, 3, 0.1f);
        animacionIdleDerecha = crearAnimacion(spritesheet, 1, 3, 0.1f);
        animacionIdleIzquierda = voltearHorizontalmente(animacionIdleDerecha);

        animacionCorrerArriba = crearAnimacion(spritesheet, 5, 3, 0.1f);
        animacionCorrerAbajo = crearAnimacion(spritesheet, 3, 3, 0.1f);
        animacionCorrerDerecha = crearAnimacion(spritesheet, 4, 3, 0.1f);
        animacionCorrerIzquierda = voltearHorizontalmente(animacionCorrerDerecha);

        animacionAtacarArriba = crearAnimacion(spritesheet, 8, 3, 0.1f);
        animacionAtacarAbajo = crearAnimacion(spritesheet, 6, 3, 0.1f);
        animacionAtacarDerecha = crearAnimacion(spritesheet, 7, 3, 0.1f);
        animacionAtacarIzquierda = voltearHorizontalmente(animacionAtacarDerecha);

        animacionMuerte = crearAnimacion(spritesheet, 9, 3, 0.1f);

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
        int anchoFrame = 48;
        int altoFrame = 48;

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

    public void recibirDaño(int cantidad) {
        this.vida -= cantidad;
        if (this.vida <= 0) {
            jugadorMuerto = true;
            morir();
        }
    }

    public boolean morir() {
        boolean muerte = false;
        if (jugadorMuerto) {
            tiempoAnimacionMuerte = 0f;
            estadoActual = EstadoJugador.MUERTO;
            animacionCorrerArriba.getKeyFrame(tiempoEstado, true);
            muerte = true;
        }
        return muerte;
    }

    public void ganarExperiencia(int cantidad) {
        this.experiencia += cantidad;
        if (this.experiencia >= experienciaSiguienteNivel) {
            subirNivel();
        }
    }

    public void subirNivel() {
        nivel++;
        experiencia = 0;
        experienciaSiguienteNivel += 10;
        vida += 20;
        ataque += 5;
        lvlUp.play();
    }

    public void reiniciar() {
        vida = 100;
        ataque = 3;
        nivel = 1;
        experiencia = 0;
        setPosition(0, 0);
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void moveUp() {
        if (movingUp && canMoveToTile(position.x, position.y + speedRunning * Gdx.graphics.getDeltaTime())) {
            position.y += speedRunning * Gdx.graphics.getDeltaTime();
            estadoActual = EstadoJugador.CORRIENDO;
            direccionActual = DireccionJugador.ARRIBA;
        }
    }

    public void moveDown() {
        if (movingDown && canMoveToTile(position.x, position.y - speedRunning * Gdx.graphics.getDeltaTime())) {
            position.y -= speedRunning * Gdx.graphics.getDeltaTime();
            estadoActual = EstadoJugador.CORRIENDO;
            direccionActual = DireccionJugador.ABAJO;
        }
    }

    public void moveLeft() {
        if (movingLeft && canMoveToTile(position.x - speedRunning * Gdx.graphics.getDeltaTime(), position.y)) {
            position.x -= speedRunning * Gdx.graphics.getDeltaTime();
            estadoActual = EstadoJugador.CORRIENDO;
            direccionActual = DireccionJugador.IZQUIERDA;
        }
    }

    public void moveRight() {
        if (movingRight && canMoveToTile(position.x + speedRunning * Gdx.graphics.getDeltaTime(), position.y)) {
            position.x += speedRunning * Gdx.graphics.getDeltaTime();
            estadoActual = EstadoJugador.CORRIENDO;
            direccionActual = DireccionJugador.DERECHA;
        }
    }

    private boolean canMoveToTile(float newX, float newY) {
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();

        int tileX = (int) (newX / tileWidth);
        int tileY = (int) (newY / tileHeight);

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX+1, tileY+1);
        return cell != null && cell.getTile() != null;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch) {
        tiempoEstado += Gdx.graphics.getDeltaTime();
        TextureRegion frameActual = getFrameActual();

        // Dibujar el frame actual en la posición del jugador
        batch.draw(frameActual, position.x, position.y);
    }

    public void attack() {
        estadoActual = EstadoJugador.ATACANDO;
        hitboxEspada.set(position.x + espadaOffsetX, position.y + espadaOffsetY, espadaWidth, espadaHeight);
        tiempoAtaque = 0f;
        atacando=true;
        ataqueSound.play();
    }

    public void finalizarAtaque(){
        atacando=false;
    }

    public void update(float delta) {
        if (estadoActual == EstadoJugador.MUERTO) {
            tiempoAnimacionMuerte += delta;
            if (animacionMuerte.isAnimationFinished(tiempoAnimacionMuerte)) {
                reiniciar();
            }
            return;
        }

        if (estadoActual == EstadoJugador.ATACANDO) {
            tiempoAtaque += delta;
            // Verificar si ha terminado la animación de ataque
            switch (direccionActual) {
                case ARRIBA:
                    if (animacionAtacarArriba.isAnimationFinished(tiempoAtaque)) {
                        estadoActual = EstadoJugador.IDLE;
                        tiempoAtaque = 0f;
                    }
                    break;
                case ABAJO:
                    if (animacionAtacarAbajo.isAnimationFinished(tiempoAtaque)) {
                        estadoActual = EstadoJugador.IDLE;
                        tiempoAtaque = 0f;
                    }
                    break;
                case IZQUIERDA:
                    if (animacionAtacarIzquierda.isAnimationFinished(tiempoAtaque)) {
                        estadoActual = EstadoJugador.IDLE;
                        tiempoAtaque = 0f;
                    }
                    break;
                case DERECHA:
                    if (animacionAtacarDerecha.isAnimationFinished(tiempoAtaque)) {
                        estadoActual = EstadoJugador.IDLE;
                        tiempoAtaque = 0f;
                    }
                    break;
            }
        }
        hitbox.setPosition(position.x, position.y);

        // Actualizar la posición de la hitbox de la espada según la dirección del personaje
        float espadaX, espadaY;
        if (estadoActual == EstadoJugador.ATACANDO) {
            switch (direccionActual) {
                case DERECHA:
                    espadaX = position.x + 20;
                    espadaY = position.y + 6;
                    break;
                case IZQUIERDA:
                    espadaX = position.x - 20;
                    espadaY = position.y + 6;
                    break;
                case ARRIBA:
                    espadaX = position.x + 6;
                    espadaY = position.y + 4;
                    break;
                case ABAJO:
                    espadaX = position.x + 6;
                    espadaY = position.y - 4;
                    break;
                default:
                    espadaX = position.x + espadaOffsetX;
                    espadaY = position.y + espadaOffsetY;
        }

            hitboxEspada.setPosition(espadaX, espadaY);
    }}

    private TextureRegion getFrameActual() {
        switch (estadoActual) {
            case CORRIENDO:
                switch (direccionActual) {
                    case ARRIBA:
                        return animacionCorrerArriba.getKeyFrame(tiempoEstado, true);
                    case ABAJO:
                        return animacionCorrerAbajo.getKeyFrame(tiempoEstado, true);
                    case IZQUIERDA:
                        return animacionCorrerIzquierda.getKeyFrame(tiempoEstado, true);
                    case DERECHA:
                        return animacionCorrerDerecha.getKeyFrame(tiempoEstado, true);
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
            default:
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
        }
        return animacionIdleAbajo.getKeyFrame(tiempoEstado, true);
    }

    public void dispose() {
        // Liberar los recursos de todas las texturas utilizadas en las animaciones
        liberarTexturas(animacionCorrerArriba);
        liberarTexturas(animacionCorrerAbajo);
        liberarTexturas(animacionCorrerDerecha);
        liberarTexturas(animacionCorrerIzquierda);
        liberarTexturas(animacionAtacarArriba);
        liberarTexturas(animacionAtacarAbajo);
        liberarTexturas(animacionAtacarDerecha);
        liberarTexturas(animacionAtacarIzquierda);
        liberarTexturas(animacionIdleArriba);
        liberarTexturas(animacionIdleAbajo);
        liberarTexturas(animacionIdleDerecha);
        liberarTexturas(animacionIdleIzquierda);
        liberarTexturas(animacionMuerte);
    }

    private void liberarTexturas(Animation<TextureRegion> animacion) {
        for (TextureRegion frame : animacion.getKeyFrames()) {
            frame.getTexture().dispose();
        }
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Rectangle getHitboxEspada() {
        return hitboxEspada;
    }

    public EstadoJugador getEstadoActual() {
        return estadoActual;
    }

    public boolean isTocando() {
        return tocando;
    }

    public void setTocando(boolean tocando) {
        this.tocando = tocando;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public float getSpeedRunning() {
        return speedRunning;
    }

    public void setSpeedRunning(float speedRunning) {
        this.speedRunning = speedRunning;
    }

    public DireccionJugador getDireccionActual() {
        return direccionActual;
    }
}
