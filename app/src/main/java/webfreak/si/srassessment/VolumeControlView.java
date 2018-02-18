package webfreak.si.srassessment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
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
        int original_percentage = volume_percentage;
        //Iterate over volume bars, set "active" color for every bar after the bar that was pressed
        for(int i=0; i<volumeBars.size(); i++)
        {
            VolumeBar vb = volumeBars.get(i);
            //Calculated volume matches the index of current bar -> start coloring active bars
            if((vb.index == volume && touch_coordinate_y ==-1))
            {
                //Calculate percentage for interface
                volume_percentage = original_percentage;
                fillVolume = true;
                canvas.drawRect(0, vb.getStart(), viewWidth, vb.getEnd(), rectanglePaintFG);
            }
            //Currently touched position matches specific volume bar -> start coloring active bars
            else if(vb.isTouchCoordinateWithinBar(touch_coordinate_y))
            {
                //Calculate percentage from click
                volume_percentage = (vb.index * 100) / volumeBars.size();
                original_percentage = volume_percentage;
                fillVolume = true;
                canvas.drawRect(0, vb.getStart(), viewWidth, vb.getEnd(), rectanglePaintFG);
            }
            //If fill Volume is true, we only need to finish coloring active bars till the end
            else if(fillVolume)
            {
                canvas.drawRect(0, vb.getStart(), viewWidth, vb.getEnd(), rectanglePaintFG);
            }
            //Just set the correct percentage - volume is less than 1 bar
            else if(volume == 0)
            {
                volume_percentage = original_percentage;
                canvas.drawRect(0, vb.getStart(), viewWidth, vb.getEnd(), rectanglePaintBG);

            }
            //Draw normal "non-active" bar
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
    public void setVolumeControlVolume(int volume)
    {
        if(volume >100)
        {
            volume = 100;
        }
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
