package com.seunome.meujogo.entity;

public class Bullet {

    public float x, y;
    public float width  = 6f;
    public float height = 12f;
    public float speed  = 420f;

    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        y += speed * delta;
    }
}