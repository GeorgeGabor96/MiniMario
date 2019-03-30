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
public class Enemy extends GameObject
{
    private static final int WIDTH = 32;
    private static final int HEIGHT = 64;
    private static final double speed = 5.0;
    private static final int points = 2;

    private boolean direction;
    private boolean dead;
    private int lifes;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private static final int numFrames = 4;
    private Animation animation;

    //animations actions
    private static int WALK_RIGHT = 0;
    private static int WALK_LEFT = 1;
    private int currentAction;


    public Enemy(double x, double y)
    {
        super(x, y);
        this.direction = false;//initial stanga
        this.dead = false;
        this.lifes = 5;

        sprites = new ArrayList<BufferedImage[]>();
        try
        {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Resources/sprite_sheets/skeleton_sprite_sheet.png"));
            for (int i = 0; i < 2 ; i++) {
                BufferedImage[] images_array = new BufferedImage[numFrames];
                for (int j = 0; j < numFrames; j++) {
                        images_array[j] = spriteSheet.getSubimage(j * WIDTH, i * HEIGHT, WIDTH, HEIGHT);
                }
                sprites.add(images_array);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        currentAction = WALK_LEFT;
        animation = new Animation(100);
        animation.setFrames(sprites.get(currentAction));
    }

    public void takeDamage(int damage)
    {
        lifes -= damage;
        if (lifes == 0)
        {
            dead = true;
        }
    }

    public void update()
    {
        if (direction == true)
        {
            x += speed;
            if (currentAction == WALK_LEFT)
            {
                animation.setFrames(sprites.get(WALK_RIGHT));
                currentAction = WALK_RIGHT;
            }
        }
        else
        {
            x -= speed;
            if (currentAction == WALK_RIGHT)
            {
                animation.setFrames(sprites.get(WALK_LEFT));
                currentAction = WALK_LEFT;
            }
        }
        animation.update();
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(animation.getImage(), (int)x, (int)y, null);
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int) x , (int) y , WIDTH, HEIGHT);
    }

    public Rectangle getBoundsDown()
    {
        return new Rectangle((int) x + 5, (int) y + HEIGHT - 5, WIDTH - 10, 5);
    }
    public Rectangle getBoundsUp()
    {
        //folosit doar pentru coliziuni mai bune cu player
        return new Rectangle((int) x, (int) y, WIDTH, 10);
    }
    public Rectangle getBoundsLeft()
    {
        return new Rectangle((int) x, (int) y + 10 , 5, HEIGHT - 10);
    }
    public Rectangle getBoundsRight()
    {
        return new Rectangle((int) x + WIDTH - 5, (int) y + 10, 5, HEIGHT - 10);
    }

    //setari
    public void setDirection(boolean direction)
    {
        this.direction = direction;
    }

    //getari

    public static int getHeight()
    {
        return HEIGHT;
    }

    public static int getPoints()
    {
        return points;
    }

    public boolean isDead()
    {
        return dead;
    }
}
