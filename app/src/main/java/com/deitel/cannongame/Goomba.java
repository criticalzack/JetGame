package com.deitel.cannongame;

import android.graphics.Rect;

import edu.noctrl.craig.generic.World;

public class Goomba extends EnemyObject {

    static Rect SOURCE = new Rect(174,326,174+51,326+54);
    public Goomba(World theWorld) {
        super(theWorld);
        points = 50;
    }

    @Override
    public Rect getSource() {
        return SOURCE;
    }

}
