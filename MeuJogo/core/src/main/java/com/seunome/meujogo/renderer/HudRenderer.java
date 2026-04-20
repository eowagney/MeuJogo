package com.seunome.meujogo.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.seunome.meujogo.entity.FloatingText;

import java.util.List;

public class HudRenderer {

    private final SignalBarRenderer    signalBar;
    private final FloatingTextRenderer floatingText;

    public HudRenderer(OrthographicCamera camera, ShapeRenderer shape) {
        signalBar    = new SignalBarRenderer(camera, shape);
        floatingText = new FloatingTextRenderer();
    }

    public void drawSignalBar(float signal) {
        signalBar.draw(signal);
    }

    public void drawFloatingTexts(List<FloatingText> texts, SpriteBatch batch) {
        floatingText.draw(texts, batch);
    }

    public void dispose() {
        floatingText.dispose();
    }
}