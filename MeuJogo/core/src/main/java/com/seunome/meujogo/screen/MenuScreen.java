package com.seunome.meujogo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.seunome.meujogo.MainGame;
import com.seunome.meujogo.renderer.MenuBackgroundRenderer;
import com.seunome.meujogo.renderer.MenuPanelRenderer;
import com.seunome.meujogo.starts.MenuStars;

public class MenuScreen implements Screen {

    private final MainGame game;

    private MenuStars              stars;
    private MenuBackgroundRenderer backgroundRenderer;
    private MenuPanelRenderer      panelRenderer;

    private float timer;

    public MenuScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Reinicia a trilha ao entrar no menu
        game.playMusic();

        stars = new MenuStars(70);
        stars.init(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        backgroundRenderer = new MenuBackgroundRenderer(stars);
        panelRenderer      = new MenuPanelRenderer(game);

        timer = 0f;
    }

    @Override
    public void render(float delta) {
        timer += delta;

        Gdx.gl.glClearColor(0.02f, 0.02f, 0.06f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float width  = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float pulse  = (float) Math.sin(timer * 1.5f) * 0.5f + 0.5f;

        stars.update(delta, width, height);
        backgroundRenderer.draw(width, height, pulse);
        panelRenderer.draw(width, height, pulse);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stars != null) {
            stars.init(width, height);
        }
    }

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        if (backgroundRenderer != null) {
            backgroundRenderer.dispose();
            backgroundRenderer = null;
        }

        if (panelRenderer != null) {
            panelRenderer.dispose();
            panelRenderer = null;
        }

        stars = null;
    }
}