package com.example.mahmoudbahaa.expenses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnlargeImage extends AppCompatActivity {
    @BindView(R.id.EnlargedImage)
    ImageView EnlargedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_image);
        ButterKnife.bind(this);

        String ImagePath = getIntent().getStringExtra("ImagePath");


        Glide.with(getApplicationContext())
                .load(new File(ImagePath)).apply(new RequestOptions().centerCrop()).into(EnlargedImage);



    }
}
