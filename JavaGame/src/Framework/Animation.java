package Framework;

import java.awt.image.BufferedImage;
/**
 * Created by Jeorje on 18.01.2017.
 */
public class Animation
{
    private BufferedImage[] frames;
    private int currentFrame;

    private long startTime;
    private long delay;//timpul pentru care un frame se va afisa

    public Animation(long delay)
    {
        super();
        this.delay = delay;
    }

    public void setDelay(long delay)
    {
        this.delay = delay;
    }


    public void setFrames(BufferedImage[] frames)
    {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();//timpul la care s-a afisat prima data frame-ul curent
    }

    public void update()
    {
        if(delay == 0)
        {
            return;
        }

        long timeElapsed = (System.nanoTime() - startTime) / 1000000;//timpul pentru care un frame a fost afisat
        if (timeElapsed > delay)
        {
            currentFrame++;
            startTime = System.nanoTime();
        }

        if(currentFrame == frames.length)//am terminat de afisat toate frame-urile, o luam de la capat
        {
            currentFrame = 0;
        }

    }

    public BufferedImage getImage()
    {
        return frames[currentFrame];
    }
}
