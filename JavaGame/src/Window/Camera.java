package Window;

import Framework.GameObject;

/**
 * Created by Jeorje on 09.01.2017.
 */
public class Camera
{
    private int x;
    private int y;

    public Camera(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void update(GameObject player)
    {
        x = (int)-player.getX() + GamePanel.WIDTH / 2;
        //y = (int)-player.getY() + GamePanel.HEIGHT / 2;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
