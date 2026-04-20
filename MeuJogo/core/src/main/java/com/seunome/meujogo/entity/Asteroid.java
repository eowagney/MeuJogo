package com.seunome.meujogo.entity;

public class Asteroid {

    public float x;
    public float y;
    public float speed = 200;

    public float width = 64;
    public float height = 64;

    public float rotacao = 0;

    public boolean destruivel;

    public float stateTime = 0;

    public Asteroid(float x, float y) {
        this.x = x;
        this.y = y;

        this.width = 48;
        this.height = 48;

        this.destruivel = Math.random() < 0.6;
    }

    public void update(float delta) {
        y -= speed * delta;
        rotacao += 90 * delta;
        stateTime += delta;
    }
}
