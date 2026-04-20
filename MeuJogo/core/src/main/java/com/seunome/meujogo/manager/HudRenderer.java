package com.seunome.meujogo.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.seunome.meujogo.entity.FloatingText;

import java.util.List;

public class HudRenderer {

    private static final float BAR_WIDTH   = 200f;
    private static final float BAR_HEIGHT  = 20f;
    private static final float BAR_MARGIN  = 20f;
    private static final float FONT_SCALE  = 1.5f;

    private final OrthographicCamera camera;
    private final ShapeRenderer      shape;
    private final BitmapFont         font;

    public HudRenderer(OrthographicCamera camera, ShapeRenderer shape) {
        this.camera = camera;
        this.shape  = shape;

        font = new BitmapFont();
        font.getData().setScale(FONT_SCALE);
    }

    public void drawSignalBar(float signal) {
        float x            = camera.viewportWidth  - BAR_WIDTH  - BAR_MARGIN;
        float y            = camera.viewportHeight - BAR_HEIGHT - BAR_MARGIN;
        float currentWidth = (signal / 100f) * BAR_WIDTH;

        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(0.3f, 0.3f, 0.3f, 1f);
        shape.rect(x, y, BAR_WIDTH, BAR_HEIGHT);

        if (signal > 50f) {
            shape.setColor(0f, 1f, 0f, 1f);
        } else if (signal > 20f) {
            shape.setColor(1f, 1f, 0f, 1f);
        } else {
            shape.setColor(1f, 0f, 0f, 1f);
        }

        shape.rect(x, y, currentWidth, BAR_HEIGHT);

        shape.end();
    }

    public void drawFloatingTexts(List<FloatingText> texts, SpriteBatch batch) {
        for (FloatingText t : texts) {
            font.draw(batch, t.text, t.x, t.y);
        }
    }

    public void dispose() {
        font.dispose();
    }
}