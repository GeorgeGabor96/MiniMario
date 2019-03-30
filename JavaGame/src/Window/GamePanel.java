package Window;
import Framework.KeyInput;
import Framework.Sound;
import GameObjects.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Jeorje on 03.01.2017.
 */
public class GamePanel extends JPanel implements Runnable
{
    //fields
    public static final int WIDTH = 1905;
    public static final int HEIGHT = 1000;

    private Thread thread;
    private boolean running = false;

    private BufferedImage image;//imagine secundara
    private Graphics2D g;//cu el desenam

    private int FPS = 60;//target FPS
    private double averageFPS;//cate FPS chiar s-au facut, se propaga erori de timp
    private BufferedImage map;

    private Font fpsFont;
    private Font scoreFont;
    private Font victoryDefeatFont;

    //private boolean gameEnd;
    private Handler handler;
    private Camera camera;

    public GamePanel()
    {
        super();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //pentru keyboard input
        this.setFocusable(true);
        this.requestFocus();
    }

    public void addNotify()
    {
        super.addNotify();
        if(thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void init()
    {
        handler = new Handler();
        try
        {
            map = ImageIO.read(getClass().getResourceAsStream("/Resources/sprite_sheets/game_map.png"));
        } catch (Exception e){}
        LoadImageMap(map);
        this.addKeyListener(new KeyInput(handler.getPlayer()));
        camera = new Camera(0, 0);
        Sound.init();
        Sound.music(Sound.BACKGROUND);
    }

    //ce face noul thread
    public void run()
    {
        init();
        running = true;
        //gameEnd = false;
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        fpsFont = g.getFont();
        scoreFont = new Font("SansSerif", Font.BOLD, 18);
        victoryDefeatFont = new Font("SansSerif", Font.BOLD, 30);

        long startTime;//in nano
        long URDTimeMillis;//timp trecut de la inceput loop pana cand a fost terminata desenarea unui cadru
        long waitTime;
        long totalTime = 0;//timp in care s-au facut numarul de FPS, in nano

        int frameCount = 0;
        int maxFrameCount = FPS;

        long targetTime = 1000 / FPS;//in cat timp ar trebuie sa se faca o bucla(~16 milisecunde, 0.016 secunde)

        //GAME LOOP
        while(running)
        {
            startTime = System.nanoTime();

            gameUpdate();
            gameRender();
            gameDraw();

            URDTimeMillis = (System.nanoTime() - startTime) / 1000000;//facem in mili
            waitTime = targetTime - URDTimeMillis;//pentru limitare

            try
            {
                Thread.sleep(waitTime);
            }
            catch (Exception e){}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == maxFrameCount)
            {
                averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);// = 1 sec / (timp in mili in care s-a facut un desenat un frame in program)
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    private void gameUpdate()
    {
        handler.update();
        camera.update(handler.getPlayer());
        //System.out.println(camera.getX() + " " + camera.getY());
    }

    public void gameRender()
    {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //row 140 si 142 tin camera fixa
        g.translate(camera.getX(),camera.getY());//am observat ca se comporta ca un fel de incrementare, se numa in pasi automati, daca las numai asta
        handler.draw(g);
        g.translate(-camera.getX(),-camera.getY());//readuce camera pe pozitia buna , nu o lasa sa se deplaseze singura

        g.setColor(Color.BLACK);
        g.drawString("FPS : " + averageFPS, 10, 10);
        g.setFont(scoreFont);
        g.drawString("Score : " + handler.getPlayer().getScore(), 1700, 80);
        g.drawString("Bullets : " + handler.getPlayer().getBulletsNumber(), 1700, 100);
        g.drawString("Life : " + handler.getPlayer().getLifes(), 1700, 120);


        //rezultate
        if (handler.getPlayer().isDead())
        {
            g.setFont(victoryDefeatFont);
            g.setColor(Color.RED);
            g.drawString("-- GAME OVER --", WIDTH / 2 - 120, HEIGHT / 2);
            g.drawString(" YOUR SCORE : " + handler.getPlayer().getScore(), WIDTH / 2 - 130, HEIGHT / 2 + 30);
            //gameEnd = true;
        }
        if (handler.getEnemies().size() == 0)
        {
            handler.getPlayer().setFinish();
            g.setFont(victoryDefeatFont);
            g.setColor(Color.RED);
            g.drawString("-- VICTORY --", WIDTH / 2 - 100, HEIGHT / 2);
            g.drawString(" YOUR SCORE : " + handler.getPlayer().getScore(), WIDTH / 2 - 130, HEIGHT / 2 + 30);
            //gameEnd = true;
        }
        g.setFont(fpsFont);
    }

    private void gameDraw()
    {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();//opreste g2
    }

    private void LoadImageMap(BufferedImage image)
    {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                int pixel = image.getRGB(i, j);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255 && green == 255 && blue == 255)//pixel alb
                {
                    handler.getBlocks().add(new Block(i * 32, j * 32));
                }
                if (red == 255 && green == 0 && blue == 0)//pixel rosu
                {
                    handler.getLava().add(new LavaBlock(i * 32, j * 32));
                }
                if (red == 0 && green == 0 && blue == 255)//pixel albastru
                {
                    handler.setPlayer(new Player(i * 32, j * 32));
                }
                if (red == 255 && green == 0 && blue == 255)//pixel mov
                {
                    handler.getEnemies().add(new Enemy(i * 32, j * 32));
                }
                if (red == 255 && green == 255 && blue == 0)//pixel galben
                {
                    handler.getCoins().add(new Coin(i * 32, j * 32));
                }
                if (red == 255 && green == 128 && blue == 192)//pixel roz
                {
                    handler.getGifts().add(new Gift(i * 32, j * 32));
                }
            }
        }
    }
}
