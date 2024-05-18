package scenes;

import api.ApiClient;
import api.ApiListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

    public Register() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        usernameField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        emailField = new TextField("", skin);

        // Crear el botón con imagen
        Texture confirmarTexture = new Texture(Gdx.files.internal("botonConfirmar.png"));
        TextureRegionDrawable buttonC = new TextureRegionDrawable(new TextureRegion(confirmarTexture));

        ImageButton registerButton = new ImageButton(buttonC);

        registerButton.setPosition(200,100);


        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Usuario:", skin)).padBottom(10);
        table.add(usernameField).width(200).padBottom(10).row();
        table.add(new Label("Contraseña:", skin)).padBottom(10);
        table.add(passwordField).width(200).padBottom(10).row();
        table.add(new Label("Correo:", skin)).padBottom(10);
        table.add(emailField).width(200).padBottom(10).row();
        table.add(registerButton).width(100).padBottom(10);

        stage.addActor(table);
        stage.addActor(registerButton);

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
