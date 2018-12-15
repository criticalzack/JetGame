package edu.noctrl.craig.generic;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by craig_000 on 5/9/2015.
 */
public class World implements View.OnTouchListener {
    public static Object GUI_LOCKER = new Object();
    public static Resources resources;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return true;
    }

    public static interface StateListener{
        public void onGameOver(boolean lost);
    }
    protected static final float TARGET_WIDTH = 540;
    protected static final float TARGET_HEIGHT = 960;
    protected static final float TARGET_PIXELS_PER_METER = 64F/30F;//ship len = 30m  ship base = 64px
    public static float PIXELS_PER_METER = 64F/30F;//ship len = 30m  ship base = 64px
    public double totalElapsedTime = 0;
    public int width;
    public int height;
    public StateListener listener;
    private SoundManager soundManager;
    public Point3F worldScale = Point3F.identity();

    protected ArrayList<GameObject> objects = new ArrayList<>(1000);
    protected ArrayList<GameObject> newObjects = new ArrayList<>(1000);

    public World(StateListener listener, SoundManager sounds){
        this.listener = listener;
        this.soundManager = sounds;
    }

    public void addObject(GameObject obj){
        synchronized (newObjects) {
            newObjects.add(obj);
        }
    }

    protected void initialize(){

    }

    public void updateSize(float width, float height){
        this.width = (int)width;
        this.height = (int)height;
        worldScale = Point3F.identity();
        worldScale.X = width / TARGET_WIDTH;
        worldScale.Y = width / TARGET_WIDTH;
        PIXELS_PER_METER = worldScale.Y * TARGET_PIXELS_PER_METER;
        if(objects.size()==0){
            initialize();
        }
    }

    public void update(float elapsedTimeMS){
        float interval = elapsedTimeMS / 1000.0F; // convert to seconds
        for(GameObject obj : objects){
            obj.update(interval);
        }

    }
    public void draw(Canvas canvas){
        if(canvas!=null){
            canvas.drawColor(Color.parseColor("#33B5E5"));
            for(GameObject obj : objects){
                obj.draw(canvas);
            }
        }
    }
    public void cullAndAdd(){
        CollisionDetection.checkCollisions(objects);
        GameObject go;
        ArrayList<GameObject> removed = new ArrayList<>(objects.size());
        synchronized (newObjects){
            for(int i = objects.size() - 1;i >= 0; i--){
                go = objects.get(i);
                if(go.offScreen){
                    objects.remove(i);
                    removed.add(go);
                }
            }
            for(GameObject obj : newObjects) {
                if (obj.drawOrder == GameObject.DrawOrder.Foreground)
                    objects.add(obj);
                else
                    objects.add(0, obj);
            }
            newObjects.clear();
        }
        for(GameObject obj : removed) {
            obj.cull();
        }

    }
}
