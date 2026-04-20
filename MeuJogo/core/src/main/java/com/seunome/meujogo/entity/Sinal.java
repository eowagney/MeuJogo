package com.seunome.meujogo.entity;

public class Sinal {

    public static final float MAX_SINAL      = 100f;
    public static final float DECAY_POR_SEG  = 4f;   // sinal cai com o tempo
    public static final float BOOST_SORTE    = 20f;   // sorte restaura sinal

    private float nivel = MAX_SINAL;

    public void update(float delta) {
        nivel -= DECAY_POR_SEG * delta;
        nivel  = Math.max(0f, nivel);
    }

    public void restaurar(float quantidade) {
        nivel = Math.min(MAX_SINAL, nivel + quantidade);
    }

    public float getNivel() {
        return nivel;
    }

    public boolean perdido() {
        return nivel <= 0f;
    }
}
