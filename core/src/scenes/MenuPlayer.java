package scenes;

import api.AppState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuPlayer implements Screen {
    private Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Game game;
    private Sound menuSound;

    public MenuPlayer() {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        menuSound = Gdx.audio.newSound(Gdx.files.internal("menu.mp3"));
        menuSound.loop();

        backgroundTexture = new Texture(Gdx.files.internal("fondo.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Crear los botones con imagen
        Texture jugarTexture = new Texture(Gdx.files.internal("jugar.png"));
        Texture estadisticasTexture = new Texture(Gdx.files.internal("estadisticas.png"));
        Texture cerrarSesionTexture = new Texture(Gdx.files.internal("cerrar.png"));
        Texture editarPerfilTexture = new Texture(Gdx.files.internal("botonEditarPerfil.png"));

        ImageButton jugarButton = new ImageButton(new Image(jugarTexture).getDrawable());
        ImageButton estadisticasButton = new ImageButton(new Image(estadisticasTexture).getDrawable());
        ImageButton cerrarSesionButton = new ImageButton(new Image(cerrarSesionTexture).getDrawable());
        ImageButton editarPerfilButton = new ImageButton(new Image(editarPerfilTexture).getDrawable());

        // Configurar disposición de los elementos en la pantalla
        Table table = new Table();
        table.setFillParent(true);
        table.add(jugarButton).width(200).height(80).padBottom(20).row();
        table.add(estadisticasButton).width(200).height(80).padBottom(20).row();
        table.add(editarPerfilButton).width(200).height(80).padBottom(20).row();
        table.add(cerrarSesionButton).width(200).height(80).padBottom(20);

        stage.addActor(backgroundImage);
        stage.addActor(table);

        // Listeners para los botones
        jugarButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuSound.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });

        estadisticasButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuSound.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new EstadisticasScreen(AppState.jugadorId));
            }
        });
        editarPerfilButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuSound.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new EditarPerfilScreen());
            }
        });
        cerrarSesionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuSound.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}