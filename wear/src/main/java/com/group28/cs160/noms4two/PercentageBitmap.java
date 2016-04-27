package com.group28.cs160.noms4two;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by eviltwin on 4/26/16.
 */
public class PercentageBitmap {
    // Obtain the background image for the specific page
    public static int BG_SIZE = 1000;

    public static Drawable getBackgroundForPage(double percentage, int color) {
        int padding = 10;
        int[] bg_array = new int[(BG_SIZE + 2 * padding) * (2 * padding + BG_SIZE)];
        for (int i = 0; i < (2 * padding + BG_SIZE); i++) {
            for (int j = 0; j < (2 * padding + BG_SIZE); j++) {
                double dist = distanceFromCenter(i, j);
                if ( dist > 320 && dist < 360 ) {
                    if (angleFromCenter(i,j)/360*100 < percentage) {
                        bg_array[i * BG_SIZE + j] = color;
                    } else {
                        bg_array[i * BG_SIZE + j] = Color.parseColor("#F2F2F2");
                    }
                } else {
                    bg_array[i*BG_SIZE + j] = Color.parseColor("#FFFFFF");
                }
            }
        }
        Bitmap bg = Bitmap.createBitmap(bg_array, BG_SIZE, BG_SIZE, Bitmap.Config.RGB_565);
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
