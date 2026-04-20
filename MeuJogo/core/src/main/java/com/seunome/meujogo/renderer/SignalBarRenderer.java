package com.seunome.meujogo.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SignalBarRenderer {

    private static final float BAR_WIDTH  = 200f;
    private static final float BAR_HEIGHT = 20f;
    private static final float MARGIN     = 20f;

    private final OrthographicCamera camera;
    private final ShapeRenderer      shape;

    public SignalBarRenderer(OrthographicCamera camera, ShapeRenderer shape) {
        this.camera = camera;
        this.shape  = shape;
    }

    public void draw(float signal) {
        float x            = camera.viewportWidth  - BAR_WIDTH  - MARGIN;
        float y            = camera.viewportHeight - BAR_HEIGHT - MARGIN;
        float currentWidth = (signal / 100f) * BAR_WIDTH;

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
}