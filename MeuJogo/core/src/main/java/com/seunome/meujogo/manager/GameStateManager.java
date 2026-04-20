package com.seunome.meujogo.manager;

public class GameStateManager {

    private boolean gameOver;
    private boolean venceu;
    private float   endTimer;

    public void update(float delta) {
        if (gameOver || venceu) {
            endTimer += delta;
        }
    }

    public void setGameOver() {
        gameOver = true;
    }

    public void setWin() {
        venceu = true;
    }

    public void reset() {
        gameOver  = false;
        venceu    = false;
        endTimer  = 0f;
    }

    public boolean isGameOver()  { return gameOver; }
    public boolean isWin()       { return venceu; }
    public boolean isEnded()     { return gameOver || venceu; }
    public float   getEndTimer() { return endTimer; }
}