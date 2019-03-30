package GameObjects;

import Framework.GameObject;

import java.awt.*;

/**
 * Created by Jeorje on 05.01.2017.
 */
//trebuie doar sa se deplaseza in stanga sau in dreapta

public class Bullet extends GameObject
{
    private static final double speed = 10.0;
    private static final int radius = 5;
    private static final Color color = Color.RED;
    private boolean direction;//true dreapta, false stanga

    public Bullet(double x, double y, boolean direction)
    {
        super(x, y);
        this.direction = direction;
    }

    public void update()
    {
        if (direction == true)
        {
            x += speed;
        }
        else
        {
            x -= speed;
        }
    }

    public void draw(Graphics2D g)
    {
        g.setColor(color);
        g.fillOval((int) x, (int) y, 2 * radius, 2 * radius);
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int) x , (int) y , 2 * radius, 2 * radius);
    }

    public static int getWidth()
    {
        return 2 * radius;
    }
}
