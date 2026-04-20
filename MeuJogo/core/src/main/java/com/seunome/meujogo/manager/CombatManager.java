package com.seunome.meujogo.manager;

import com.badlogic.gdx.Gdx;
import com.seunome.meujogo.entity.Asteroid;
import com.seunome.meujogo.entity.Bullet;

import java.util.Iterator;
import java.util.List;

public class CombatManager {

    private static final String TAG           = "CombatManager";
    private static final float  SIGNAL_REWARD = 5f;

    public void handleBulletHits(List<Bullet> bullets, List<Asteroid> asteroids,
                                 SignalManager signalManager, EffectManager effectManager) {
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();

            Iterator<Asteroid> asteroidIterator = asteroids.iterator();

            while (asteroidIterator.hasNext()) {
                Asteroid a = asteroidIterator.next();

                if (overlaps(b.x, b.y, b.width, b.height,
                             a.x, a.y, a.width, a.height)) {

                    bulletIterator.remove();

                    if (a.destruivel) {
                        asteroidIterator.remove();
                        signalManager.addSignal(SIGNAL_REWARD);
                        effectManager.spawnText(a.x, a.y, "+5");
                        Gdx.app.log(TAG, "Asteroide destruido. +5 sinal.");
                    } else {
                        Gdx.app.log(TAG, "Asteroide atingido mas indestrutivel.");
                    }

                    break;
                }
            }
        }
    }

    private boolean overlaps(float ax, float ay, float aw, float ah,
                              float bx, float by, float bw, float bh) {
        return ax      < bx + bw
            && ax + aw > bx
            && ay      < by + bh
            && ay + ah > by;
    }
}