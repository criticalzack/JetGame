package com.deitel.cannongame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import edu.noctrl.craig.generic.GameObject;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.SoundManager;

public class StageOne extends ShipWorld implements WorldFunctions
{
    private float timeRemainingMS = 10000;
    private float timeRemainingSec;

    public StageOne(StateListener listener, SoundManager sounds)
    {
        super(listener, sounds);
    }

    @Override
    public boolean onTouch(View view, MotionEvent mEvent)
    {
        //this logic for figuring out velocity also came from Craig's document Project 3 Hints: https://docs.google.com/document/d/1tEAWzt2bm0XoRe0JiB2wQFdG6PC99D2xHZmFKITFb5Y/edit
        if(projectilesOnScreen >= MAX_PROJECTILES)
            return true;
        switch (mEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //create a projectile object
                Projectile proj = new Projectile(this);
                //have it start out at the same place as the player ship
                proj.position = new Point3F(shipObject.position.X, shipObject.position.Y, shipObject.position.Z);
                proj.speed = 300;
                proj.baseVelocity = moveTowardPoint(proj.position.X, mEvent.getX(), proj.position.Y, mEvent.getY());
                proj.updateVelocity();
                this.addObject(proj);
                soundManager.playSound(SoundManager.FIRE_ID);
                projectilesOnScreen++;
                projectilesFired++;
                break;
        }

        return true;
    }

    @Override
    public void update(float elapsedTimeMS)
    {
        super.update(elapsedTimeMS);
        timeRemainingMS -= elapsedTimeMS;
        if(timeRemainingMS < 0)
            this.listener.onGameOver(true);
        timeRemainingSec = (float)(timeRemainingMS / 1000.0);
    }

    @Override
    public void draw(Canvas canvas)
    {
        if(canvas!=null){
            canvas.drawColor(Color.parseColor("#33B5E5"));

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(50);
            if(timeRemainingSec < 0)
                timeRemainingSec = 0;
            canvas.drawText("Time remaining: " + timeRemainingSec + "s.", 10, 50, paint);
            for(GameObject obj : objects){
                obj.draw(canvas);
            }
        }

        //taken from https://stackoverflow.com/questions/2655402/android-canvas-drawtext
    }

    @Override
    public void recordEnemyKilled(EnemyObject killed)
    {
        super.recordEnemyKilled(killed);
        if(enemiesKilled >= 10)
            listener.onGameOver(false);
        timeRemainingMS += 3500;
    }
}
