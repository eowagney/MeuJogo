package com.seunome.meujogo.renderer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.seunome.meujogo.MainGame;

public class MenuPanelRenderer {

    private final MainGame game;
    private final ShapeRenderer shape;
    private final BitmapFont fontTitulo;
    private final BitmapFont fontNormal;
    private final GlyphLayout layout;

    public MenuPanelRenderer(MainGame game) {
        this.game = game;

        shape = new ShapeRenderer();
        layout = new GlyphLayout();

        fontTitulo = new BitmapFont();
        fontTitulo.getData().setScale(2.2f);

        fontNormal = new BitmapFont();
        fontNormal.getData().setScale(1.0f);
    }

    public void draw(float width, float height, float pulse) {
        float cx = width / 2f;
        float cy = height / 2f;

        float padV = 16f;
        float padH = 22f;
        float gapNorm = 10f;
        float gapSep = 8f;

        float altTitulo = 30f;
        float altTexto = 14f;
        float altBotao = 30f;

        float panelHeight = padV
                + altTitulo
                + gapSep + 1f + gapSep
                + altTexto + gapNorm
                + altTexto + gapNorm
                + altTexto + gapNorm
                + altTexto + gapSep
                + altBotao
                + padV;

        float panelWidth = 390f;
        float panelX = cx - panelWidth / 2f;
        float panelY = cy - panelHeight / 2f;

        float separatorY = panelY + panelHeight - padV - altTitulo - gapSep;
        float buttonY = panelY + padV;

        drawPanelShape(panelX, panelY, panelWidth, panelHeight, separatorY, buttonY, padH, altBotao, pulse);
        drawPanelText(cx, panelY, panelHeight, separatorY, buttonY, padV, gapNorm, gapSep, altTexto, altBotao, pulse);
    }

    private void drawPanelShape(float x, float y, float w, float h,
                                float separatorY, float buttonY, float padH,
                                float buttonHeight, float pulse) {

        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(0.04f, 0.06f, 0.14f, 0.88f);
        shape.rect(x, y, w, h);

        float borderGlow = 0.5f + pulse * 0.4f;
        shape.setColor(0.1f, borderGlow, 1f, 0.9f);
        shape.rect(x, y, w, 2f);
        shape.rect(x, y + h - 2f, w, 2f);
        shape.rect(x, y, 2f, h);
        shape.rect(x + w - 2f, y, 2f, h);

        shape.setColor(0.15f, 0.5f, 1f, 0.45f);
        shape.rect(x + 24f, separatorY, w - 48f, 1f);

        shape.setColor(0.05f, 0.45f + pulse * 0.15f, 0.12f + pulse * 0.08f, 0.9f);
        shape.rect(x + padH, buttonY, w - padH * 2f, buttonHeight);

        shape.end();
    }

    private void drawPanelText(float centerX, float panelY, float panelHeight,
                               float separatorY, float buttonY,
                               float padV, float gapNorm, float gapSep,
                               float textHeight, float buttonHeight, float pulse) {

        game.batch.begin();

        float currentY = panelY + panelHeight - padV;

        fontTitulo.setColor(0.4f, 0.85f, 1f, 1f);
        drawCenteredText(fontTitulo, "SINAL E SORTE", centerX, currentY);

        currentY = separatorY - gapSep;

        fontNormal.setColor(0.65f, 0.78f, 0.98f, 0.9f);
        drawCenteredText(fontNormal, "Terra -> Marte: mantenha o sinal!", centerX, currentY);
        currentY -= textHeight + gapNorm;

        fontNormal.setColor(0.55f, 0.68f, 0.9f, 0.85f);
        drawCenteredText(fontNormal, "WASD / Setas = mover", centerX, currentY);
        currentY -= textHeight + gapNorm;

        drawCenteredText(fontNormal, "ESPACO = atirar", centerX, currentY);
        currentY -= textHeight + gapNorm;

        fontNormal.setColor(0.78f, 0.58f, 0.3f, 0.85f);
        drawCenteredText(fontNormal, "Alguns asteroides podem ser destruidos", centerX, currentY);

        float enterAlpha = 0.85f + pulse * 0.15f;
        fontNormal.getData().setScale(1.1f);
        fontNormal.setColor(0.25f, 1f, 0.5f, enterAlpha);
        drawCenteredText(fontNormal, "[ ENTER ] INICIAR MISSAO", centerX, buttonY + buttonHeight - 8f);
        fontNormal.getData().setScale(1.0f);

        game.batch.end();
    }

    private void drawCenteredText(BitmapFont font, String text, float centerX, float y) {
        layout.setText(font, text);
        font.draw(game.batch, text, centerX - layout.width / 2f, y);
    }

    public void dispose() {
        shape.dispose();
        fontTitulo.dispose();
        fontNormal.dispose();
    }
}