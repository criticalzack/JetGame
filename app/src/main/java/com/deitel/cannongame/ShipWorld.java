package com.deitel.cannongame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import edu.noctrl.craig.generic.GameObject;
import edu.noctrl.craig.generic.Point3F;
import edu.noctrl.craig.generic.SoundManager;
import edu.noctrl.craig.generic.World;

public class ShipWorld extends World implements WorldFunctions {
    protected ShipObject shipObject;
    protected SoundManager soundManager;
    protected int projectilesOnScreen = 0;
    protected final int MAX_PROJECTILES = 3;
    protected int spawnInterval = randomWithRange(1000, 6000);
    public int enemiesKilled = 0;
    public int projectilesFired = 0;
    public int points = 0;
    public ArrayList<EnemyObject> enemies;


    public ShipWorld(StateListener listener, SoundManager sounds) {
        super(listener, sounds);
        soundManager = sounds;
        //create ship object and add it to the world
        shipObject = new ShipObject(this);
        this.addObject(shipObject);
    }

    //figuring out the position of objects must take place in this method, because otherwise the width and height of the screen aren't initialized
    @Override
    protected void initialize()
    {
        shipObject.position = new Point3F(width / 6, height / 2, 0);
        enemies = new ArrayList<EnemyObject>();
        //these should be separate enemy objects, but I'm using the same object just to test the random positioning
        //this logic came from Craig's document Project 3 Hints: https://docs.google.com/document/d/1tEAWzt2bm0XoRe0JiB2wQFdG6PC99D2xHZmFKITFb5Y/edit
        for(int i = 0; i < 3; i++)
            createEnemy();

    }

    protected void createEnemy()
    {
        EnemyObject enShip;
        int typeOfEnemy = randomWithRange(1,2);
        switch(typeOfEnemy)
        {
            case 1:
                enShip = new Goomba(this);
                break;
            case 2:
                enShip = new Koopa(this);
                break;
            default:
                enShip = new Goomba(this);
                break;
        }
        //make sure you're not spawning an enemy directly on top of the ship
        do {
            enShip.position = new Point3F(randomWithRange(width / 6, width), randomWithRange(0, height), 0);
            //test for the beginning of the game when bounds isn't initialized yet
            if(shipObject.bounds == null)
                break;
        } while(shipObject.bounds.contains(enShip.position.X, enShip.position.Y));
        this.addObject(enShip);
        synchronized (enemies) {
            enemies.add(enShip);
        }
    }

    @Override
    public void update(float elapsedTimeMS)
    {
        super.update(elapsedTimeMS);
        spawnInterval -= elapsedTimeMS;
        if(0 >= spawnInterval)
        {
            createEnemy();
            spawnInterval = randomWithRange(1000,6000);
        }
    }


    //helper method for spawning enemies in random locations
    protected int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    protected Point3F moveTowardPoint(float startX, float endX, float startY, float endY)
    {
        float dx = endX - startX;
        float dy = endY - startY;
        float h = (float)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        return new Point3F(dx/h, dy/h,0);
    }

    @Override
    public void recordEnemyKilled(EnemyObject killed)
    {
        if(killed.killedByPlayer) {
            soundManager.playSound(SoundManager.ALIEN_HIT);
            enemiesKilled++;
            points += killed.points;
        }
        synchronized (enemies) {
            enemies.remove(killed);
        }
    }

    @Override
    public void recordEnemyHit() {

    }
    @Override
    public void projectileRemoved()
    {
        projectilesOnScreen--;
    }


}
