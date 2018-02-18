package webfreak.si.srassessment;

/**
 * Created by simon.hocevar on 18.02.2018.
 */

public class Utils
{
    public static int calculatePercentage(String percentage, int max)
    {
        int volume = 0;
        int percent = Integer.parseInt(percentage);
        if(percent > 0 && percent < 99)
        {
            volume = (percent * max)/100;
        }
        if(percent == 0)
        {
            volume = 0;
        }
        if(percent >= 100)
        {
            volume= max;
        }
        return  volume;
    }
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
