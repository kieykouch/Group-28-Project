package com.group28.cs160.shared;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by eviltwin on 4/28/16.
 */
public class Circle extends View {

    private static final int START_ANGLE_POINT = -95;

    private float angle;
    private int size;
    private int color;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Initial Angle (optional, it can be zero)
        angle = 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int strokeWidth = 40;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);

        RectF rect;
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WATCH)) {
            rect = new RectF(0, 0, size, size);
        } else {
            rect = new RectF(strokeWidth, strokeWidth, size - strokeWidth, size - strokeWidth);
        }

        // We draw one circle underneath to represent the path, and another arc on top that
        // can be drawn by an animator
        paint.setColor(color);
        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        size = widthSize < heightSize ? widthSize : heightSize;

        setMeasuredDimension(size, size);
    }


    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setColor(int c) { color = c; }
}
