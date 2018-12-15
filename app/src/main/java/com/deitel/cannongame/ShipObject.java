package com.deitel.cannongame;

import android.graphics.Rect;

import edu.noctrl.craig.generic.GameObject;
import edu.noctrl.craig.generic.GameSprite;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.World;

public class ShipObject extends GameSprite {

    //The source of the ship
    static final Rect SHIP_OBJ = new Rect(196,175,196+47,175+51);
    static final Point3F SCALE = new Point3F(1,1,1);

    public ShipObject(World theWorld) {
        super(theWorld);
        this.type = ObjectType.Box;
        this.collidesWith = Collision.SolidAI;
        this.substance = Collision.SolidPlayer;

    }

    @Override
    public Rect getSource() {
        return SHIP_OBJ;
    }

    @Override
    public Point3F getScale() {
        return SCALE;
    }

    @Override
    public void cull() {

    }

    @Override
    public void collision(GameObject other)
    {
        if(other instanceof EnemyProjectile || other instanceof EnemyObject) {
            world.listener.onGameOver(true);

        }
    }


}
