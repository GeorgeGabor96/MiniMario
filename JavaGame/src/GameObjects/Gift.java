package GameObjects;

import Framework.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Jeorje on 14.01.2017.
 */
public class Gift extends GameObject
{
    private static Color color = Color.PINK;;
    protected static final int WIDTH = 32;
    protected static final int HEIGHT = 32;
    private static final int lives = 1;
    private static final int bullets = 6;
    private BufferedImage image;

    public Gift(double x, double y)
    {
        super(x, y);

        try
        {
            image = ImageIO.read(getClass().getResourceAsStream("/Resources/sprite_sheets/gift_sprite_sheet.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g)
    {

        g.drawImage(image, (int)x , (int)y, null);
    }

    // nu se mai misca
    public void update()
    {
        return;
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int) x , (int) y , WIDTH, HEIGHT);
    }

    public static int getLives()
    {
        return lives;
    }

    public static int getBullets()
    {
        return bullets;
    }
}
