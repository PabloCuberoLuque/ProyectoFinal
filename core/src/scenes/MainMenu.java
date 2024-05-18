package scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenu implements Screen {
    private Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;


    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("fondo.png"));

        // Crear un objeto Image con la textura de fondo
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        // Cargar la textura de la imagen del botón
        Texture registerTexture = new Texture(Gdx.files.internal("botonRegistro.png"));
        TextureRegionDrawable buttonR = new TextureRegionDrawable(new TextureRegion(registerTexture));

        Texture loginTexture = new Texture(Gdx.files.internal("botonIniciarSesion.png"));
        TextureRegionDrawable buttonL = new TextureRegionDrawable(new TextureRegion(loginTexture));

        Texture exitTexture = new Texture(Gdx.files.internal("botonSalir.png"));
        TextureRegionDrawable buttonE = new TextureRegionDrawable(new TextureRegion(exitTexture));
        // Crear un botón de imagen con la textura cargada
        ImageButton registerButton = new ImageButton(buttonR);
        ImageButton loginButton = new ImageButton(buttonL);
        ImageButton exitButton = new ImageButton(buttonE);

        // Posicionar los botones en la pantalla
        registerButton.setPosition(960, 540);
        loginButton.setPosition(960, 440);
        exitButton.setPosition(960, 340);

        // Agregar listeners a los botones
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Acción al hacer clic en el botón "Register"
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Register());
            }
        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Acción al hacer clic en el botón "Login"
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Login());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Acción al hacer clic en el botón "Salir"
                Gdx.app.exit();
            }
        });

        // Agregar botones al stage
        stage.addActor(backgroundImage);
        stage.addActor(registerButton);
        stage.addActor(loginButton);
        stage.addActor(exitButton);

    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Actualizar y dibujar stage
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
    public void hide() {}

    @Override
    public void dispose() {}
}