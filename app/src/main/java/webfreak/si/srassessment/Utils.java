package webfreak.si.srassessment;

/**
 * Created by simon.hocevar on 18.02.2018.
 */

public class Utils
{
    public static int calculateLines(String lines, int max)
    {
        int volume = Integer.parseInt(lines);
        if(volume > max)
        {
            volume = max;
        }
        return volume;
    }
}
