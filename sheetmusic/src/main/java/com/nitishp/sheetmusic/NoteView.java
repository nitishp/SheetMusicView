package com.nitishp.sheetmusic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private Paint blackPaint;
    Bitmap bitmap;

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
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap, 0, 0, blackPaint);
    }

    // Initialize the paint variables that will be used in onDraw()
    private void initialize()
    {
        if(noteData.getNoteDuration() == NoteData.NoteDuration.EIGHTH)
        {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eighth_note_stem_facing_up);
        }
        else if(noteData.getNoteDuration() == NoteData.NoteDuration.FOURTH)
        {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.quarter_note_stem_facing_up);
        }
        else if(noteData.getNoteDuration() == NoteData.NoteDuration.HALF)
        {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.half_note_stem_facing_up);
        }
        else
        {
            // TODO: Throw an exception
            bitmap = null;
        }
        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
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
