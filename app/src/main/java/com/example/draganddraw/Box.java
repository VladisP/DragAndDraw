package com.example.draganddraw;

import android.graphics.PointF;

class Box {

    private PointF mOrigin;
    private PointF mCurrent;

    Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    Box(PointF origin, PointF current) {
        mOrigin = origin;
        mCurrent = current;
    }

    PointF getCurrent() {
        return mCurrent;
    }

    void setCurrent(PointF current) {
        mCurrent = current;
    }

    PointF getOrigin() {
        return mOrigin;
    }
}
