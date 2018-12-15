package com.deitel.cannongame;

import android.graphics.Rect;

import edu.noctrl.craig.generic.World;

public class Koopa extends EnemyObject {
    static Rect SOURCE = new Rect(311,313,311+53,313+82);
    public Koopa(World theWorld) {
        super(theWorld);
        points = 100;
    }

    @Override
    public Rect getSource() {
        return SOURCE;
    }
}
