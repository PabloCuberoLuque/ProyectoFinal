package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Login implements Screen {
    private Stage stage;
    private TextField usernameField;
    private TextField passwordField;
    private TextButton loginButton;
    private TextButton registerButton;

    public Login() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Campos de texto para nombre de usuario y contraseña
        usernameField = new TextField("", new TextField.TextFieldStyle());
        passwordField = new TextField("", new TextField.TextFieldStyle());
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        // Botones de inicio de sesión y registro
        loginButton = new TextButton("Login", new TextButton.TextButtonStyle());
        registerButton = new TextButton("Register", new TextButton.TextButtonStyle());

        // Configurar disposición de los elementos en la pantalla
        Table table = new Table();
        table.setFillParent(true);
        table.add(new Label("Username:", new Label.LabelStyle())).padBottom(10);
        table.add(usernameField).width(200).padBottom(10).row();
        table.add(new Label("Password:", new Label.LabelStyle())).padBottom(10);
        table.add(passwordField).width(200).padBottom(10).row();
        table.add(loginButton).width(100).padBottom(10);
        table.add(registerButton).width(100).padBottom(10);

        stage.addActor(table);

        // Listener para el botón de inicio de sesión
        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                // Lógica para enviar solicitud HTTP de inicio de sesión al backend
            }
        });

        // Listener para el botón de registro
        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Lógica para cambiar a la pantalla de registro
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
    public void dispose() {}
}