package Framework;

import GameObjects.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Jeorje on 02.01.2017.
 */
public class KeyInput implements KeyListener
{
    private Player player;

    public KeyInput(Player player)
    {
        this.player = player;
    }

    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)
        {
            player.setLeft(true);
        }
        if (key == KeyEvent.VK_RIGHT)
        {
            player.setRight(true);
        }
        if (key == KeyEvent.VK_UP && player.getFalling() == false)
        {
            player.setFalling(true);
            player.setUp(true);
        }
        if (key == KeyEvent.VK_F)
        {
            player.setFiring(true);
        }

    }

    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)
        {
            player.setLeft(false);
        }
        if (key == KeyEvent.VK_RIGHT)
        {
            player.setRight(false);
        }
        if (key == KeyEvent.VK_UP )
        {
            player.setUp(false);
            player.setFalling(true);
        }
        if (key == KeyEvent.VK_F)
        {
            player.setFiring(false);
        }
    }

    public void keyTyped(KeyEvent e) {}
}
