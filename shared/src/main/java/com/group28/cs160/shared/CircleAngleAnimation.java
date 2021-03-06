package com.group28.cs160.shared;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by eviltwin on 4/28/16.
 */
public class CircleAngleAnimation extends Animation {

    private Circle circle;

    private float oldAngle;
    private float newAngle;

    public CircleAngleAnimation(Circle circle, float newAngle) {
        this.oldAngle = circle.getAngle();
        // New angle cannot be less than 10.
        this.newAngle = newAngle > 10 ? newAngle : 10;
        this.circle = circle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);

        circle.setAngle(angle);
        circle.requestLayout();
    }
}
