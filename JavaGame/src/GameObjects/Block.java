package GameObjects;

import Framework.GameObject;
import java.awt.*;

/**
 * Created by Jeorje on 02.01.2017.
 */
public class Block extends GameObject
{
    private static Color color = Color.GREEN;;
    protected static final int WIDTH = 32;
    protected static final int HEIGHT = 32;

    public Block(double x, double y)
    {
        super(x, y);
    }

    public void draw(Graphics2D g)
    {
        g.setColor(color);
        g.fillRect((int) x ,(int) y, WIDTH, HEIGHT);
        g.setStroke(new BasicStroke(3));
        g.setColor(color.darker());
        g.drawRect((int) x, (int) y, WIDTH, HEIGHT);
        g.setStroke(new BasicStroke(1));
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

    public static int getWidth()
    {
        return WIDTH;
    }

    public static int getHeight()
    {
        return HEIGHT;
    }
}