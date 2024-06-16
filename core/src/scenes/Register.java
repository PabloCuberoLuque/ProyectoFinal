package scenes;

import api.ApiClient;
import api.ApiListener;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Register implements Screen {
    private Stage stage;
    private TextField usernameField;
    private TextField passwordField;
    private TextField emailField;
    private Image backgroundImage;
    private Texture backgroundTexture;
    private Sound menuSound;

    public Register() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        menuSound = Gdx.audio.newSound(Gdx.files.internal("menu.mp3"));
        menuSound.loop();

        backgroundTexture = new Texture(Gdx.files.internal("fondo.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Crear estilos manualmente
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.5f);
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fondoText.png"))));
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, Color.BLACK, cursorDrawable, null, backgroundDrawable);


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
                menuSound.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });


        // Crear el botón con imagen
        Texture confirmarTexture = new Texture(Gdx.files.internal("botonConfirmar.png"));
        TextureRegionDrawable buttonC = new TextureRegionDrawable(new TextureRegion(confirmarTexture));

        Texture usuarioImageTexture = new Texture(Gdx.files.internal("usuario.png"));
        Texture contraseñaImageTexture = new Texture(Gdx.files.internal("contraseña.png"));
        Texture correoImageTexture = new Texture(Gdx.files.internal("correo.png"));

        // Suponiendo que ya tienes el estilo de las imágenes configurado
        Image usuarioImage = new Image(usuarioImageTexture);
        Image contraseñaImage = new Image(contraseñaImageTexture);
        Image correoImage = new Image(correoImageTexture);


        ImageButton registerButton = new ImageButton(buttonC);

        registerButton.setPosition(1600, 40);

        Table table = new Table();
        table.setFillParent(true);
        table.add(usuarioImage).spaceBottom(5);
        table.add(usernameField).width(100).padBottom(10).row();
        table.add(contraseñaImage).spaceBottom(5);
        table.add(passwordField).width(100).padBottom(10).row();
        table.add(correoImage).spaceBottom(5);
        table.add(emailField).width(100).padBottom(10).row();
        table.center();

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
                // Validar campos vacíos
                if (usuario.isEmpty() || contrasena.isEmpty() || correo.isEmpty()) {
                    showErrorDialog("Todos los campos son obligatorios");
                    return;
                }

                // Validar que el nombre de usuario comience con una letra mayúscula
                if (!Character.isUpperCase(usuario.charAt(0))) {
                    showErrorDialog("El nombre de usuario debe comenzar con una letra mayúscula");
                    return;
                }

                // Validar que el nombre de usuario no contenga números
                if (usuario.matches(".*\\d.*")) {
                    showErrorDialog("El nombre de usuario no debe contener números");
                    return;
                }
                // Validar el formato del correo electrónico
                if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    showErrorDialog("Formato de correo electrónico inválido");
                    return;
                }

                // Verificar si el usuario ya existe
                ApiClient.verificarUsuarioExistente(usuario, new ApiListener() {
                    @Override
                    public void exito(String usuarioExiste) {
                        if (usuarioExiste.equals("true")) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    Gdx.app.log("Register", "El usuario ya existe");
                                    menuSound.stop();
                                    ((Game) Gdx.app.getApplicationListener()).setScreen(new Register());
                                }
                            });
                        } else {
                            ApiClient.register(usuario, contrasena, correo, new ApiListener() {
                                @Override
                                public void exito(String respuesta) {
                                    Gdx.app.log("Register", "Registro exitoso");
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Cerrar la pantalla actual
                                            Register.this.dispose();
                                            menuSound.stop();
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
                    }

                    @Override
                    public void error(Throwable error) {
                        Gdx.app.error("Verificar Usuario", "Error al verificar usuario: " + error.getMessage());
                    }
                });
            }
        });}

    private void showErrorDialog(String message) {
        // Crear un estilo para el diálogo manualmente
        Window.WindowStyle windowStyle = new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fondo.png")))));
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