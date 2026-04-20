package com.seunome.meujogo.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.seunome.meujogo.entity.Bullet;
import com.seunome.meujogo.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BulletManager {

    private final List<Bullet> bullets = new ArrayList<>();
    private final Sound        somTiro;

    public BulletManager() {
        somTiro = Gdx.audio.newSound(Gdx.files.internal("tiro.wav"));
    }

    public void update(float delta, Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            float x = player.x + player.width  / 2f;
            float y = player.y + player.height;
            bullets.add(new Bullet(x, y));
            somTiro.play(0.6f);
        }

        for (Bullet b : bullets) {
            b.update(delta);
        }

        bullets.removeIf(b -> b.y > Gdx.graphics.getHeight());
    }

    public void reset() {
        bullets.clear();
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void dispose() {
        somTiro.dispose();
    }
}