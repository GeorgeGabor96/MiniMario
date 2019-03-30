package Framework;

import java.applet.Applet;
import java.applet.AudioClip;
/**
 * Created by Jeorje on 19.01.2017.
 */
//creste memoria
public class Sound
{
    private static AudioClip clipBackGround;
    private static AudioClip clipJump;
    private static AudioClip clipFireBullet;
    private static AudioClip clipTakeDamage;

    public static final int BACKGROUND = 0;
    public static final int JUMP = 1;
    public static final int FIREBULLET = 2;
    public static final int TAKEDAMAGE = 3;

    public static void init()
    {
        try
        {
            clipBackGround = Applet.newAudioClip(Sound.class.getResource("/Resources/sounds/background.wav"));//Sound.class pentru ca metoda statica
            clipJump = Applet.newAudioClip(Sound.class.getResource("/Resources/sounds/jump.wav"));
            clipFireBullet = Applet.newAudioClip(Sound.class.getResource("/Resources/sounds/fireBullet.wav"));
            clipTakeDamage = Applet.newAudioClip(Sound.class.getResource("/Resources/sounds/takeDamage.wav"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void music(int option)
    {
        if (option == BACKGROUND)
        {
            clipBackGround.loop();
        }
        else if (option == JUMP)
        {
            clipJump.play();
        }
        else if (option == FIREBULLET)
        {
            clipFireBullet.play();
        }
        else if (option == TAKEDAMAGE)
        {
            clipTakeDamage.play();
        }
    }
}
