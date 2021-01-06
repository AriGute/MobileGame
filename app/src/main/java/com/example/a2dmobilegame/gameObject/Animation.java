package com.example.a2dmobilegame.gameObject;

import android.graphics.Bitmap;

import com.example.a2dmobilegame.MainGameThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Animation represent a single animation.
 */
public class Animation {
    private int currentFrame = 0;
    private List<Bitmap> anim;
    private float frameRate = 1;
    private float frameTime = 0;

    public Animation(float frameRate){
        anim = new ArrayList<Bitmap>();
        this.frameTime = frameRate;
    }

    /**
     * Add frame to this animation.
     * @param img bitmap image.
     */
    public void addFrame(Bitmap img){
        anim.add(img);
    }

    /**
     * Get the current frame that should be deployed.
     * @return current frame as Bitmap.
     */
    public Bitmap getFrame(){
        Bitmap frame = anim.get(currentFrame);
        if(frameTime <=0 ) {
            frameTime = frameRate;
            if(currentFrame < anim.size()-1) {
                currentFrame++;
            }else {
                currentFrame = 0;
            }
        }else {
            frameTime -= 1/MainGameThread.getDeltaTime();
        }
        return frame;
    }

    /**
     * Reset the animation to the first frame.
     */
    public void resetAnim(){
        frameTime = 0;
        currentFrame = 0;
    }
}
