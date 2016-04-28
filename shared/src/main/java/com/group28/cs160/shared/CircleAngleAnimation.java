package com.group28.cs160.shared;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by eviltwin on 4/28/16.
 * TODO(prad): Credit http://stackoverflow.com/questions/29381474/how-to-draw-a-circle-with-animation-in-android
 */
public class CircleAngleAnimation extends Animation {

    private Circle circle;

    private float oldAngle;
    private float newAngle;

    public CircleAngleAnimation(Circle circle, float newAngle) {
        this.oldAngle = circle.getAngle();
        this.newAngle = newAngle;
        this.circle = circle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);

        circle.setAngle(angle);
        circle.requestLayout();
    }
}
