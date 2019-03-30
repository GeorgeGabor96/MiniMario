package Framework;

import java.awt.*;

/**
 * Created by Jeorje on 01.01.2017.
 */
public abstract class GameObject
{
    protected double x;
    protected double y;

    protected double velX;
    protected double velY;

    public GameObject(double x, double y)
    {
        this.x = x;
        this.y = y;
        this.velX = 0.0;
        this.velY = 0.0;
    }

    public abstract void draw(Graphics2D g);

    public abstract void update();

    public abstract Rectangle getBounds();

    //geteri
    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getVelX()
    {
        return velX;
    }

    public double getVelY()
    {
        return velY;
    }

    //setari
    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void setVelX(double velX)
    {
        this.velX = velX;
    }

    public void setVelY(double velY)
    {
        this.velY = velY;
    }
}
