package org.resourcecenterint.resourcebiblestudyandroid.widgets;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Ajose Olaolu on 31/10/2017.
 */

public class CustomScaleGestures implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener, View.OnClickListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    boolean inScale;
    Activity mActivity;

    public CustomScaleGestures(Activity activity) {
        gestureScale = new ScaleGestureDetector(activity, this);
        mActivity = activity;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = (scaleFactor < 1 ? 1 : scaleFactor); // prevent our view from becoming too small //
        scaleFactor = ((float) ((int) (scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        inScale = true;
        return true;
    }


    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        inScale = false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

        Toast.makeText(mActivity, "onSingleTapUp", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {

        Toast.makeText(mActivity, "onSingleTapUp", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

        Toast.makeText(mActivity, "onLongPress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() < e2.getX()) {

            Toast.makeText(mActivity, "Left to Right swipe performed", Toast.LENGTH_SHORT).show();
        }

        if (e1.getX() > e2.getX()) {

            Toast.makeText(mActivity, "Right to Left swipe performed", Toast.LENGTH_SHORT).show();

        }

        if (e1.getY() < e2.getY()) {

            Toast.makeText(mActivity, "Up swipe performed", Toast.LENGTH_SHORT).show();
        }

        if (e1.getY() > e2.getY()) {

            Toast.makeText(mActivity, "Down swipe performed", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {

        Toast.makeText(mActivity, "onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {

        Toast.makeText(mActivity, "onDoubleTap", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {

        Toast.makeText(mActivity, "onDoubleTapEvent", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClick(View view) {

    }
}