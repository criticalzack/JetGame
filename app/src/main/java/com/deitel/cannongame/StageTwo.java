package com.deitel.cannongame;

import android.view.MotionEvent;
import android.view.View;

import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.SoundManager;

public class StageTwo extends ShipWorld implements WorldFunctions
{
    private boolean isClick = true;
    private float enProjInterval = 4000;

    public StageTwo(StateListener listener, SoundManager sounds)
    {
        super(listener, sounds);
    }


    @Override
    protected void createEnemy()
    {
        //limit the number of enemies on screen, otherwise it would get crazy
        if(enemies.size() > 2)
            return;
        //superclass method creates enemy and puts it at the end of the enemy list
        super.createEnemy();
        //get the enemy just created and set random movement
        EnemyObject enShip = enemies.get(enemies.size() - 1);
        enShip.baseVelocity = moveTowardPoint(enShip.position.X, (float)randomWithRange(0, width), enShip.position.Y, (float)randomWithRange(0, height));
        enShip.speed = 300;
        enShip.updateVelocity();
    }

    @Override
    public boolean onTouch(View view, MotionEvent mEvent)
    {
        switch (mEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                isClick = false;
                shipObject.position.X = mEvent.getX();
                shipObject.position.Y = mEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(isClick) {
                    if(projectilesOnScreen >= MAX_PROJECTILES)
                        return true;
                    //create a projectile object
                    Projectile proj = new Projectile(this);
                    //have it start out at the same place as the player ship
                    proj.position = new Point3F(shipObject.position.X, shipObject.position.Y, shipObject.position.Z);
                    proj.speed = 300;
                    proj.baseVelocity = new Point3F(1, 0, 0);
                    proj.updateVelocity();
                    this.addObject(proj);
                    soundManager.playSound(SoundManager.FIRE_ID);
                    projectilesOnScreen++;
                    projectilesFired++;
                }
                break;
        }

        return true;
    }

    @Override
    public void update(float elapsedTimeMS)
    {
        super.update(elapsedTimeMS);
        enProjInterval -= elapsedTimeMS;
        if(enProjInterval <= 0)
        {
            enProjInterval = 4000;
            if(!enemies.isEmpty())
            {
                EnemyObject enemyAFiring = enemies.get(randomWithRange(0, enemies.size() - 1));
                EnemyProjectile projectile = new EnemyProjectile(this);
                projectile.position = new Point3F(enemyAFiring.position.X, enemyAFiring.position.Y, enemyAFiring.position.Z);
                projectile.speed = 300;
                projectile.baseVelocity = moveTowardPoint(enemyAFiring.position.X, shipObject.position.X, enemyAFiring.position.Y, shipObject.position.Y);
                projectile.updateVelocity();
                this.addObject(projectile);
            }
        }

    }

    @Override
    public void cullAndAdd()
    {
        //if enemy is about to go off screen, keep them on screen and send them in a different direction
        for(EnemyObject enShip : enemies)
        {
            if(enShip.offScreen && !enShip.killedByPlayer)
            {
                enShip.offScreen = false;
                enShip.baseVelocity = moveTowardPoint(enShip.position.X, (float)randomWithRange(0, width), enShip.position.Y, (float)randomWithRange(0, height));
                enShip.speed = 300;
                enShip.updateVelocity();
            }
        }
        super.cullAndAdd();
    }

    @Override
    public void recordEnemyKilled(EnemyObject killed) {
        super.recordEnemyKilled(killed);
        if(enemies.isEmpty())
            listener.onGameOver(false);
    }

    @Override
    public void recordEnemyHit() {

    }

    @Override
    public void projectileRemoved() {
        super.projectileRemoved();

    }
}
