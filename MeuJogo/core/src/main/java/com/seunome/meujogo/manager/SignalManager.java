package com.seunome.meujogo.manager;

import com.badlogic.gdx.Gdx;

public class SignalManager {

    private static final String TAG             = "SignalManager";
    private static final float  DRAIN_PER_SECOND = 3f;
    private static final float  DAMAGE_INTERVAL  = 1f;

    private float signal;
    private float damageCooldown;

    public void update(float delta) {
        signal -= DRAIN_PER_SECOND * delta;

        if (damageCooldown > 0f) {
            damageCooldown -= delta;
        }

        if (signal < 0f) {
            signal = 0f;
        }
    }

    public void takeDamage(float amount) {
        if (damageCooldown <= 0f) {
            signal        -= amount;
            damageCooldown = DAMAGE_INTERVAL;
            Gdx.app.log(TAG, "Dano recebido. Sinal atual: " + signal);
        }
    }

    public void addSignal(float amount) {
        signal += amount;

        if (signal > 100) {
            signal = 100;
        }
    }

    public float getSignal() {
        return signal;
    }

    public boolean isEmpty() {
        return signal <= 0f;
    }

    public void reset() {
        signal        = 100f;
        damageCooldown = 0f;
    }
}