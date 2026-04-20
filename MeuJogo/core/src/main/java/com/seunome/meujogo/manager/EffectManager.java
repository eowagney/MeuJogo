package com.seunome.meujogo.manager;

import com.seunome.meujogo.entity.FloatingText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EffectManager {

    private List<FloatingText> texts = new ArrayList<>();

    public void spawnText(float x, float y, String text) {
        texts.add(new FloatingText(x, y, text));
    }

    public void update(float delta) {
        Iterator<FloatingText> it = texts.iterator();

        while (it.hasNext()) {
            FloatingText t = it.next();
            t.update(delta);

            if (!t.isAlive()) {
                it.remove();
            }
        }
    }

    public List<FloatingText> getTexts() {
        return texts;
    }

    public void reset() {
        texts.clear();
    }
}