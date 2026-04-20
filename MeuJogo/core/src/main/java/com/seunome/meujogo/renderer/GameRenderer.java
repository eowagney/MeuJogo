package com.seunome.meujogo.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.seunome.meujogo.entity.Asteroid;
import com.seunome.meujogo.entity.Bullet;
import com.seunome.meujogo.entity.Player;
import com.seunome.meujogo.manager.EffectManager;
import com.seunome.meujogo.starts.MenuStars;

import java.util.List;

public class GameRenderer {

    private static final int   PLAYER_FRAME_SIZE  = 512;
    private static final float PLAYER_FRAME_SPEED = 0.1f;
    private static final int   GOAL_FRAME_WIDTH   = 133;
    private static final int   GOAL_FRAME_HEIGHT  = 76;
    private static final float GOAL_FRAME_SPEED   = 0.15f;

    // Overlay constants
    private static final float PANEL_W         = 420f;
    private static final float PANEL_H         = 200f;
    private static final float PROGRESS_W      = 340f;
    private static final float PROGRESS_H      = 10f;
    private static final float MENU_DELAY      = 3f;

    private final OrthographicCamera       camera;
    private final SpriteBatch              batch;
    private final ShapeRenderer            shape;
    private final MenuStars                stars;
    private final HudRenderer              hudRenderer;
    private final Texture                  playerSheet;
    private final Texture                  asteroidTexture;
    private final Texture                  goalSheet;
    private final Animation<TextureRegion> playerAnimation;
    private final Animation<TextureRegion> goalAnimation;

    // Overlay fonts
    private final BitmapFont   fontTitle;
    private final BitmapFont   fontSub;
    private final GlyphLayout  layout;

    private float stateTime     = 0f;
    private float goalStateTime = 0f;

    public GameRenderer(int screenWidth, int screenHeight) {
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.position.set(screenWidth / 2f, screenHeight / 2f, 0f);
        camera.update();

        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        hudRenderer = new HudRenderer(camera, shape);

        stars = new MenuStars(70);
        stars.init(screenWidth, screenHeight);

        playerSheet     = new Texture("nave.png");
        asteroidTexture = new Texture("asteroide.png");
        goalSheet       = new Texture("marte.png");

        playerAnimation = buildAnimation(playerSheet, PLAYER_FRAME_SIZE, PLAYER_FRAME_SIZE, PLAYER_FRAME_SPEED);
        goalAnimation   = buildAnimation(goalSheet,   GOAL_FRAME_WIDTH,  GOAL_FRAME_HEIGHT, GOAL_FRAME_SPEED);

        fontTitle = new BitmapFont();
        fontTitle.getData().setScale(2.8f);

        fontSub = new BitmapFont();
        fontSub.getData().setScale(1.1f);

        layout = new GlyphLayout();
    }

    private Animation<TextureRegion> buildAnimation(Texture sheet, int frameWidth, int frameHeight, float speed) {
        TextureRegion[][] tmp    = TextureRegion.split(sheet, frameWidth, frameHeight);
        int               rows   = tmp.length;
        int               cols   = tmp[0].length;
        TextureRegion[]   frames = new TextureRegion[rows * cols];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return new Animation<>(speed, frames);
    }

    public void update(float delta, float width, float height) {
        stateTime     += delta;
        goalStateTime += delta;
        stars.update(delta, width, height);
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.position.set(width / 2f, height / 2f, 0f);
        camera.update();
        stars.init(width, height);
    }

    // -------------------------------------------------------------------------
    // Draw normal (jogo rodando)
    // -------------------------------------------------------------------------
    public void draw(Player player, List<Asteroid> asteroids, List<Bullet> bullets,
                     boolean goalActive, float goalX, float goalY, float goalSize,
                     float signal, EffectManager effectManager) {
        camera.update();

        shape.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        drawStars();
        hudRenderer.drawSignalBar(signal);

        batch.begin();

        if (goalActive) {
            drawGoal(goalX, goalY, goalSize);
        }

        drawAsteroids(asteroids);
        drawPlayer(player);
        hudRenderer.drawFloatingTexts(effectManager.getTexts(), batch);

        batch.end();

        drawBullets(bullets);
    }

    // -------------------------------------------------------------------------
    // Draw com overlay de fim de jogo
    // -------------------------------------------------------------------------
    public void drawEndOverlay(Player player, List<Asteroid> asteroids, List<Bullet> bullets,
                               boolean goalActive, float goalX, float goalY, float goalSize,
                               float signal, EffectManager effectManager,
                               boolean venceu, String motivo, float endTimer) {

        // Renderiza o jogo congelado no fundo
        draw(player, asteroids, bullets, goalActive, goalX, goalY, goalSize, signal, effectManager);

        float cx    = camera.viewportWidth  / 2f;
        float cy    = camera.viewportHeight / 2f;
        float pulse = (float) Math.sin(stateTime * 2.5f) * 0.5f + 0.5f;

        shape.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        drawOverlayPanel(cx, cy, venceu, endTimer, pulse);
        drawOverlayText(cx, cy, venceu, motivo, pulse);
    }

    // -------------------------------------------------------------------------
    // Painel do overlay
    // -------------------------------------------------------------------------
    private void drawOverlayPanel(float cx, float cy, boolean venceu, float endTimer, float pulse) {
        float px = cx - PANEL_W / 2f;
        float py = cy - PANEL_H / 2f;

        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Fundo escuro semi-transparente cobrindo a tela
        shape.setColor(0f, 0f, 0f, 0.55f);
        shape.rect(0, 0, camera.viewportWidth, camera.viewportHeight);

        // Painel central
        shape.setColor(0.04f, 0.06f, 0.14f, 0.95f);
        shape.rect(px, py, PANEL_W, PANEL_H);

        // Bordas brilhantes (cor varia: verde=vitória, vermelho=derrota)
        float glow = 0.5f + pulse * 0.4f;
        if (venceu) {
            shape.setColor(0.1f, glow, 0.3f, 0.95f);
        } else {
            shape.setColor(glow, 0.1f, 0.1f, 0.95f);
        }
        shape.rect(px,              py,              PANEL_W, 2f);
        shape.rect(px,              py + PANEL_H - 2f, PANEL_W, 2f);
        shape.rect(px,              py,              2f,     PANEL_H);
        shape.rect(px + PANEL_W - 2f, py,            2f,     PANEL_H);

        // Separador
        shape.setColor(0.15f, 0.5f, 1f, 0.35f);
        shape.rect(px + 30f, py + PANEL_H - 70f, PANEL_W - 60f, 1f);

        // Barra de progresso (tempo restante até voltar ao menu)
        float progX    = cx - PROGRESS_W / 2f;
        float progY    = py + 22f;
        float progress = Math.min(endTimer / MENU_DELAY, 1f);

        shape.setColor(0.2f, 0.2f, 0.35f, 1f);
        shape.rect(progX, progY, PROGRESS_W, PROGRESS_H);

        if (venceu) {
            shape.setColor(0.15f, 0.85f, 0.4f, 1f);
        } else {
            shape.setColor(0.9f, 0.2f, 0.2f, 1f);
        }
        shape.rect(progX, progY, PROGRESS_W * progress, PROGRESS_H);

        shape.end();
    }

    // -------------------------------------------------------------------------
    // Textos do overlay
    // -------------------------------------------------------------------------
    private void drawOverlayText(float cx, float cy, boolean venceu, String motivo, float pulse) {
        float py = cy - PANEL_H / 2f;

        batch.begin();

        // Título principal
        float titleAlpha = 0.9f + pulse * 0.1f;
        if (venceu) {
            fontTitle.setColor(0.25f, 1f, 0.5f, titleAlpha);
            drawCentered(fontTitle, "MISSAO CONCLUIDA", cx, cy + PANEL_H / 2f - 18f);
        } else {
            fontTitle.setColor(1f, 0.25f, 0.25f, titleAlpha);
            drawCentered(fontTitle, "MISSAO FALHOU", cx, cy + PANEL_H / 2f - 18f);
        }

        // Subtítulo / motivo
        fontSub.setColor(0.65f, 0.78f, 0.98f, 0.9f);
        drawCentered(fontSub, motivo, cx, cy + 18f);

        // Label da barra
        fontSub.setColor(0.45f, 0.55f, 0.75f, 0.75f);
        drawCentered(fontSub, "Voltando ao menu...", cx, py + 46f);

        batch.end();
    }

    private void drawCentered(BitmapFont font, String text, float cx, float y) {
        layout.setText(font, text);
        font.draw(batch, text, cx - layout.width / 2f, y);
    }


    private void drawStars() {
        shape.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < stars.getStarCount(); i++) {
            float size       = stars.getStarSize(i);
            float brightness = 0.5f + size * 0.15f;

            shape.setColor(brightness, brightness, brightness + 0.1f, 1f);
            shape.rect(stars.getStarX(i), stars.getStarY(i), size, size);
        }

        shape.end();
    }

    private void drawBullets(List<Bullet> bullets) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(1f, 1f, 0f, 1f);

        for (Bullet b : bullets) {
            shape.rect(b.x, b.y, b.width, b.height);
        }

        shape.end();
    }

    private void drawAsteroids(List<Asteroid> asteroids) {
        for (Asteroid asteroid : asteroids) {
            batch.draw(
                asteroidTexture,
                asteroid.x, asteroid.y,
                asteroid.width  / 2f, asteroid.height / 2f,
                asteroid.width, asteroid.height,
                1f, 1f,
                asteroid.rotacao,
                0, 0,
                asteroidTexture.getWidth(), asteroidTexture.getHeight(),
                false, false
            );
        }
    }

    private void drawGoal(float goalX, float goalY, float goalSize) {
        TextureRegion currentFrame = goalAnimation.getKeyFrame(goalStateTime, true);
        float drawW = goalSize * ((float) GOAL_FRAME_WIDTH / GOAL_FRAME_HEIGHT);
        float drawH = goalSize;
        batch.draw(currentFrame, goalX, goalY, drawW, drawH);
    }

    private void drawPlayer(Player player) {
        TextureRegion currentFrame = playerAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, player.x, player.y, player.width, player.height);
    }

    public void dispose() {
        batch.dispose();
        shape.dispose();
        playerSheet.dispose();
        asteroidTexture.dispose();
        goalSheet.dispose();
        hudRenderer.dispose();
        fontTitle.dispose();
        fontSub.dispose();
    }
}