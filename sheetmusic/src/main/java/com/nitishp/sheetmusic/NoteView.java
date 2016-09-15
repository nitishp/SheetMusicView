package com.nitishp.sheetmusic;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

// This class contains the NoteView to actually display music notes. The positions for this
// note ares set by the parent (which should always be MusicBarView
public class NoteView extends View
{
    private NoteData noteData; // metadata on the note type we want to draw

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

    // Initialize the paint variables that will be used in onDraw()
    private void initialize()
    {
        int backgroundImageId;
        if(noteData.getNoteDuration() == NoteData.NoteDuration.EIGHTH)
        {
            backgroundImageId = R.drawable.eighth_note_stem_facing_up;
        }
        else if(noteData.getNoteDuration() == NoteData.NoteDuration.FOURTH)
        {
            if(noteData.getNoteValue().greaterThanHigherB())
            {
                backgroundImageId = R.drawable.quarter_note_stem_facing_down;
            }
            else
            {
                backgroundImageId = R.drawable.quarter_note_stem_facing_up;
            }
        }
        else if(noteData.getNoteDuration() == NoteData.NoteDuration.HALF)
        {
            if(noteData.getNoteValue().greaterThanHigherB())
            {
                backgroundImageId = R.drawable.half_note_stem_facing_down;
            }
            else
            {
                backgroundImageId = R.drawable.half_note_stem_facing_up;
            }
        }
        else
        {
            // TODO: Throw an exception
            backgroundImageId = R.drawable.eighth_note_stem_facing_up;
        }
        setBackgroundResource(backgroundImageId);

        // Set the background image to the center
        WindowManager.LayoutParams l = new WindowManager.LayoutParams();
        l.gravity = Gravity.CENTER;
        setLayoutParams(l);
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
