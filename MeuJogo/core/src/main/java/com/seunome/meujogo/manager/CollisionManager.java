package com.seunome.meujogo.manager;

import com.seunome.meujogo.entity.Asteroid;
import com.seunome.meujogo.entity.Player;

import java.util.Iterator;
import java.util.List;

public class CollisionManager {

    private static final float PLAYER_WIDTH  = 32f;
    private static final float PLAYER_HEIGHT = 32f;

    public boolean reachedGoal(Player player, float goalX, float goalY, float goalSize) {
        return overlaps(player.x, player.y, PLAYER_WIDTH, PLAYER_HEIGHT,
                        goalX,    goalY,    goalSize,     goalSize);
    }

    public boolean handlePlayerAsteroidCollision(Player player, List<Asteroid> asteroids) {
        boolean hit = false;

        Iterator<Asteroid> iterator = asteroids.iterator();

        while (iterator.hasNext()) {
            Asteroid a = iterator.next();

            if (overlaps(player.x, player.y, PLAYER_WIDTH, PLAYER_HEIGHT,
                         a.x,      a.y,      a.width,      a.height)) {
                iterator.remove();
                hit = true;
            }
        }

        return hit;
    }

    private boolean overlaps(float ax, float ay, float aw, float ah,
                              float bx, float by, float bw, float bh) {
        return ax      < bx + bw
            && ax + aw > bx
            && ay      < by + bh
            && ay + ah > by;
    }
}