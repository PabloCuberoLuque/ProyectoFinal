package scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.SombrasAbismo;


import java.awt.*;

public class Hud {
    public Stage stage;
    private Viewport viewport;
    private float timeCount;
    private Integer score;

    java.awt.Label arma;
    java.awt.Label armadura;
    java.awt.Label mazmorra;
    java.awt.Label level;
    public Hud(SpriteBatch sb){
        timeCount = 0;
        score=0;

        viewport = new FitViewport(SombrasAbismo.V_WIDTH,SombrasAbismo.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        /*arma = new java.awt.Label("ARMA:", new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle( new BitmapFont(), java.awt.Color.WHITE));
        armadura = new java.awt.Label("ARMADURA:", new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle( new BitmapFont(), java.awt.Color.WHITE));
        mazmorra = new java.awt.Label("MAZMORRA:", new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle( new BitmapFont(), java.awt.Color.WHITE));
        level = new java.awt.Label("NIVEL:", new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle( new BitmapFont(), Color.WHITE));*/

        table.add();
    }



}
