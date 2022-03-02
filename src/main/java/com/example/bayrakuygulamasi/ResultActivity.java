package com.example.bayrakuygulamasi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private TextView textViewSonuc,textViewYuzdeSonuc;
    private Button buttonTekrar;
    private int dogruSayac;
    private RatingBar ratingBar;

    private Animation uptodownresult,lefttorightresult,downtoupresult;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewSonuc = findViewById(R.id.textViewSonuc);
        textViewYuzdeSonuc = findViewById(R.id.textViewYuzdeSonuc);
        buttonTekrar = findViewById(R.id.buttonTekrar);
        ratingBar=findViewById(R.id.ratingBar);

        uptodownresult= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodownresult);
        lefttorightresult= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.lefttorightresult);
        downtoupresult= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.downtoupresult);

        textViewSonuc.setAnimation(uptodownresult);
        textViewYuzdeSonuc.setAnimation(lefttorightresult);
        ratingBar.setAnimation(lefttorightresult);
        buttonTekrar.setAnimation(downtoupresult);

        dogruSayac = getIntent().getIntExtra("dogruSayac",0);

        if(dogruSayac<5){
            textViewYuzdeSonuc.setTextColor(Color.RED);
        }
        else if(dogruSayac>=5 && dogruSayac<7){
            textViewYuzdeSonuc.setTextColor(R.color.turuncu);
        }
        else if(dogruSayac>=7 && dogruSayac<9){
            textViewYuzdeSonuc.setTextColor(R.color.acik_yesil);
        }
        else {
            textViewYuzdeSonuc.setTextColor(R.color.yesil);
        }

        textViewSonuc.setText(dogruSayac+" DOĞRU "+(10-dogruSayac)+" YANLIŞ");
        textViewYuzdeSonuc.setText("% "+(dogruSayac*10)+" Başarı");

        ratingBar.setRating((float) dogruSayac/2);

        buttonTekrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this, QuizActivity.class));
                finish();
            }
        });
    }
}
