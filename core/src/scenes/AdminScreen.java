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
import model.Jugador;
import model.Partida;

import java.util.List;

public class AdminScreen implements Screen {
    private Stage stage;
    private Sound menuSound;


    public AdminScreen() {
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

        //Estilo label
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

        // Botón PAPELERA
        Texture papeleraTexture = new Texture(Gdx.files.internal("botonPapelera1.png"));
        TextureRegionDrawable papeleraDrawable = new TextureRegionDrawable(new TextureRegion(papeleraTexture));
        ImageButton papeleraButton = new ImageButton(papeleraDrawable);

        volverMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuSound.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });

        // Tabla de partidas
        Table jugadoresTable = new Table();

        // Obtener las partidas del jugador
        ApiClient.obtenerJugadores(new ApiListenerL<List<Jugador>>() {
            @Override
            public void exito(List<Jugador> jugadoresObtenidos) {
                // Limpiar la tabla de partidas
                jugadoresTable.clear();

                // Rellenar la tabla de partidas con las nuevas partidas obtenidas
                for (Jugador jugador : jugadoresObtenidos) {
                    Label idLabel = new Label("ID: " + jugador.getId(), labelStyle);
                    Label usuario = new Label("Usuario: " + jugador.getUsuario(), labelStyle);
                    Label correo = new Label("Correo: " + jugador.getCorreo(), labelStyle);

                    Table partidaTable = new Table();
                    partidaTable.add(idLabel).left().row();
                    partidaTable.add(usuario).left().row();
                    partidaTable.add(correo).left().row();

                    jugadoresTable.add(partidaTable).expandX().fillX().pad(10).row();
                }
            }

            @Override
            public void error(Throwable error) {
                System.out.println("Error al obtener las partidas: " + error.getMessage());
            }
        });

        ScrollPane scrollPane = new ScrollPane(jugadoresTable);
        scrollPane.setFadeScrollBars(false);

        // Configurar la disposición en la tabla principal
        mainTable.add(scrollPane).expand().fill().padBottom(20).row();
        mainTable.add(volverMenuButton).width(200).height(80);

        papeleraButton.setSize(300,300);
        papeleraButton.setPosition(1400,100);

        papeleraButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBorrarDialog();
            }
        });

        // Añadir fondo y tabla principal al stage
        stage.addActor(backgroundImage);
        stage.addActor(mainTable);
        stage.addActor(papeleraButton);
    }

    private void showBorrarDialog() {
        // Crear un estilo para el diálogo manualmente
        Window.WindowStyle windowStyle = new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fondo.png")))));
        Dialog borrarDialog = new Dialog("", windowStyle) {
            @Override
            protected void result(Object object) {
                this.hide();
            }
        };

        // Crear estilos manualmente
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.5f);
        TextureRegionDrawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fondoText.png"))));
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, Color.BLACK, cursorDrawable, null, backgroundDrawable);

        // Crear un estilo para el label del mensaje
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.BLACK);
        borrarDialog.text(new Label("Introduce el ID del jugador que quieres borrar:", labelStyle)).padBottom(20);


        // Crear un campo de texto para el ID del jugador
        TextField idField = new TextField("", textFieldStyle);
        borrarDialog.getContentTable().add(idField).padBottom(20);

        // Crear un botón de confirmar manualmente
        Texture confirmarTexture = new Texture(Gdx.files.internal("botonConfirmar.png"));
        TextureRegionDrawable buttonConfirmar = new TextureRegionDrawable(new TextureRegion(confirmarTexture));
        ImageButton confirmarButton = new ImageButton(buttonConfirmar);
        confirmarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String playerId = idField.getText();
                if (playerId.isEmpty()) {
                    showErrorMessage("Por favor, introduce un ID válido.");
                    return;
                }

                // Llamar al método para borrar jugador por ID
                ApiClient.borrarJugador(Integer.parseInt(playerId), new ApiListener() {
                    @Override
                    public void exito(String respuesta) {
                        Gdx.app.postRunnable(() -> {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new AdminScreen());
                    });
                    }

                    @Override
                    public void error(Throwable error) {
                        Gdx.app.error("AdminScreen", "Error al borrar jugador");
                    }
                });
            }
        });

        // Agregar el botón de confirmar al diálogo
        borrarDialog.button(confirmarButton);

        // Mostrar el diálogo
        borrarDialog.show(stage);
    }

    private void showErrorMessage(String message) {
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
        ImageButton okButton = new ImageButton(buttonOK);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                errorDialog.hide();
            }
        });

        errorDialog.button(okButton, true);
        errorDialog.show(stage);
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
