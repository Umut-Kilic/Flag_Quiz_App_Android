package com.example.bayrakuygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView imageViewMain;
    private TextView textViewMain;
    private Button buttonBasla;

    private Animation uptodownmain,uptodowninfinitemain,downtoupmain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewMain = findViewById(R.id.imageViewMain);
        textViewMain = findViewById(R.id.textViewMain);
        buttonBasla = findViewById(R.id.buttonBasla);

        uptodownmain= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodownmain);
        uptodowninfinitemain= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodowninfinitemain);
        downtoupmain= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.downtoupmain);

        imageViewMain.setAnimation(uptodowninfinitemain);
        textViewMain.setAnimation(uptodownmain);
        buttonBasla.setAnimation(downtoupmain);

        veritabaniKopyala();

        buttonBasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QuizActivity.class));
            }
        });
    }

    public void veritabaniKopyala(){
        DatabaseCopyHelper copyHelper = new DatabaseCopyHelper(this);

        try {
            copyHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        copyHelper.openDataBase();
    }
}
