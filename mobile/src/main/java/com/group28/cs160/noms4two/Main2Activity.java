package com.group28.cs160.noms4two;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import java.io.ByteArrayInputStream;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageButton imageButton = (ImageButton) findViewById(R.id.img);
        byte[] arr = getIntent().getByteArrayExtra("arr");
        Bitmap map = BitmapFactory.decodeStream(new ByteArrayInputStream(arr));
        imageButton.setBackground(new BitmapDrawable(map));
    }
}
