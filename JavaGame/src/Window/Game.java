package Window;

import javax.swing.*;

/**
 * Created by Jeorje on 01.01.2017.
 */
public class Game
{
    public static void main(String[] args) {
        JFrame window = new JFrame("MINI MARIO");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }
}
