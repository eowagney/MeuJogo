package com.seunome.meujogo.starts;

public class MenuStars {

    private final int starCount;
    private final float[] starX;
    private final float[] starY;
    private final float[] starSize;
    private final float[] starSpeed;

    public MenuStars(int starCount) {
        this.starCount = starCount;
        this.starX = new float[starCount];
        this.starY = new float[starCount];
        this.starSize = new float[starCount];
        this.starSpeed = new float[starCount];
    }

    public void init(float width, float height) {
        for (int i = 0; i < starCount; i++) {
            starX[i] = (float) Math.random() * width;
            starY[i] = (float) Math.random() * height;

            float layer = (float) Math.random();
            starSize[i] = 1f + layer * 2f;
            starSpeed[i] = 12f + layer * 40f;
        }
    }

    public void update(float delta, float width, float height) {
        for (int i = 0; i < starCount; i++) {
            starY[i] -= starSpeed[i] * delta;

            if (starY[i] < 0) {
                starY[i] = height + 4f;
                starX[i] = (float) Math.random() * width;
            }
        }
    }

    public int getStarCount() {
        return starCount;
    }

    public float getStarX(int index) {
        return starX[index];
    }

    public float getStarY(int index) {
        return starY[index];
    }

    public float getStarSize(int index) {
        return starSize[index];
    }
}