package scenes;

import actors.*;
import api.ApiClient;
import api.ApiListener;
import api.PartidaInfo;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import model.*;
import utils.InputHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen4 implements Screen {
    private Game juego;
    private OrthographicCamera camera;
    private TiledMap mapa;
    private AssetManager manager;
    private SpriteBatch batch;
    private Stage escenario;
    private Hud hud;
    private Player player;
    private Array<SlimeFinal> slimes;
    private Array<Cofre> cofres;
    private Array<Llave> llaves;

    private Array<Escalera> escalera;
    private InputHandler inputHandler;
    private OrthogonalTiledMapRenderer renderer;
    private Inventario inventario;
    private Partida partida;
    private Jugador jugador;
    private Objeto objeto;
    private int idJugador;
    private boolean llaveObtenida;
    private boolean objetoObtenido;
    private Texture llaveTexture;
    private Texture armaTexture;
    private Texture armaduraTexture;
    private PartidaInfo partidaInfo;
    private TiledMapTileLayer collisionLayer;
    private TiledMapTileLayer objetoCollisionLayer;
    private int enemigosAsesinadosFinal;
    private Sound fondoSound;
    private Sound llaveSound;
    private Sound nivelCompletadoSound;
    // Propiedades del mapa
    private int tileWidth, tileHeight, mapWidthInTiles, mapHeightInTiles, mapWidthInPixels, mapHeightInPixels;
    private List<DamageIndicator> damageIndicators;
    private float tiempoEstado;


    public GameScreen4(Game juego,PartidaInfo partidaInfo,Player player1,int enemigosAsesinados) {
        this.juego = juego;
        this.enemigosAsesinadosFinal=enemigosAsesinados;
        this.jugador = new Jugador();
        this.inventario= new Inventario();
        this.partida = new Partida();
        this.partidaInfo = partidaInfo;
        this.llaveObtenida=false;
        this.objetoObtenido=false;
        manager = new AssetManager();
        escenario = new Stage(new ScreenViewport());
        fondoSound = Gdx.audio.newSound(Gdx.files.internal("Cueva.mp3"));
        llaveSound = Gdx.audio.newSound(Gdx.files.internal("llave.mp3"));
        nivelCompletadoSound = Gdx.audio.newSound(Gdx.files.internal("nivelCompletado.mp3"));
        fondoSound.loop();

        damageIndicators = new ArrayList<>();

        llaveTexture = new Texture(Gdx.files.internal("llave1.png"));
        armaTexture = new Texture(Gdx.files.internal("arma.png"));
        armaduraTexture = new Texture(Gdx.files.internal("armadura.png"));


        manager.setLoader(TiledMap.class,new TmxMapLoader());
        manager.load("mapas/nivel4.tmx",TiledMap.class);
        manager.finishLoading();

        mapa = manager.get("mapas/nivel4.tmx", TiledMap.class);


        // Leer propiedades mapa
        MapProperties properties = mapa.getProperties();
        tileWidth         = properties.get("tilewidth", Integer.class);
        tileHeight        = properties.get("tileheight", Integer.class);
        mapWidthInTiles   = properties.get("width", Integer.class);
        mapHeightInTiles  = properties.get("height", Integer.class);
        mapWidthInPixels  = mapWidthInTiles  * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;


        renderer = new OrthogonalTiledMapRenderer(mapa);

        cofres= new Array<>();
        slimes = new Array<>();
        llaves = new Array<>();
        escalera = new Array<>();
        MapObjects enemigos = mapa.getLayers().get("Enemigos").getObjects();
        MapObjects cofre = mapa.getLayers().get("Cofres").getObjects();
        MapObjects llave = mapa.getLayers().get("Llaves").getObjects();
        MapObjects escalera1 = mapa.getLayers().get("Escalera").getObjects();
        collisionLayer = (TiledMapTileLayer) mapa.getLayers().get("Salas");
        objetoCollisionLayer = (TiledMapTileLayer) mapa.getLayers().get("Objetos");

        //Escalera
        for (MapObject object : escalera1) {
            if (object.getProperties().get("type", String.class).equals("Escalera")) {
                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);


                Escalera e1 = new Escalera(x,y);
                escalera.add(e1); // Añadir el slime a la lista
                escenario.addActor(e1);
            }
        }
        //Enemigos
        for (MapObject object : enemigos) {
            if (object.getProperties().get("type", String.class).equals("Slime")) {
                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);

                SlimeFinal slime = new SlimeFinal(x,y,collisionLayer);
                slimes.add(slime); // Añadir el slime a la lista
                escenario.addActor(slime);
            }
        }

        //Cofres
        for (MapObject object : cofre) {
            if (object.getProperties().get("type", String.class).equals("Cofre")) {
                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);

                Cofre c1 = new Cofre(x,y);
                cofres.add(c1);
                escenario.addActor(c1);
            }
        }

        //LLaves
        for (MapObject object : llave) {
            if (object.getProperties().get("type", String.class).equals("Llave")) {
                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);

                Llave l1 = new Llave(x,y);
                llaves.add(l1);
                escenario.addActor(l1);
            }
        }

        camera = new OrthographicCamera(600, 400);
        camera.setToOrtho(false, 600, 400);
        camera.update();

        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(escenario);

        tiempoEstado = 0f;


        // Inicializar el jugador
        player = new Player(400, 500,collisionLayer);
        player.setVida(player1.getVida());
        player.setAtaque(player1.getAtaque());
        player.setExperiencia(player1.getExperiencia());
        player.setNivel(player1.getNivel());

        // Inicializar el manejador de entradas
        inputHandler = new InputHandler(player);
        Gdx.input.setInputProcessor(inputHandler);

        escenario.addActor(player);

        // Inicializar el HUD
        hud = new Hud(batch,player,enemigosAsesinados);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.5f, .7f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Actualizar la posición del jugador
        player.update(delta);

        for (SlimeFinal slime : slimes) {
            slime.act(delta);
        }

        for (Cofre cofre : cofres) {
            cofre.act(delta);
        }

        for (Llave llave : llaves) {
            llave.act(delta);
        }

        for (Llave llave : llaves) {
            llave.act(delta);
        }



        // Actualizar la posición de la cámara para seguir al jugador
        updateCamera();

        // Renderizar solo la parte visible del mapa
        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Dibujar el jugador en su posición actual

        // Dibujar todos los slimes y verificar colisiones
        for (SlimeFinal slime : slimes) {
            slime.render(batch);
        }

        for (Cofre cofre : cofres) {
            cofre.render(batch);
        }

        for (Llave llave : llaves) {
            llave.render(batch);
        }

        for (Iterator<DamageIndicator> iterator = damageIndicators.iterator(); iterator.hasNext();) {
            DamageIndicator indicator = iterator.next();
            indicator.update();
            indicator.render(batch);

            if (indicator.isExpired()) {
                iterator.remove();
            }
        }

        player.render(batch);

        batch.end();



        escenario.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        escenario.draw();


        hud.update(delta);
        hud.render();

        checkCollisions();

        inputHandler.update(delta);

        if (player.morir()) {
            player.morir();
            player.reiniciar();
            fondoSound.stop();
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MuerteScreen());
        }
    }

    private void updateCamera() {
        float playerX = player.getPosition().x;
        float playerY = player.getPosition().y;

        camera.position.x = playerX;
        camera.position.y = playerY;

        // Limitar la cámara para que no salga del borde del mapa
        camera.position.x = Math.max(camera.viewportWidth / 2, camera.position.x);
        camera.position.x = Math.min(mapWidthInPixels - camera.viewportWidth / 2, camera.position.x);
        camera.position.y = Math.max(camera.viewportHeight / 2, camera.position.y);
        camera.position.y = Math.min(mapHeightInPixels - camera.viewportHeight / 2, camera.position.y);

        camera.update();
    }
    private void checkCollisions() {
        // Colisiones
        for (SlimeFinal slime : slimes) {
            // Detectar colisión
            if (player.getHitbox().overlaps(slime.getHitbox())) {
                if (!player.isTocando() && !player.isInvulnerable()) {
                    player.recibirDaño(slime.getAtaque()); // Ejemplo de daño
                    player.setTocando(true);
                    player.setLastHitTime(TimeUtils.millis()); // Registrar el tiempo de la última colisión
                }
            } else {
                // Si las hitboxes no se están tocando, reseteamos la bandera
                player.setTocando(false);
            }


            // Verificar colisión con la hitbox de la espada si el jugador está atacando
                if (slime.getHitbox().overlaps(player.getHitboxEspada()) && !slime.isDaño() && !slime.isInvulnerable()&& !slime.isMuerto()) {
                    int damage = player.getAtaque();
                    // Realizar alguna acción cuando la espada del jugador golpea al slime
                    slime.recibirDaño(player.getAtaque(),player);
                    slime.setDaño(true);
                    slime.setLastHitTime(TimeUtils.millis());

                    damageIndicators.add(new DamageIndicator(new BitmapFont(), slime.getPosicion().x, slime.getPosicion().y, damage));
                    if (slime.morir(player)) {
                        enemigosAsesinadosFinal++;
                        hud.aumentarEnemigosAsesinados();
                        fondoSound.stop();
                        ApiClient.actualizarEnemigos(partidaInfo.getPartidaId(), enemigosAsesinadosFinal, new ApiListener() {
                            @Override
                            public void exito(String respuesta) {
                                System.out.println("Enemigos actualizados con exito");
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        nivelCompletadoSound.play();
                                        ((Game) Gdx.app.getApplicationListener()).setScreen(new FinalScreen());
                                    }
                                });
                            }

                            @Override
                            public void error(Throwable error) {
                                System.out.println("Error al actualizar los enemigos asesinados");
                            }
                        });
                    }
                }else{
                    slime.setDaño(false);
                }
            }

        for(Llave llave:llaves){
            if(player.getHitbox().overlaps(llave.getHitbox()) && !llaveObtenida){
                ApiClient.actualizarInventario(partidaInfo.getInventarioId(), 1, partidaInfo.getPartidaId(), new ApiListener() {
                    @Override
                    public void exito(String respuesta) {
                        System.out.println("Llave añadida al inventario");
                        hud.updateLlaveImage(llaveTexture);
                        llaveSound.play();
                        llaveObtenida=true;
                    }
                    @Override
                    public void error(Throwable error) {
                        System.out.println("Error al añadir la llave al inventario");
                        llaveObtenida=true;
                    }
                });
            }
        }

        for(Cofre cofre:cofres){
            if (player.getHitbox().overlaps(cofre.getHitbox())&& !objetoObtenido) {
                objetoObtenido=true;
                cofre.intentarAbrir(player,llaveObtenida);
                llaveObtenida=false;
                hud.clearLLaveImage();
                if(cofre.isAbierto()){
                    ApiClient.actualizarInventario(partidaInfo.getInventarioId(), 2, partidaInfo.getPartidaId(), new ApiListener() {
                        @Override
                        public void exito(String respuesta) {
                            System.out.println("Objeto añadido al inventario");
                            hud.updateArmaduraImage(armaduraTexture);
                            player.setVida(player.getVida()+20);
                        }
                        @Override
                        public void error(Throwable error) {
                            System.out.println("Error al añadir el objeto al inventario");
                        }
                    });
                }

            }
        }
    }


    @Override
    public void resize(int width, int height) {
        escenario.getViewport().update(width, height, true);
        hud.getStage().getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose(); // Asegurarse de liberar los recursos del jugador
        hud.dispose(); // Liberar los recursos del HUD
        manager.dispose();
    }

}