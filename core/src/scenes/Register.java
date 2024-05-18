package scenes;

import api.ApiClient;
import api.ApiListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Register implements Screen {

    private Stage stage;
    private TextField usernameField;
    private TextField passwordField;
    private TextField emailField;

    private Image backgroundImage;
    private Texture backgroundTexture;

    public Register() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);



        backgroundTexture = new Texture(Gdx.files.internal("fondo.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Crear estilos manualmente
        BitmapFont font = new BitmapFont();
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fondoText.png"))));
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, font.getColor(), cursorDrawable, null, backgroundDrawable);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, font.getColor());

        usernameField = new TextField("", textFieldStyle);
        passwordField = new TextField("", textFieldStyle);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        emailField = new TextField("", textFieldStyle);


        // Botón de retroceder
        Texture retrocederTexture = new Texture(Gdx.files.internal("retroceder.png"));
        TextureRegionDrawable retrocederDrawable = new TextureRegionDrawable(new TextureRegion(retrocederTexture));
        ImageButton retrocederButton = new ImageButton(retrocederDrawable);
        retrocederButton.setPosition(10, Gdx.graphics.getHeight() - retrocederButton.getHeight() - 10);
        retrocederButton.setSize(150, 150);
        retrocederButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Acción para retroceder a la página anterior
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });


        // Crear el botón con imagen
        Texture confirmarTexture = new Texture(Gdx.files.internal("botonConfirmar.png"));
        TextureRegionDrawable buttonC = new TextureRegionDrawable(new TextureRegion(confirmarTexture));

        ImageButton registerButton = new ImageButton(buttonC);

        registerButton.setPosition(200, 100);

        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Usuario:", labelStyle)).padBottom(10);
        table.add(usernameField).width(100).padBottom(10).row();
        table.add(new Label("Contraseña:", labelStyle)).padBottom(10);
        table.add(passwordField).width(100).padBottom(10).row();
        table.add(new Label("Correo:", labelStyle)).padBottom(10);
        table.add(emailField).width(100).padBottom(10).row();
        table.add(registerButton).width(100).padBottom(10);


        stage.addActor(backgroundImage);
        stage.addActor(table);
        stage.addActor(registerButton);
        stage.addActor(retrocederButton);


        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String usuario = usernameField.getText();
                String contrasena = passwordField.getText();
                String correo = emailField.getText();
                ApiClient.register(usuario, contrasena, correo, new ApiListener() {
                    @Override
                    public void exito(String respuesta) {
                        Gdx.app.log("Register", "Registro exitoso");
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                // Cerrar la pantalla actual
                                Register.this.dispose();
                                // Mostrar la pantalla del menú principal
                                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                            }
                        });
                    }

                    @Override
                    public void error(Throwable error) {
                        Gdx.app.error("Register", "Error al registrar usuario: " + error.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public void show() {}

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
    public void dispose() {}
}