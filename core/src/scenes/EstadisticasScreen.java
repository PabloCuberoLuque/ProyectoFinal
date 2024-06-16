package scenes;

import api.ApiClient;
import api.ApiListener;
import api.ApiListenerL;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import model.Partida;

import java.util.ArrayList;
import java.util.List;

public class EstadisticasScreen implements Screen {
    private Stage stage;
    private int jugadorId;
    private Sound menuSound;

    public EstadisticasScreen(int jugadorId) {
        this.jugadorId = jugadorId;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        menuSound = Gdx.audio.newSound(Gdx.files.internal("menu.mp3"));
        menuSound.loop();

        // Fondo
        Texture backgroundTexture = new Texture(Gdx.files.internal("fondo.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Estilo para los labels
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.5f);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.BLACK);

        // Tabla principal
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // Botón "Volver al Menú"
        Texture volverMenuTexture = new Texture(Gdx.files.internal("botonVolverMenu.png"));
        TextureRegionDrawable volverMenuDrawable = new TextureRegionDrawable(new TextureRegion(volverMenuTexture));
        ImageButton volverMenuButton = new ImageButton(volverMenuDrawable);

        volverMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuSound.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuPlayer());
            }
        });

        // Tabla de partidas
        Table partidasTable = new Table();

        // Obtener las partidas del jugador
        ApiClient.obtenerPartidas(jugadorId, new ApiListenerL<List<Partida>>() {
            @Override
            public void exito(List<Partida> partidasObtenidas) {
                // Limpiar la tabla de partidas
                partidasTable.clear();

                // Rellenar la tabla de partidas con las nuevas partidas obtenidas
                for (Partida partida : partidasObtenidas) {
                    Label idLabel = new Label("ID: " + partida.getId(), labelStyle);
                    Label enemigosLabel = new Label("Enemigos asesinados: " + partida.getEnemigosAsesinados(), labelStyle);

                    Table partidaTable = new Table();
                    partidaTable.add(idLabel).left().row();
                    partidaTable.add(enemigosLabel).left().row();

                    partidasTable.add(partidaTable).expandX().fillX().pad(10).row();
                }
            }

            @Override
            public void error(Throwable error) {
                System.out.println("Error al obtener las partidas: " + error.getMessage());
            }
        });

        ScrollPane scrollPane = new ScrollPane(partidasTable);
        scrollPane.setFadeScrollBars(false);

        // Configurar la disposición en la tabla principal
        mainTable.add(scrollPane).expand().fill().padBottom(20).row();
        mainTable.add(volverMenuButton).width(200).height(80);

        // Añadir fondo y tabla principal al stage
        stage.addActor(backgroundImage);
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        // Limpiar la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar y dibujar el escenario
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
