package scenes;

import api.ApiClient;
import api.ApiListener;
import api.AppState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import model.Jugador;


public class Login implements Screen {
    private Stage stage;
    private TextField usernameField;
    private TextField passwordField;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Sound menuSound;

    public Login() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Fondo de la pagina
        backgroundTexture = new Texture(Gdx.files.internal("fondo.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        menuSound = Gdx.audio.newSound(Gdx.files.internal("menu.mp3"));
        menuSound.loop();

        //Creacion de texturas para botones con imagen
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.5f);
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fondoText.png"))));
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, Color.BLACK, cursorDrawable, null, backgroundDrawable);

        // Botón de retroceder
        Texture retrocederTexture = new Texture(Gdx.files.internal("retroceder.png"));
        TextureRegionDrawable retrocederDrawable = new TextureRegionDrawable(new TextureRegion(retrocederTexture));
        ImageButton retrocederButton = new ImageButton(retrocederDrawable);
        retrocederButton.setPosition(10, Gdx.graphics.getHeight() - retrocederButton.getHeight() - 10); // Posición arriba a la izquierda
        retrocederButton.setSize(150, 150);
        retrocederButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuSound.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });


        //Creacion de campos de usuario y contraseña
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, font.getColor());

        usernameField = new TextField("", textFieldStyle);
        passwordField = new TextField("", textFieldStyle);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        // Boton de inicio de sesión
        Texture loginTexture = new Texture(Gdx.files.internal("botonConfirmar.png"));
        TextureRegionDrawable loginButtonDrawable = new TextureRegionDrawable(new TextureRegion(loginTexture));
        ImageButton loginButton = new ImageButton(loginButtonDrawable);

        // Suponiendo que tienes las imágenes cargadas en tu proyecto
        Texture usuarioImageTexture = new Texture(Gdx.files.internal("usuario.png"));
        Texture contraseñaImageTexture = new Texture(Gdx.files.internal("contraseña.png"));

        // Suponiendo que ya tienes el estilo de las imágenes configurado
        Image usuarioImage = new Image(usuarioImageTexture);
        Image contraseñaImage = new Image(contraseñaImageTexture);

        loginButton.setPosition(800,50);
        loginButton.setSize(250,250);

        //Creacion de la tabla
        Table table = new Table();
        table.setFillParent(true);
        table.add(usuarioImage).spaceBottom(5);
        table.add(usernameField).width(200).padBottom(10).row();
        table.add(contraseñaImage).spaceBottom(5);
        table.add(passwordField).width(200).padBottom(10).row();
        table.setPosition(0,100);


        //Se añade al stage
        stage.addActor(backgroundImage);
        stage.addActor(table);
        stage.addActor(loginButton);
        stage.addActor(retrocederButton);

        // Listener para el botón de inicio de sesión
        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String usuario = usernameField.getText();
                String contrasena = passwordField.getText();

                if(usuario.equals("PabloAdmin")){
                    ApiClient.login(usuario, contrasena, new ApiListener() {
                    @Override
                    public void exito(String respuesta) {
                        AppState.nombreUsuario = usuario;
                        AppState.jugadorId= Integer.parseInt(respuesta);
                        Gdx.app.postRunnable(() -> {
                            menuSound.stop();
                            Login.this.dispose();
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new AdminScreen());
                        });
                    }

                    @Override
                    public void error(Throwable error) {
                        Gdx.app.error("Login", "Error al iniciar sesión: " + error.getMessage());
                        Gdx.app.postRunnable(() -> showErrorDialog("Error al iniciar sesión: " + error.getMessage()));
                    }
                });
                }else{

                ApiClient.login(usuario, contrasena, new ApiListener() {
                    @Override
                    public void exito(String respuesta) {
                        AppState.nombreUsuario = usuario;
                        AppState.jugadorId= Integer.parseInt(respuesta);
                        Gdx.app.postRunnable(() -> {
                            Login.this.dispose();
                            menuSound.stop();
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuPlayer());
                        });
                    }

                    @Override
                    public void error(Throwable error) {
                        Gdx.app.error("Login", "Error al iniciar sesión: " + error.getMessage());
                        // Mostrar un diálogo con el mensaje de error
                        Gdx.app.postRunnable(() -> showErrorDialog("Error al iniciar sesión: " + error.getMessage()));
                    }
                });
            }}
        });

    }

    private void showErrorDialog(String message) {
        // Crear un estilo para el diálogo manualmente
        Window.WindowStyle windowStyle = new Window.WindowStyle(new BitmapFont(), Color.WHITE, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fondo.png")))));
        Dialog errorDialog = new Dialog("", windowStyle) {
            @Override
            protected void result(Object object) {
                this.hide();
            }
        };

        // Crear un estilo para el label del mensaje
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        errorDialog.text(new Label(message, labelStyle));

        // Crear un botón de OK manualmente
        Texture okTexture = new Texture(Gdx.files.internal("botonConfirmar.png"));
        TextureRegionDrawable buttonOK = new TextureRegionDrawable(new TextureRegion(okTexture));
        ImageButton registerButton = new ImageButton(buttonOK);

        errorDialog.button(registerButton, true);
        errorDialog.show(stage);
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
