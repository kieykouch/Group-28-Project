package com.group28.cs160.shared;

/**
 * Created by eviltwin on 4/20/16.
 * Card Fragment for mobile needs to extend v4 Fragment.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CenteredImageFragmentv4 extends Fragment {
    private OnClickListener listener;
    private String description;
    private int iconRes;
    private float angle;
    private float oldAngle;
    private int ringColor, highlightColor;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_centered_image, container, false);

        ImageView item = (ImageView) fragmentView.findViewById(R.id.item);
        item.setImageResource(iconRes);

        TextView text = (TextView) fragmentView.findViewById(R.id.item_text);
        text.setText(description);

        Circle path = (Circle) fragmentView.findViewById(R.id.path);
        path.setAngle(360);
        path.setColor(ringColor);

        Circle circle = (Circle) fragmentView.findViewById(R.id.circle);
        circle.setAngle(oldAngle);
        circle.setColor(highlightColor);

        CircleAngleAnimation animation = new CircleAngleAnimation(circle, angle);
        if (oldAngle != 0) {
            Log.d("CenteredImageFragment", "Old Angle: " + oldAngle);
            animation.setDuration(3000);
        } else {
            animation.setDuration(1000);
        }
        circle.startAnimation(animation);

        if (listener != null) {
            fragmentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View view) {
                        listener.onClick(view);
                }

            });
        }

        return fragmentView;
    }

    public void setAngle(float a) {angle = a;}

    public void setOldAngle(float a) {oldAngle = a;}

    public void setImage(int i) {
        iconRes = i;
    }

    public void setDescription(String d) {
        description = d;
    }

    public void setOnClickListener(final OnClickListener listener) {
        this.listener = listener;
    }

    public void setColor(int rC, int hC) { ringColor = rC; highlightColor = hC; }
}