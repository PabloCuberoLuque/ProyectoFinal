package scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class FinalScreen implements Screen {

    private Stage stage;


    public FinalScreen() {
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Fondo
        Texture backgroundTexture = new Texture(Gdx.files.internal("fondo.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Estilo para el label del mensaje
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label messageLabel = new Label("¡Felicidades, valiente aventurero! Has completado la misión con éxito y has demostrado ser un verdadero héroe. ¡Gracias por jugar y disfrutar de esta aventura épica!", labelStyle);

        // Botón "Volver al Menú"
        Texture volverMenuTexture = new Texture(Gdx.files.internal("botonVolverMenu.png"));
        TextureRegionDrawable volverMenuDrawable = new TextureRegionDrawable(new TextureRegion(volverMenuTexture));
        ImageButton volverMenuButton = new ImageButton(volverMenuDrawable);

        volverMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuPlayer());
            }
        });

        // Configurar la disposición
        Table table = new Table();
        table.setFillParent(true);
        table.add(messageLabel).center().padBottom(50).row();
        table.add(volverMenuButton).width(200).height(80);

        // Añadir fondo y tabla al stage
        stage.addActor(backgroundImage);
        stage.addActor(table);
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
