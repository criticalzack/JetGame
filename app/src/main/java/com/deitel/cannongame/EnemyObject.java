package com.deitel.cannongame;

import android.graphics.Rect;

import edu.noctrl.craig.generic.GameObject;
import edu.noctrl.craig.generic.GameSprite;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.World;

public abstract class EnemyObject extends GameSprite {

    static final Point3F SCALE = new Point3F((float).5,(float).5,1);
    boolean killedByPlayer = false;
    int points;


    public EnemyObject(World theWorld) {
        super(theWorld);
        this.substance = Collision.SolidAI;
        this.collidesWith = Collision.SolidPlayer;
    }


    @Override
    public Point3F getScale() {
        return SCALE;
    }

    @Override
    public void cull() {
        ((WorldFunctions)(this.world)).recordEnemyKilled(this);
    }

    @Override
    public void collision(GameObject other)
    {
        if(other instanceof Projectile) {
            killedByPlayer = true;
            this.kill();
        }
    }
}
