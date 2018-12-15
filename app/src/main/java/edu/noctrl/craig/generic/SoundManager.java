package edu.noctrl.craig.generic;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.deitel.cannongame.R;

public class SoundManager {
	// constants and variables for managing sounds
	public static int FIRE_ID = 0;
	public static int JET_HIT = 1;
	public static int ALIEN_HIT = 2;
	
	
	protected SoundPool soundPool; // plays sound effects
	protected Context context;
	
	public SoundManager(Context context){
        SoundPool.Builder builder = new SoundPool.Builder();
        AudioAttributes.Builder atts = new AudioAttributes.Builder();
        atts.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        atts.setUsage(AudioAttributes.USAGE_GAME);
        atts.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
        builder.setAudioAttributes(atts.build());
        builder.setMaxStreams(3);

        // initialize SoundPool to play the app's three sound effects
		soundPool = builder.build();
		this.context = context;
		initializeSounds();
	}
	
	public void releaseResources(){
		soundPool.release(); // release all resources used by the SoundPool
		soundPool = null;
	}
	
	protected void initializeSounds(){
        FIRE_ID = soundPool.load(context, R.raw.smb_fireball, 1);
        JET_HIT = soundPool.load(context, R.raw.smb_bowserfalls, 1);
        ALIEN_HIT = soundPool.load(context, R.raw.smb_kick, 1);
	}
	
	public void playSound(int sound){
		soundPool.play(sound, 1, 1, 1, 0, 1f);
	}
	
}
