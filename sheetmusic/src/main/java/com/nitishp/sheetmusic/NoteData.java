package com.nitishp.sheetmusic;

/**
 * Created by Nitish Paradkar on 11/11/2015.
 */
public class NoteData
{
    public enum NoteValue
    {
        LOWER_B(0),
        LOWER_C(1),
        LOWER_D(2),
        LOWER_E(3),
        LOWER_F(4),
        LOWER_G(5),
        HIGHER_A(6),
        HIGHER_B(7),
        HIGHER_C(8),
        HIGHER_D(9),
        HIGHER_E(10),
        HIGHER_F(11),
        HIGHER_G(12);

        private int value;
        NoteValue(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

    }
    public enum NoteDuration
    {
        SIXTEENTH,
        EIGHTH,
        FOURTH,
        HALF,
        WHOLE
    }


    private NoteValue noteValue;
    private NoteDuration noteDuration;

    public NoteData(NoteValue noteValue, NoteDuration noteDuration)
    {
        this.noteValue = noteValue;
        this.noteDuration = noteDuration;
    }

    public NoteValue getNoteValue()
    {
        return this.noteValue;
    }

    public void setNoteValue(NoteValue in)
    {
        this.noteValue = in;
    }

    public NoteDuration getNoteDuration()
    {
        return this.noteDuration;
    }

    public void setNoteDuration(NoteDuration in)
    {
        this.noteDuration = in;
    }
}
