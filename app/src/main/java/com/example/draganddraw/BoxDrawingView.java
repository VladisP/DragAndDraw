package com.example.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {

    private static final String KEY_PARENT_STATE = "ParentState";
    private static final String KEY_POINTS = "Points";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentBox = null;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    private ArrayList<PointF> getPoints() {
        ArrayList<PointF> points = new ArrayList<>();
        for (Box box : mBoxen) {
            points.add(box.getOrigin());
            points.add(box.getCurrent());
        }

        return points;
    }

    private void setPoints(ArrayList<PointF> points) {
        for (int i = 0; i < points.size(); i += 2) {
            Box box = new Box(points.get(i), points.get(i + 1));
            mBoxen.add(box);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();

        state.putParcelable(KEY_PARENT_STATE, super.onSaveInstanceState());
        state.putParcelableArrayList(KEY_POINTS, getPoints());

        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle savedState = (Bundle) state;
        super.onRestoreInstanceState(savedState.getParcelable(KEY_PARENT_STATE));

        ArrayList<PointF> points = savedState.getParcelableArrayList(KEY_POINTS);
        if (points != null) {
            setPoints(points);
        }
    }
}
