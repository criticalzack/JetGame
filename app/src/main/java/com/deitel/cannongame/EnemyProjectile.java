package com.deitel.cannongame;

import edu.noctrl.craig.generic.GameObject;
import edu.noctrl.craig.generic.World;

public class EnemyProjectile extends Projectile {
    public EnemyProjectile(World theWorld)
    {
        super(theWorld);
        this.collidesWith = Collision.SolidPlayer;
    }

    @Override
    public void cull()
    {

    }

    @Override
    public void collision(GameObject other)
    {
        if(other instanceof ShipObject)
            this.kill();
    }
}
