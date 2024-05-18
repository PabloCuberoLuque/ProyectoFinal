package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import scenes.MainMenu;

public class SombrasAbismo extends Game {
	public SpriteBatch batch;

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenu());
	}

	@Override
	public void render () {
		super.render();
	}

}
