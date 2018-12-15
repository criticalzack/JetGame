package com.deitel.cannongame;

import android.graphics.Rect;

import edu.noctrl.craig.generic.GameObject;
import edu.noctrl.craig.generic.GameSprite;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.World;

public class Projectile extends GameSprite
{
    //source of the projectile
    static final Rect PROJECTILE = new Rect(585,10,585+52,10+50);
    static final Point3F SCALE = new Point3F((float)0.5,(float)0.5,1);

    public Projectile(World theWorld) {
        super(theWorld);
        this.collidesWith = Collision.SolidAI;

    }

    @Override
    public Rect getSource() {
        return PROJECTILE;
    }

    @Override
    public Point3F getScale() {
        return SCALE;
    }

    @Override
    public void cull() {
        ((ShipWorld)this.world).projectileRemoved();
    }

    @Override
    public void collision(GameObject other)
    {
        if(other instanceof EnemyObject)
            this.kill();
    }
}
