package utils;

import actors.Player;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Array;

public class InputHandler extends InputAdapter {
    private final Player player;
    private final Array<Integer> keys = new Array<>();

    public InputHandler(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!keys.contains(keycode, false)) {
            keys.add(keycode);
        }
        switch (keycode) {
            case Input.Keys.UP:
                player.setMovingUp(true);
                break;
            case Input.Keys.DOWN:
                player.setMovingDown(true);
                break;
            case Input.Keys.LEFT:
                player.setMovingLeft(true);
                break;
            case Input.Keys.RIGHT:
                player.setMovingRight(true);
                break;
            case Input.Keys.SPACE:
                player.attack();
                player.finalizarAtaque();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.removeValue(keycode, false);
        switch (keycode) {
            case Input.Keys.UP:
                player.setMovingUp(false);
                break;
            case Input.Keys.DOWN:
                player.setMovingDown(false);
                break;
            case Input.Keys.LEFT:
                player.setMovingLeft(false);
                break;
            case Input.Keys.RIGHT:
                player.setMovingRight(false);
                break;
        }
        return true;
    }

    public void update(float delta) {
        for (Integer keycode : keys) {
            switch (keycode) {
                case Input.Keys.UP:
                    player.moveUp();
                    break;
                case Input.Keys.DOWN:
                    player.moveDown();
                    break;
                case Input.Keys.LEFT:
                    player.moveLeft();
                    break;
                case Input.Keys.RIGHT:
                    player.moveRight();
                    break;
            }
        }
    }

}
