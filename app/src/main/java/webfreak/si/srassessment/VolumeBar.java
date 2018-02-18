package webfreak.si.srassessment;

/**
 * Created by simon.hocevar on 16.02.2018.
 */

public class VolumeBar
{
    int index;
    int heightStart;
    int heightEnd;
    public VolumeBar(int index, int heightStart, int heightEnd)
    {
        this.heightStart = heightStart;
        this.heightEnd = heightEnd;
        this.index = index;
    }

    public boolean isTouchCoordinateWithinBar(int yCoordinate)
    {
        if (yCoordinate == -1)
        {
            return false;
        }
        int tolerance = Math.abs(heightEnd-heightStart);
        if(heightStart - tolerance < yCoordinate && yCoordinate < heightEnd)
        {
            return true;
        }
        return false;
    }

    public int getStart()
    {
        return  heightStart;
    }
    public int getEnd()
    {
        return  heightEnd;
    }
    public void setStart(int start)
    {
        heightStart = start;
    }
    public void setEnd(int end)
    {
        heightEnd = end;
    }
}
