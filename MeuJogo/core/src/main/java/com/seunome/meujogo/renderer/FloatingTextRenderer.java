package com.seunome.meujogo.renderer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.seunome.meujogo.entity.FloatingText;

import java.util.List;

public class FloatingTextRenderer {

    private final BitmapFont font;

    public FloatingTextRenderer() {
        font = new BitmapFont();
    }

    public void draw(List<FloatingText> texts, SpriteBatch batch) {

        for (FloatingText t : texts) {
            font.draw(batch, t.text, t.x, t.y);
        }
    }

    public void dispose() {
        font.dispose();
    }
}