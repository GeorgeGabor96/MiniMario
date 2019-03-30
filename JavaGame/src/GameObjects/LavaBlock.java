package GameObjects;

import Framework.Animation;
import Framework.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Jeorje on 14.01.2017.
 */
public class LavaBlock extends GameObject
{
    protected static final int WIDTH = 32;
    protected static final int HEIGHT = 32;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private static final int numFrames = 6;
    private Animation animation;

    public LavaBlock(double x, double y)
    {
        super(x, y);

        //incarca sprites
        sprites = new ArrayList<BufferedImage[]>();

        try
        {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Resources/sprite_sheets/lava_sprite_sheet.png"));
            BufferedImage[] images_array = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                images_array[i] = spriteSheet.getSubimage(i * WIDTH, 0, WIDTH, HEIGHT);
            }
            sprites.add(images_array);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        animation = new Animation(100);
        animation.setFrames(sprites.get(0));
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(animation.getImage(), (int)x, (int)y, null);
    }

    // nu se mai misca
    public void update()
    {
        animation.update();
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int) x , (int) y , WIDTH, HEIGHT);
    }

    public int getHeight()
    {
        return HEIGHT;
    }
}
