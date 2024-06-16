package scenes;

import actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Hud{
    private Stage stage;
    private Label timerLabel;
    private Label hpLabel;
    private Label xpLabel;
    private Label hpValueLabel;
    private Label xpValueLabel;
    private Label enemigosAsesinadosLabel;
    private Label enemigosAsesinadosValueLabel;
    private Label nivelLabel;
    private Label personajeNivelLabel;
    private Label personajeNivelValueLabel;
    private Image fotopj;
    private Player player;
    private float elapsedTime;
    private Image llaveImage;
    private Image armaImage;
    private Image armaduraImage;
    private int nivel;
    private int enemigos;

    public Hud(SpriteBatch batch, Player player,int enemigosAsesinados) {
        this.player = player;
        this.elapsedTime = 0f;
        stage = new Stage(new ScreenViewport(), batch);
        this.nivel=1;
        this.enemigos=enemigosAsesinados;


        // Crear el label del temporizador
        timerLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timerLabel.setFontScale(2f);

        // Crear los labels de HP y XP
        hpLabel = new Label("HP:", new Label.LabelStyle(new BitmapFont(), Color.RED));
        hpLabel.setFontScale(2f);
        hpValueLabel = new Label(String.valueOf(player.getVida()), new Label.LabelStyle(new BitmapFont(), Color.RED));
        hpValueLabel.setFontScale(2f);

        xpLabel = new Label("XP:", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        xpLabel.setFontScale(2f);
        xpValueLabel = new Label(String.valueOf(player.getExperiencia()), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        xpValueLabel.setFontScale(2f);

        nivelLabel = new Label("Nivel de mazmorra:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        nivelLabel.setFontScale(2f);

        // Crear los labels de Nivel
        personajeNivelLabel = new Label("Nivel del personaje:", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        personajeNivelLabel.setFontScale(2f);
        personajeNivelValueLabel = new Label(String.valueOf(player.getNivel()), new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        personajeNivelValueLabel.setFontScale(2f);

        // Crear los labels de Enemigos Asesinados
        enemigosAsesinadosLabel = new Label("Enemigos Asesinados:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        enemigosAsesinadosLabel.setFontScale(2f);
        enemigosAsesinadosValueLabel = new Label(String.valueOf(enemigosAsesinados), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        enemigosAsesinadosValueLabel.setFontScale(2f);

        // Cargar la textura del icono del pj
        Texture fotoIcono = new Texture("fotopj.png");
        fotopj = new Image(fotoIcono);
        fotopj.setSize(80, 80);  // Tamaño ajustado para la resolución 1280x720p

        // Crear un Table para organizar el HUD
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        // Añadir el icono del personaje a la izquierda
        table.add(fotopj).size(fotopj.getWidth(), fotopj.getHeight()).pad(10).left().top();

        // Añadir los labels de HP y XP debajo del icono del personaje
        Table hpXpTable = new Table();
        hpXpTable.add(hpLabel).left().padRight(10);
        hpXpTable.add(hpValueLabel).left().row();
        hpXpTable.add(xpLabel).left().padRight(10);
        hpXpTable.add(xpValueLabel).left();
        hpXpTable.add(personajeNivelLabel).left().padRight(10);
        hpXpTable.add(personajeNivelValueLabel).left().row();
        table.add(hpXpTable).padTop(30).left().top();


        // Espacio entre los labels y el icono de la mochila
        table.add().expandX();


        // Placeholder for item image
        llaveImage = new Image();
        armaduraImage = new Image();
        armaImage = new Image();

        // Agrega las imágenes a la tabla
        table.add(llaveImage).size(64, 64).padRight(10).center().top(); // Añade la llave a la izquierda
        table.add(armaImage).size(64, 64).padRight(10).center().top(); // Añade el arma en el centro
        table.add(armaduraImage).size(64, 64).center().top().padLeft(10); // Añade la armadura a la derecha del arma



        // Añadir los labels de Enemigos Asesinados y el temporizador
        Table enemigosTimerTable = new Table();
        enemigosTimerTable.add(enemigosAsesinadosLabel).left().padRight(10);
        enemigosTimerTable.add(enemigosAsesinadosValueLabel).left().row();
        enemigosTimerTable.add(timerLabel).pad(10).right().top();

        table.add(enemigosTimerTable).right().top();
        stage.addActor(table);
    }

    public void update(float deltaTime) {
        elapsedTime += deltaTime;

        // Actualizar el temporizador
        int minutos = (int) elapsedTime / 60;
        int segundos = (int) elapsedTime % 60;
        timerLabel.setText(String.format("%02d:%02d", minutos, segundos));

        // Actualizar los valores de HP, XP y Enemigos Asesinados
        hpValueLabel.setText(String.valueOf(player.getVida()));
        xpValueLabel.setText(String.valueOf(player.getExperiencia()));
        enemigosAsesinadosValueLabel.setText(String.valueOf(enemigos));
        personajeNivelValueLabel.setText(String.valueOf(player.getNivel()));
    }

    public void draw() {
        stage.act();
        stage.draw();
    }

    public void render() {
        draw();
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
    }


    public void updateLlaveImage(Texture texture) {
        llaveImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }
    public void clearLLaveImage() {
        llaveImage.setVisible(false);
    }

    public void updateArmaImage(Texture texture) {
        armaImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }
    public void updateArmaduraImage(Texture texture) {
        armaduraImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void aumentarNivel(){
        nivel++;
    }
    public void aumentarEnemigosAsesinados(){
        enemigos++;
    }
}