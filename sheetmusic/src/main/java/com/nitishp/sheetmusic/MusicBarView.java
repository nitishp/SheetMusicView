package com.nitishp.sheetmusic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

// This class represents the music bar of the sheet view
// It contains a list of notes to display
// It also has a list of child NoteView's which for which it sets the positions
public class MusicBarView extends ViewGroup
{
    private final int NUM_POSSIBLE_BLACK_AREA = 7;
    private final int NUM_DEFINITE_BLACK_AREA = 5;
    private final int NUM_WHITE_AREA = 6;
    private final float PERCENT_HEIGHT_BLACK_AREA = 0.01f;
    private final float PERCENT_HEIGHT_WHITE_AREA = (1 - (PERCENT_HEIGHT_BLACK_AREA*NUM_POSSIBLE_BLACK_AREA))/(NUM_WHITE_AREA);
    private final int MAX_NUM_NOTES = 16;
    private final int NUM_WHITE_SPACES_LINE = 3; // number of white areas the child NoteView wants to cover

    private List<NoteData> notes;
    private float width, height, xTopLeft;
    private List<Float> linePositions;

    private Paint musicBarBlack;
    private Context mContext;

    public MusicBarView(Context context)
    {
        super(context);
        initialize(context);
    }

    public MusicBarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize(context);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        // Draw all the black areas
        // TODO: FIX THIS BACK TO NUM_DEFINITE_BLACK_AREA
        for (int i = 0; i < NUM_POSSIBLE_BLACK_AREA; ++i)
        {
            canvas.drawRect(xTopLeft, linePositions.get(i), xTopLeft + width,
                    linePositions.get(i) + (PERCENT_HEIGHT_BLACK_AREA * height), musicBarBlack);
        }
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        // TODO: Take advantage of changed????

        // Recompute all the dimensions and locations of the black lines
        setup();
        int debug = getPaddingTop();
        int debug2 = getPaddingLeft();
        this.width = right - left - getPaddingRight() - getPaddingLeft();
        this.height = bottom - top - getPaddingBottom() - getPaddingTop();
        this.xTopLeft = getPaddingLeft();

        float blackLineHeight = PERCENT_HEIGHT_BLACK_AREA * this.height;
        float whiteAreaHeight = PERCENT_HEIGHT_WHITE_AREA * this.height;

        float startVal = 0;
        for(int i = 0; i < NUM_POSSIBLE_BLACK_AREA; ++i)
        {
            linePositions.add(startVal);
            startVal += blackLineHeight + whiteAreaHeight;
        }

        // Don't bother setting up child widths if there are no children
        if(getChildCount() == 0)
            return;

        // Change the positions of the children
        int itemWidth = (int) (this.width / getChildCount());
        for(int i = 0; i < getChildCount(); ++i)
        {
            View v = getChildAt(i);
            // get the bottom value of the NoteView based on the NoteValue in the list of notes
            float incrementValue = 0.5f*(blackLineHeight + whiteAreaHeight);
            // most possible bottom value for the note is this.height - blackLineHeight (because the lowest note is LOWER_B)

            int noteBottom = (int) (this.height - blackLineHeight - (notes.get(i).getNoteValue().getValue() * incrementValue));
            int leftStartVal = (i * itemWidth) + getPaddingLeft();
            v.layout(leftStartVal, noteBottom - v.getMeasuredHeight(), leftStartVal + v.getMeasuredWidth(), noteBottom);
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childWidth = getMeasuredWidth() / MAX_NUM_NOTES;
        int wSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeight = (int) (getMeasuredHeight() * PERCENT_HEIGHT_WHITE_AREA * NUM_WHITE_SPACES_LINE);
        int hSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);

        // Set the height and width for every child
        for (int i = 0; i < getChildCount(); ++i)
        {
            getChildAt(i).measure(wSpec, hSpec);
        }
    }

    // TODO: Take care of this function to make adding a note easier (add a child node)
    public void addNote(NoteData note)
    {
        notes.add(note);
        addView(new NoteView(mContext, note));
    }

    // Set up the parameters to initialize all paint objects
    private void initialize(Context context)
    {
        mContext = context;
        width = height = xTopLeft = 0;
        notes = new ArrayList<>();
        musicBarBlack = new Paint();
        musicBarBlack.setColor(Color.BLACK);
        musicBarBlack.setStyle(Paint.Style.FILL);
        linePositions = new ArrayList<>();
        setWillNotDraw(false); // Make sure that onDraw is called for a viewGroup
    }

    // Set up the notes list to make sure it matches with the child views of this MusicBarView
    // Check that each child view is a NoteView
    private void setup()
    {
        linePositions.clear(); // Need to do this to prevent a bunch of linePosition

        for(int i = 0; i < getChildCount(); ++i)
        {
            NoteView noteView;
            try
            {
                noteView = (NoteView) getChildAt(i);
            }
            catch (ClassCastException e)
            {
                throw new ClassCastException("MusicBarView can only have children of type NoteView");
            }

            // Get the type attributes and initialize a new NoteData and modify the element in the list as needed
            if(i < notes.size())
            {
                notes.get(i).setNoteValue(noteView.getNoteValue());
                notes.get(i).setNoteDuration(noteView.getNoteDuration());
            }
            else
            {
                NoteData noteData = new NoteData(noteView.getNoteValue(), noteView.getNoteDuration());
                notes.add(noteData);
            }
        }
    }

}
