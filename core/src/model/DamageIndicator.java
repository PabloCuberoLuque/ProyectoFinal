package model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class DamageIndicator {
    private BitmapFont font;
    private Vector2 position;
    private String text;
    private long creationTime;
    private float duration;
    private float alpha;

    public DamageIndicator(BitmapFont font, float x, float y, int damage) {
        this.font = font;
        this.position = new Vector2(x, y);
        this.text = "-" + damage;
        this.creationTime = TimeUtils.millis();
        this.duration = 1.0f;  // Duración del indicador de daño en segundos
        this.alpha = 1.0f;
    }

    public boolean isExpired() {
        return TimeUtils.timeSinceMillis(creationTime) > duration * 1000;
    }

    public void update() {
        float elapsed = TimeUtils.timeSinceMillis(creationTime) / 1000.0f;
        this.alpha = 1.0f - elapsed / duration;
        this.position.y += 30 * Gdx.graphics.getDeltaTime();  // Hacer que el texto suba
    }

    public void render(SpriteBatch batch) {
        font.setColor(1.0f, 0.0f, 0.0f, alpha);  // Rojo con transparencia
        font.draw(batch, text, position.x, position.y);
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);  // Resetear el color
    }
}
