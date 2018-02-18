package webfreak.si.srassessment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by simon.hocevar on 17.02.2018.
 */

public class VolumeControlView extends View

{
    private int volume;
    private int scale;
    private int double_scale;
    private int color_fg;
    private int color_bg;
    private int volume_percentage=-1;
    private int touch_coordinate_y=-1;
    private Paint rectanglePaintFG;
    private Paint rectanglePaintBG;
    VolumeControlInterface callback;

    private ArrayList<VolumeBar> volumeBars = new ArrayList<>();

    public VolumeControlView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        rectanglePaintFG = new Paint();
        rectanglePaintBG = new Paint();

        callback = (VolumeControlInterface) context;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VolumeControlView);
        try
        {
            scale = typedArray.getInt(R.styleable.VolumeControlView_vcv_scale, 0);
            volume_percentage = typedArray.getInt(R.styleable.VolumeControlView_vcv_volume_percentage, 0);
            volume = (scale * volume_percentage)/100;
            color_fg = typedArray.getInt(R.styleable.VolumeControlView_vcv_color_foreground, 0);
            color_bg = typedArray.getInt(R.styleable.VolumeControlView_vcv_color_background, 0);
            double_scale = scale * 2;
        }
        finally
        {
            typedArray.recycle();
        }
        //Add default bar objects to list
        for(int i=0; i<scale;i++)
        {
            volumeBars.add(new VolumeBar(scale-i,0,0));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewHeight = this.getMeasuredHeight();
        int viewWidth = this.getMeasuredWidth();
        //We need the height of each bar - divide entire height with number of stripes (one colored and one "transparent")
        int scaled = viewHeight / double_scale;
        int index = 0;
        //Update start and end coordinates for each bar object
        for(int i=0; i<double_scale; i++)
        {
            if(i%2==0)
            {
                volumeBars.get(index).setStart(i * scaled);
                volumeBars.get(index).setEnd((i * scaled) + scaled);
                index++;
            }
        }

        rectanglePaintFG.setStyle(Paint.Style.FILL);
        rectanglePaintFG.setAntiAlias(true);
        rectanglePaintFG.setColor(color_fg);

        rectanglePaintBG.setStyle(Paint.Style.FILL);
        rectanglePaintBG.setAntiAlias(true);
        rectanglePaintBG.setColor(color_bg);

        boolean fillVolume = false;
        //Iterate over volume bars, set "active" color for every bar after the bar that was pressed
        for(int i=0; i<volumeBars.size(); i++)
        {
            VolumeBar vb = volumeBars.get(i);
            //Set active bars if current bar was clicked, or the bar index is the same as current volume or if we must color all the bars from now on (fillVolume)
            if(vb.isTouchCoordinateWithinBar(touch_coordinate_y) || (vb.index == volume && touch_coordinate_y ==-1) || fillVolume)
            {
                //If this is the first "active" bar (fillVolume flag has not been set yet)
                if(!fillVolume)
                {
                    //Calculate percentage for interface
                    volume_percentage = (vb.index * 100) / volumeBars.size();
                }
                fillVolume = true;
                canvas.drawRect(0, vb.getStart(), viewWidth, vb.getEnd(), rectanglePaintFG);
            }
            else
            {
                canvas.drawRect(0, vb.getStart(), viewWidth, vb.getEnd(), rectanglePaintBG);
                volume_percentage = 0;
            }
        }
        // On button click we still have touch coordinate set - that's why we set it to -1 (we get a new value as soon as new coordinate is clicked)
        if(touch_coordinate_y!=-1)
        {
            touch_coordinate_y =-1;
        }
        //Set callback for percentage
        callback.onVolumeChanged(volume_percentage);
    }

    //Getters
    public int getVolumeControlVolume()
    {
        return volume_percentage;
    }
    public int getVolumeControlScale()
    {
        return scale;
    }
    public int getVolumeControlColorFG()
    {
        return color_fg;
    }
    public int getVolumeControlColorBG()
    {
        return color_bg;
    }

    //Setters
    public void setVolumeControlLines(int lines)
    {
        this.volume_percentage = (lines * 100)/volumeBars.size();
        this.volume = lines;
        invalidate();
        requestLayout();
    }
    public void setVolumeControlVolume(@IntRange(from=0,to=100)int volume)
    {
        this.volume = (volumeBars.size() * volume)/100;
        this.volume_percentage = volume;
        invalidate();
        requestLayout();
    }
    public void setVolumeControlScale(int scale)
    {
        this.scale = scale;
        this.double_scale = scale * 2;
        this.volume = (scale * volume_percentage)/100;
        //Add default bar objects to list
        volumeBars.clear();
        for(int i=0; i<scale;i++)
        {
            volumeBars.add(new VolumeBar(scale-i,0,0));
        }
        invalidate();
        requestLayout();
    }
    public void setVolumeControlColorFG(int color)
    {
        this.color_fg = color;
        invalidate();
        requestLayout();
    }
    public void setVolumeControlColorBG(int color)
    {
        this.color_bg = color;
        invalidate();
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.performClick();
        touch_coordinate_y = (int)motionEvent.getY();
        invalidate();
        requestLayout();
        return super.onTouchEvent(motionEvent);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
