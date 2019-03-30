package GameObjects;

import Framework.Animation;
import Framework.GameObject;
import Framework.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Jeorje on 02.01.2017.
 */
public class Player extends GameObject
{
    private static final int WIDTH = 32;
    private static final int HEIGHT = 64;

    private double gravity;
    private double MaxVelY;

    private static final double speed = 5.0;

    private boolean falling;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean direction;

    //pentru gloante
    private static final int firingDelayTime = 300;//in milisecunde
    private long startFiringTime;//tine minte momentul de timp cand a tras un glont
    private boolean firing;
    private boolean canFire;

    //rezistenta
    private int lifes;
    private boolean dead;

    //invincibil
    private boolean invincible;
    private int invincibleTime = 1000;
    private long invincibleStartTime;

    private boolean finish;
    private int score;
    private int bulletsNumber;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private int[] numFrames = {10, 10, 3, 3, 8, 8, 1, 1};
    private Animation animation;

    //animation actions
    private static final int STANCE_RIGHT = 0;
    private static final int STANCE_LEFT = 1;
    private static final int JUMPING_RIGHT = 2;
    private static final int JUMPING_LEFT = 3;
    private static final int WALKING_RIGHT = 4;
    private static final int WALKING_LEFT = 5;
    private static final int HIT_RIGHT = 6;
    private static final int HIT_LEFT = 7;
    private int currentAction;

    public Player(double x, double y)
    {
        super(x, y);
        gravity = 1;
        MaxVelY = 10;
        falling = true;
        left = false;
        right = false;
        up = false;
        falling = true;
        direction = true;//la inceput este indreptat in partea dreapta
        bulletsNumber = 10;
        finish = false;

        //gloante
        firing = false;
        canFire = false;
        startFiringTime = 0;

        //rezistenta
        lifes = 5;
        dead = false;

        //invincibil
        invincible = false;

        score = 0;

        //animation
        sprites = new ArrayList<BufferedImage[]>();
        try
        {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/Resources/sprite_sheets/mario_sprite_sheet.png"));
            for (int i = 0; i < numFrames.length ; i++) {
                BufferedImage[] images_array = new BufferedImage[numFrames[i]];
                for (int j = 0; j < numFrames[i]; j++) {
                    images_array[j] = spriteSheet.getSubimage(j * WIDTH, i * HEIGHT, WIDTH, HEIGHT);
                }
                sprites.add(images_array);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        currentAction = STANCE_RIGHT;
        animation = new Animation(200);
        animation.setFrames(sprites.get(currentAction));
    }

    public void takeDamage(int damageTaken)
    {
        lifes -= damageTaken;
        if (lifes <= 0)
        {
            dead = true;
        }
    }

    public void draw(Graphics2D g)
    {
        /*if (dead)
        {
            drawDead(g);
        }
        else if (invincible)
        {
            drawInvincible(g);
        }
        else
        {
            drawNormal(g);
        }*/
        g.drawImage(animation.getImage(), (int) x, (int) y, null);
    }

    public synchronized void update()
    {
        if (dead == false) {
            //miscare pe lateral
            if (left) {
                direction = false;
                velX = -speed;
            } else if (right) {
                direction = true;
                velX = speed;
            } else {
                velX = 0;
            }

            //miscare pe vertical
            //jumping
            if (up && !falling)
            {
                Sound.music(Sound.JUMP);
                velY = -4.5 * speed;
                falling = true;
            }
            if (falling || up == false)
            {
                velY += gravity;
                if (velY > MaxVelY) {
                    velY = MaxVelY;
                }
            }

            x += velX;
            y += velY;

            updateFire();
            if (invincible)
            {
                updateInvincible();
            }
        }

        nextAnimation();

        animation.update();
    }

    private void nextAnimation()
    {
        if (invincible == true && direction == false && currentAction != HIT_LEFT)
        {
            currentAction = HIT_LEFT;
            animation.setFrames(sprites.get(currentAction));
        }
        if (invincible == true && direction == true && currentAction != HIT_RIGHT)
        {
            currentAction = HIT_RIGHT;
            animation.setFrames(sprites.get(currentAction));
        }
        if (invincible == false && falling == true && direction == false && currentAction != JUMPING_LEFT)
        {
            currentAction = JUMPING_LEFT;
            animation.setDelay(250);
            animation.setFrames(sprites.get(currentAction));
        }
        if (invincible == false && falling == true && direction == true && currentAction != JUMPING_RIGHT)
        {
            currentAction = JUMPING_RIGHT;
            animation.setDelay(250);
            animation.setFrames(sprites.get(currentAction));
        }
        if (invincible == false && falling == false && left == false && direction == false && currentAction != STANCE_LEFT)
        {
            currentAction = STANCE_LEFT;
            animation.setDelay(100);
            animation.setFrames(sprites.get(currentAction));
        }
        if (invincible == false && falling == false && right == false && direction == true && currentAction != STANCE_RIGHT)
        {
            currentAction = STANCE_RIGHT;
            animation.setDelay(100);
            animation.setFrames(sprites.get(currentAction));
        }
        if (invincible == false && falling == false && left == true && currentAction != WALKING_LEFT)
        {
            currentAction = WALKING_LEFT;
            animation.setDelay(100);
            animation.setFrames(sprites.get(currentAction));
        }
        if (invincible == false && falling == false && right == true && currentAction != WALKING_RIGHT)
        {
            currentAction = WALKING_RIGHT;
            animation.setDelay(100);
            animation.setFrames(sprites.get(currentAction));
        }

    }

    public void updateInvincible()
    {
        if ((System.nanoTime() - invincibleStartTime) / 1000000 > invincibleTime)
        {
            invincible = false;
        }
    }

    private void updateFire()
    {
        if (firing && bulletsNumber > 0)
        {
            long waitTime = (System.nanoTime() - startFiringTime) / 1000000;//cat a trecut de cand am tras ultimul glont
            if (waitTime > firingDelayTime)
            {
                canFire = true;
            }
        }
    }

    public void addScore(int score)
    {
        if (finish == false)
        {
            this.score += score;
        }
    }

    public Rectangle getBoundsDown()
    {
        return new Rectangle((int) x + 5, (int) y + HEIGHT - 5, WIDTH - 10, 5);
    }
    public Rectangle getBoundsUp()
    {
        return new Rectangle((int) x + 5, (int) y, WIDTH - 10, 5);
    }
    public Rectangle getBoundsLeft()
    {
        return new Rectangle((int) x, (int) y + 5 , 5, HEIGHT - 10);
    }
    public Rectangle getBoundsRight()
    {
        return new Rectangle((int) x + WIDTH - 5, (int) y + 5,  5, HEIGHT - 10);
    }
    public Rectangle getBoundsDownForEnemy()
    {
        //folosit doar pentru coliziuni mai bune cu inamici
        return new Rectangle((int) x , (int) y + getHeight() - 10, WIDTH, 10);
    }
    public Rectangle getBounds()
    {
        return new Rectangle((int) x , (int) y , WIDTH, HEIGHT);
    }

    //getari
    public boolean getFalling() {
        return falling;
    }

    public static int getWidth()
    {
        return WIDTH;
    }

    public static int getHeight()
    {
        return HEIGHT;
    }

    public double getSpeed()
    {
        return speed;
    }

    public boolean getCanFire()
    {
        return canFire;
    }

    public boolean getDirection()
    {
        return direction;
    }

    public boolean isDead()
    {
        return dead;
    }

    public boolean getInvincible()
    {
        return invincible;
    }

    public int getScore()
    {
        return score;
    }

    public int getBulletsNumber()
    {
        return bulletsNumber;
    }

    public int getLifes()
    {
        return lifes;
    }

    //setari
    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public void setLeft(boolean b){left = b;}
    public void setRight(boolean b){right = b;}
    public void setUp(boolean b){up = b;}

    public void setFinish()
    {
        finish = true;
    }

    public void setFiring(boolean firing)
    {
        this.firing = firing;
    }

    public void setCanFire(boolean canFire)
    {
        this.canFire = canFire;
    }

    public void setStartFiringTime(long startFiringTime)
    {
        this.startFiringTime = startFiringTime;
    }

    public void setInvincible(boolean invincible)
    {
        this.invincible = invincible;
        invincibleStartTime = System.nanoTime();
    }

    public void increaseLife(int lifes)
    {
        this.lifes += lifes;
    }

    public void increaseBulletsNumber(int bulletsNumber)
    {
        this.bulletsNumber += bulletsNumber;
    }

    public void decreaseBulletsNumber()
    {
        this.bulletsNumber--;
    }



}
