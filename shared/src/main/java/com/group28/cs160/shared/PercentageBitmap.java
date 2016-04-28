package com.group28.cs160.shared;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Range;

/**
 * Created by eviltwin on 4/26/16.
 */
public class PercentageBitmap {
    // Obtain the background image for the specific page
    public static Range<Double> RADIUS = new Range<>(320.0, 360.0);
    public static int BG_SIZE = (int) Math.round(2 * RADIUS.getUpper());

    public static Bitmap getPercentageBitMap(double percentage, int circleHighlightColor, int circleColor, int fill) {
        int padding = 10;
        int[] bg_array = new int[(BG_SIZE + 2 * padding) * (2 * padding + BG_SIZE)];
        for (int i = 0; i < (2 * padding + BG_SIZE); i++) {
            for (int j = 0; j < (2 * padding + BG_SIZE); j++) {
                double dist = distanceFromCenter(i, j);
                if ( RADIUS.contains(dist) ) {
                    if (angleFromCenter(i,j)/360*100 < percentage) {
                        bg_array[i * BG_SIZE + j] = circleHighlightColor;
                    } else {
                        bg_array[i * BG_SIZE + j] = circleColor;
                    }
                } else {
                    bg_array[i*BG_SIZE + j] = fill;
                }
            }
        }
        return Bitmap.createBitmap(bg_array, BG_SIZE, BG_SIZE, Bitmap.Config.RGB_565);
    }

    public static Drawable getPercentageDrawable(double percentage, int circleHighlightColor, int circleColor, int fill) {
        Bitmap bg = getPercentageBitMap(percentage,circleHighlightColor, circleColor, fill);
        return new BitmapDrawable(bg);
    }

    private static double distanceFromCenter(int row, int col) {
        return Math.sqrt(Math.pow(row-BG_SIZE/2, 2) + Math.pow(col-BG_SIZE/2, 2));
    }

    private static double angleFromCenter(int row, int col) {
        double angle = 0;
        if (col - BG_SIZE / 2 == 0) {
            if (row - BG_SIZE / 2 > 0) angle = 180;
            else angle = 0;
        } else {
            angle = Math.toDegrees(Math.atan(((row - BG_SIZE / 2) / (col - BG_SIZE / 2))));
        }
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }
}
