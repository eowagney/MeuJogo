package com.seunome.meujogo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.seunome.meujogo.MainGame;
import com.seunome.meujogo.entity.Player;
import com.seunome.meujogo.manager.AsteroidManager;
import com.seunome.meujogo.manager.BulletManager;
import com.seunome.meujogo.manager.CollisionManager;
import com.seunome.meujogo.manager.CombatManager;
import com.seunome.meujogo.manager.EffectManager;
import com.seunome.meujogo.manager.SignalManager;
import com.seunome.meujogo.renderer.GameRenderer;

public class GameScreen implements Screen {

    private static final String TAG               = "GameScreen";
    private static final float  MENU_RETURN_DELAY = 3f;
    private static final float  GOAL_SPAWN_TIME   = 20f;
    private static final float  GOAL_SIZE         = 140f;
    private static final float  GOAL_SPEED        = 80f;

    private final MainGame game;

    private Player           player;
    private AsteroidManager  asteroidManager;
    private CollisionManager collisionManager;
    private BulletManager    bulletManager;
    private CombatManager    combatManager;
    private SignalManager    signalManager;
    private EffectManager    effectManager;
    private GameRenderer     renderer;

    // Som da nave — Music para suportar loop contínuo enquanto tecla pressionada
    private Music somNave;

    private float   endTimer;
    private boolean gameOver;
    private boolean venceu;
    private String  motivoFim = "";

    private float   goalX;
    private float   goalY;
    private float   goalTimer;
    private boolean goalActive;

    public GameScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        game.playMusic();

        somNave = Gdx.audio.newMusic(Gdx.files.internal("som_nave.wav"));
        somNave.setLooping(true);
        somNave.setVolume(0.45f);

        asteroidManager  = new AsteroidManager();
        collisionManager = new CollisionManager();
        bulletManager    = new BulletManager();
        combatManager    = new CombatManager();
        signalManager    = new SignalManager();
        effectManager    = new EffectManager();
        renderer         = new GameRenderer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        initGame();
    }

    private void initGame() {
        player   = new Player();
        player.x = Gdx.graphics.getWidth() / 2f;
        player.y = 50f;

        asteroidManager.reset();
        bulletManager.reset();
        signalManager.reset();
        effectManager.reset();

        endTimer   = 0f;
        gameOver   = false;
        venceu     = false;
        motivoFim  = "";
        goalActive = false;
        goalTimer  = 0f;
        goalX      = 0f;
        goalY      = 0f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.06f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.update(delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameOver || venceu) {
            endTimer += delta;
            stopSomNave();

            renderer.drawEndOverlay(
                player, asteroidManager.getAsteroids(), bulletManager.getBullets(),
                goalActive, goalX, goalY, GOAL_SIZE,
                signalManager.getSignal(), effectManager,
                venceu, motivoFim, endTimer
            );

            if (endTimer >= MENU_RETURN_DELAY) {
                game.setScreen(new MenuScreen(game));
            }

            return;
        }

        update(delta);
        renderer.draw(
            player, asteroidManager.getAsteroids(), bulletManager.getBullets(),
            goalActive, goalX, goalY, GOAL_SIZE,
            signalManager.getSignal(), effectManager
        );
    }

    private void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        // Som da nave: toca enquanto alguma tecla de movimento estiver pressionada
        boolean movendo = Gdx.input.isKeyPressed(Input.Keys.W)
                       || Gdx.input.isKeyPressed(Input.Keys.A)
                       || Gdx.input.isKeyPressed(Input.Keys.S)
                       || Gdx.input.isKeyPressed(Input.Keys.D);

        if (movendo) {
            if (!somNave.isPlaying()) somNave.play();
        } else {
            stopSomNave();
        }

        player.update(delta);
        asteroidManager.update(delta);
        bulletManager.update(delta, player);
        signalManager.update(delta);
        effectManager.update(delta);

        combatManager.handleBulletHits(
            bulletManager.getBullets(),
            asteroidManager.getAsteroids(),
            signalManager,
            effectManager
        );

        if (signalManager.isEmpty()) {
            triggerGameOver("Sinal perdido. Comunicacao encerrada.");
            return;
        }

        goalTimer += delta;

        if (!goalActive && goalTimer >= GOAL_SPAWN_TIME) {
            spawnGoal();
        }

        if (goalActive) {
            goalY -= GOAL_SPEED * delta;

            if (goalY < -GOAL_SIZE) {
                goalActive = false;
                triggerGameOver("Marte passou sem ser alcancado.");
                return;
            }

            if (collisionManager.reachedGoal(player, goalX, goalY, GOAL_SIZE)) {
                goalActive = false;
                triggerWin("Voce chegou a Marte. Missao cumprida.");
                return;
            }
        }

        if (collisionManager.handlePlayerAsteroidCollision(player, asteroidManager.getAsteroids())) {
            signalManager.takeDamage(10f);
            Gdx.app.log(TAG, "Colisao com asteroide.");

            if (signalManager.isEmpty()) {
                triggerGameOver("Sinal destruido por colisao com asteroide.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private void stopSomNave() {
        if (somNave != null && somNave.isPlaying()) {
            somNave.stop();
        }
    }

    private void triggerGameOver(String motivo) {
        gameOver  = true;
        motivoFim = motivo;
        endTimer  = 0f;
        Gdx.app.log(TAG, "Game over: " + motivo);
    }

    private void triggerWin(String motivo) {
        venceu    = true;
        motivoFim = motivo;
        endTimer  = 0f;
        Gdx.app.log(TAG, "Vitoria: " + motivo);
    }

    private void spawnGoal() {
        goalX      = (float) (Math.random() * (Gdx.graphics.getWidth() - GOAL_SIZE));
        goalY      = Gdx.graphics.getHeight();
        goalActive = true;
        goalTimer  = 0f;
    }

    @Override
    public void resize(int width, int height) {
        if (renderer != null) {
            renderer.resize(width, height);
        }
    }

    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stopSomNave();
        if (somNave != null) {
            somNave.dispose();
            somNave = null;
        }
        if (renderer != null) {
            renderer.dispose();
            renderer = null;
        }
    }
}