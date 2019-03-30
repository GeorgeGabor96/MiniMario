package Window;

import Framework.Sound;
import GameObjects.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Jeorje on 03.01.2017.
 */
public class Handler
{
    private ArrayList<Block> blocks;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private ArrayList<Coin> coins;
    private ArrayList<LavaBlock> lava;
    private ArrayList<Gift> gifts;
    private Player player;

    public Handler()
    {
        blocks = new ArrayList<Block>();
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        coins = new ArrayList<Coin>();
        lava = new ArrayList<LavaBlock>();
        gifts = new ArrayList<Gift>();
    }

    public ArrayList<Block> getBlocks()
    {
        return blocks;
    }

    public ArrayList<Enemy> getEnemies()
    {
        return enemies;
    }

    public ArrayList<Coin> getCoins()
    {
        return coins;
    }

    public ArrayList<LavaBlock> getLava()
    {
        return lava;
    }

    public ArrayList<Gift> getGifts()
    {
        return gifts;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public void update()
    {
        //update player
        player.update();

        //update lava
        for (int i = 0; i < lava.size(); i++)
        {
            lava.get(i).update();
        }

        //update enemies
        for (int i = 0; i < enemies.size(); i++)
        {
            enemies.get(i).update();
        }

        for (int i = 0; i < coins.size(); i++)
        {
            coins.get(i).update();
        }

        //update bullets
        for (int i = 0; i < bullets.size(); i++)
        {
            bullets.get(i).update();
        }

        //daca se poate adaugam un glont
        if (player.getCanFire() == true && player.getBulletsNumber() > 0)
        {
            Sound.music(Sound.FIREBULLET);
            if (player.getDirection() == true)
            {
                bullets.add(new Bullet(player.getX() + player.getWidth(), player.getY() + player.getHeight() / 2, true));
            }
            else
            {
                bullets.add(new Bullet(player.getX() - Bullet.getWidth(), player.getY() + player.getHeight() / 2, false));
            }
            player.setCanFire(false);
            player.setStartFiringTime(System.nanoTime());
            player.decreaseBulletsNumber();
        }

        this.Collisions();
    }

    public void draw(Graphics2D g)
    {
        //desenam player
        player.draw(g);

        //desenam blocks
        for (int i = 0; i < blocks.size(); i++)
        {
            blocks.get(i).draw(g);
        }

        //desenam lava
        for (int i = 0; i < lava.size(); i++)
        {
            lava.get(i).draw(g);
        }

        //desenam gifts
        for (int i = 0; i < gifts.size(); i++)
        {
            gifts.get(i).draw(g);
        }

        //desenam gloante
        for (int i = 0; i < bullets.size(); i++)
        {
            bullets.get(i).draw(g);
        }

        //desenam inamici
        for (int i = 0; i < enemies.size(); i++)
        {
            enemies.get(i).draw(g);
        }

        //desenam bani
        for (int i = 0; i < coins.size(); i++)
        {
            coins.get(i).draw(g);
        }
    }

    private void Collisions()
    {
        //coliziuni teren
        for (int i = 0; i < blocks.size(); i++)
        {
            Block tempBlock = blocks.get(i);

            //coliziuni player - teren
            if (player.getBoundsDown().intersects(tempBlock.getBounds()))
            {
                player.setFalling(false);
                player.setVelY(0);
                player.setY(tempBlock.getY() - Player.getHeight());
            }
            if (player.getBoundsUp().intersects(tempBlock.getBounds()))
            {
                player.setFalling(true);
                player.setVelY(2 * player.getSpeed());//CA SA COBOARE JOS
                player.setY(tempBlock.getY() + tempBlock.getHeight());
            }
            if (player.getBoundsLeft().intersects(tempBlock.getBounds()))
            {
                player.setVelX(0);
                player.setX(tempBlock.getX() + tempBlock.getWidth());
            }
            if (player.getBoundsRight().intersects(tempBlock.getBounds()))
            {
                player.setVelX(0);
                player.setX(tempBlock.getX() - Player.getWidth());
            }

            //coliziuni teren - gloante
            for (int j = 0; j < bullets.size(); j++)
            {
                if (tempBlock.getBounds().intersects(bullets.get(j).getBounds()))
                {
                    bullets.remove(j);
                    --j;
                }
            }

            //coliziuni teren inamic
            for (int j = 0; j < enemies.size(); j++)
            {
                Enemy tempEnemy = enemies.get(j);
                if (tempBlock.getBounds().intersects(tempEnemy.getBoundsDown()))
                {
                    tempEnemy.setX(tempBlock.getX() - tempEnemy.getHeight());
                }
                if (tempBlock.getBounds().intersects(tempEnemy.getBoundsLeft()))
                {
                    tempEnemy.setDirection(true);
                }
                if (tempBlock.getBounds().intersects(tempEnemy.getBoundsRight()))
                {
                    tempEnemy.setDirection(false);
                }
            }

        }

        //coliziune inamic - glont
        for (int i = 0; i < enemies.size(); i++)
        {
            Enemy tempEnemy = enemies.get(i);
            for (int j = 0; j < bullets.size(); j++)
            {
                if (tempEnemy.getBounds().intersects(bullets.get(j).getBounds()))
                {
                    bullets.remove(j);
                    tempEnemy.takeDamage(1);
                    if (tempEnemy.isDead())
                    {
                        player.addScore(Enemy.getPoints());
                        enemies.remove(i);
                        i--;
                    }
                }
            }
        }

        //coliziune player - inamic
        for (int i = 0; i < enemies.size(); i++)
        {
            Enemy tempEnemy =  enemies.get(i);
            if (tempEnemy.getBoundsUp().intersects(player.getBoundsDownForEnemy()) && player.getInvincible() == false)
            {
                Sound.music(Sound.JUMP);
                player.setVelY(-20);
                tempEnemy.takeDamage(1);
                if (tempEnemy.isDead() == true)
                {
                    player.addScore(Enemy.getPoints());
                    enemies.remove(i);
                    i--;
                }
            }
            else if (tempEnemy.getBounds().intersects(player.getBoundsLeft()) && player.getInvincible() == false)
            {
                Sound.music(Sound.TAKEDAMAGE);
                player.setVelY(-20);
                player.takeDamage(1);
                player.setInvincible(true);
            }
            else if (tempEnemy.getBounds().intersects(player.getBoundsRight()) && player.getInvincible() == false)
            {
                Sound.music(Sound.TAKEDAMAGE);
                player.setVelY(-20);
                player.takeDamage(1);
                player.setInvincible(true);
            }

        }

        //coliziuni player - coins
        for (int i = 0; i < coins.size(); i++)
        {
            if (player.getBounds().intersects(coins.get(i).getBounds()))
            {
                player.addScore(Coin.getPoints());
                coins.remove(i);
                i--;
            }
        }

        //coliziuni player - lava
        for (int i = 0; i < lava.size(); i++)
        {
            LavaBlock tempLava = lava.get(i);
            if (player.getBoundsDown().intersects(tempLava.getBounds()) && player.getInvincible() == false)
            {
                Sound.music(Sound.TAKEDAMAGE);
                player.takeDamage(1);
                player.setVelY(-20);
                player.setInvincible(true);
            }
            else if (player.getBoundsUp().intersects(tempLava.getBounds()) && player.getInvincible() == false)
            {
                Sound.music(Sound.TAKEDAMAGE);
                player.takeDamage(1);
                player.setInvincible(true);
                player.setVelY(2 * player.getSpeed());//CA SA COBOARE JOS
                player.setY(tempLava.getY() + tempLava.getHeight());
            }
        }

        //coliziuni player - gift
        for (int i = 0; i < gifts.size(); i++)
        {
            Gift tempGift = gifts.get(i);
            if (player.getBounds().intersects(tempGift.getBounds()))
            {
                player.increaseBulletsNumber(Gift.getBullets());
                player.increaseLife(Gift.getLives());
                gifts.remove(i);
                --i;
            }
        }
    }
}
