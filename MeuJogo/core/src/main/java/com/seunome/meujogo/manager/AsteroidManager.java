package com.seunome.meujogo.manager;

import com.badlogic.gdx.Gdx;
import com.seunome.meujogo.entity.Asteroid;

import java.util.ArrayList;
import java.util.List;

public class AsteroidManager {


    private final List<Asteroid> asteroids = new ArrayList<>();
    private float spawnTimer = 0;
    private float spawnRate = 1f;
    private float difficultyTimer = 0;

    public void update(float delta) {

        spawnTimer += delta;
        difficultyTimer += delta;

        if (difficultyTimer > 5f) {
            difficultyTimer = 0;

            spawnRate -= 0.3f;

            if (spawnRate < 0.2f) {
                spawnRate = 0.2f;
            }

            System.out.println("Dificuldade aumentou! SpawnRate: " + spawnRate);
        }

        if (spawnTimer > spawnRate) {
            spawnTimer = 0;

            int quantidade = 1 + (int)(Math.random() * 2);

            for (int i = 0; i < quantidade; i++) {
                spawnAsteroid();
            }
        }

        for (Asteroid a : asteroids) {
            a.update(delta);
        }

        asteroids.removeIf(a -> a.y < -50);
    }

    private void spawnAsteroid() {
        float x = (float) Math.random() * Gdx.graphics.getWidth();
        float y = Gdx.graphics.getHeight();

        asteroids.add(new Asteroid(x, y));
    }

    public void reset() {
        asteroids.clear();

        spawnTimer = 0;
        difficultyTimer = 0;
        spawnRate = 1.5f;
    }

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }
}

