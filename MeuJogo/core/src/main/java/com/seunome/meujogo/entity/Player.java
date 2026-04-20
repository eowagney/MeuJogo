package com.seunome.meujogo.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {

    public float x = 100;
    public float y = 100;
    public float speed = 200;
    public float width = 84;
    public float height = 84;

    public void update(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.A)) x -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) x += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) y += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) y -= speed * delta;

       float larguraTela = com.badlogic.gdx.Gdx.graphics.getWidth();
       float alturaTela = com.badlogic.gdx.Gdx.graphics.getHeight();

        if (x < 0) x = 0;
        if (x > larguraTela - 32) x = larguraTela - 32;

        if (y < 0) y = 0;
        if (y > alturaTela - 32) y = alturaTela - 32;
            }
}