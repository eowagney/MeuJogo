package com.seunome.meujogo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.seunome.meujogo.screen.MenuScreen;

public class MainGame extends Game {

    public SpriteBatch batch;

    private Music trilha;

    @Override
    public void create() {
        batch  = new SpriteBatch();
        trilha = Gdx.audio.newMusic(Gdx.files.internal("trilha_sonora.mp3"));
        trilha.setLooping(true);
        trilha.setVolume(0.75f);

        setScreen(new MenuScreen(this));
    }

    /** Reinicia e toca a trilha do zero. Chamado ao entrar em qualquer tela. */
    public void playMusic() {
        if (trilha != null) {
            trilha.stop();
            trilha.play();
        }
    }

    /** Para a trilha. */
    public void stopMusic() {
        if (trilha != null) {
            trilha.stop();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (trilha != null) {
            trilha.stop();
            trilha.dispose();
            trilha = null;
        }

        if (batch != null) {
            batch.dispose();
            batch = null;
        }
    }
}