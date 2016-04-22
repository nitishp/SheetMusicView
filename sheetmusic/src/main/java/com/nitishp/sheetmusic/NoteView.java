package com.nitishp.sheetmusic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

// This class contains the NoteView to actually display music notes. The positions for this
// note ares set by the parent (which should always be MusicBarView
public class NoteView extends View
{
    private NoteData noteData; // metadata on the note type we want to draw

    private Paint blackNotePaint;
    private Paint blackLinePaint;
    private RectF mOvalRect;

    private final float ROTATE_ANGLE = 0f;
    private final float OVAL_HEIGHT_PERCENTAGE = 0.33f;

    // Used to draw the oval (i.e. used during rotation of the canvas)
    private float mOvalXCenter = 0f;
    private float mOvalYCenter = 0f;

    private float lineStartX = 0f;
    private float lineStartY = 0f;

    // This function reads the attributes passed in through the XML and initializes the internal
    // noteData structure to the correct values
    // This function will throw an exception if its missing the noteValue and noteDuration attributes
    // or if they are incorrect values
    public NoteView(Context context, AttributeSet attributes)
    {
        super(context, attributes);
        TypedArray attrs = context.getTheme().obtainStyledAttributes(attributes, R.styleable.NoteView, 0 ,0);

        try
        {
            // Set up the internal note
            NoteData.NoteValue[] noteValues = NoteData.NoteValue.values();
            NoteData.NoteDuration[] noteDurations = NoteData.NoteDuration.values();

            int noteValueIndex = attrs.getInteger(R.styleable.NoteView_noteValue, -1);
            if((noteValueIndex < 0) || (noteValueIndex >= noteValues.length))
                throw new IllegalArgumentException("Invalid attribute for noteValue");

            int noteDurationIndex = attrs.getInteger(R.styleable.NoteView_noteDuration, -1);
            if((noteDurationIndex < 0) || (noteDurationIndex >= noteDurations.length))
                throw new IllegalArgumentException("Invalid attribute for noteDuration");

            this.noteData = new NoteData(noteValues[noteValueIndex], noteDurations[noteDurationIndex]);
        }
        finally
        {
            attrs.recycle();
        }
        initialize();
    }

    public NoteView(Context context, NoteData noteData)
    {
        super(context);
        this.noteData = noteData;
        initialize();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED)
        {
            height = 0;
        }
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
        {
            width = 0;
        }
        this.setMeasuredDimension(width, height);
    }

    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
    {
        float left = 0;
        float right = width;

        // Calculate the offset needed by the ROTATION ANGLE to keep the entire oval in
        // Do this using parametric equations of a circle assuming the center is (left + right)/2
        // And the radius is also (left + right)/2
        float radius = (left + right) / 2;
        float offsetY = (float) (radius*Math.sin(Math.toRadians(ROTATE_ANGLE)));

        // Set up the rest using the offset -- take care of MARGIN_OF_ERROR for top and bottom?
        float top = (height - offsetY)*(1 - OVAL_HEIGHT_PERCENTAGE);
        float bottom = height - offsetY;
        mOvalXCenter = (right + left) / 2;
        mOvalYCenter = (top + bottom) / 2;
        mOvalRect = new RectF(left, top, right, bottom);

        // Set up the positions for the line
        lineStartX = (float) (((left + right) / 2) + radius * Math.cos(Math.toRadians(ROTATE_ANGLE)));
        lineStartY = (float) (((top + bottom) / 2) - radius * Math.sin(Math.toRadians(ROTATE_ANGLE)));
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        // TODO: Take care of drawing eigths or sixteenths

        // Draw the oval for the note
        canvas.save();
        canvas.rotate(-ROTATE_ANGLE, mOvalXCenter, mOvalYCenter);
        // Draw a stroke oval or a filled oval depending on the noteDuration
        if((noteData.getNoteDuration() == NoteData.NoteDuration.WHOLE)
                || (noteData.getNoteDuration() == NoteData.NoteDuration.HALF))
        {
            // Since the stroke line will be drawn in the middle, this will cause clipping unless the
            // bounds are taken care of
            mOvalRect.bottom -= blackLinePaint.getStrokeWidth() / 2;
            mOvalRect.top += blackLinePaint.getStrokeWidth() / 2;
            mOvalRect.left += blackLinePaint.getStrokeWidth() / 2;
            mOvalRect.right -= blackLinePaint.getStrokeWidth() / 2;
            canvas.drawOval(mOvalRect, blackLinePaint);
        }
        else
        {
            canvas.drawOval(mOvalRect, blackNotePaint);
        }
        canvas.restore();

        // Draw the line for the note (if it is needed)
        if(noteData.getNoteDuration() != NoteData.NoteDuration.WHOLE)
        {
            lineStartX -= blackLinePaint.getStrokeWidth() / 2;
            canvas.drawLine(lineStartX, lineStartY, lineStartX, 0, blackLinePaint);
        }
    }

    // Initialize the paint variables that will be used in onDraw()
    private void initialize()
    {
        blackNotePaint = new Paint();
        blackNotePaint.setColor(Color.BLACK);
        blackNotePaint.setStyle(Paint.Style.FILL);

        blackLinePaint = new Paint();
        blackLinePaint.setColor(Color.BLACK);
        blackLinePaint.setStyle(Paint.Style.STROKE);
        blackLinePaint.setStrokeWidth(12);
        blackLinePaint.setStrokeJoin(Paint.Join.ROUND);
        blackLinePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public NoteData.NoteDuration getNoteDuration()
    {
        return noteData.getNoteDuration();
    }

    public NoteData.NoteValue getNoteValue()
    {
        return noteData.getNoteValue();
    }

    public void setNoteValue(NoteData.NoteValue in)
    {
        noteData.setNoteValue(in);
    }

    public void setNoteDuration(NoteData.NoteDuration in)
    {
        noteData.setNoteDuration(in);
    }
}
