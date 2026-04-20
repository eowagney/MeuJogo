package com.seunome.meujogo.entity;

public class FloatingText {

    public float x, y;
    public String text;
    public float time = 1f;

    public FloatingText(float x, float y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public void update(float delta) {
        y += 30 * delta;
        time -= delta;
    }

    public boolean isAlive() {
        return time > 0;
    }
}