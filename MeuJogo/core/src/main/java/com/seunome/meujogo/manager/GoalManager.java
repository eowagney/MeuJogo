package com.seunome.meujogo.manager;

public class GoalManager {

    @SuppressWarnings("unused")
    private float x, y;
    private boolean active;
    private float timer;

    public void update(float delta) {
        timer += delta;

        if (!active && timer > 20f) {
            spawn();
        }

        if (active) {
            y -= 150 * delta;
        }
    }

    private void spawn() {
        x = (float) Math.random() * 800;
        y = 600;
        active = true;
        timer = 0;
    }
}
