package GameObjects;

import Framework.Animation;
import Framework.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Jeorje on 05.01.2017.
 */
public class Coin extends GameObject
{
    private static final int diameter = 32;
    private static final int points = 1;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private static final int numFrames = 7;
    private Animation animation;

    public Coin(double x, double y)
    {
        super(x, y);

        //incarca sprites
        sprites = new ArrayList<BufferedImage[]>();

        try
        {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Resources/sprite_sheets/coin_sprite_sheet.png"));
            BufferedImage[] images_array = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                images_array[i] = spriteSheet.getSubimage(i * diameter, 0, diameter, diameter);
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

    public void update()
    {
        animation.update();
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(animation.getImage(), (int)x, (int)y, null);
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, diameter, diameter);
    }

    public static int getPoints()
    {
        return points;
    }

}
