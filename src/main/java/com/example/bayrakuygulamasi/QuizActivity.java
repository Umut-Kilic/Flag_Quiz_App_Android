package com.example.bayrakuygulamasi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewDogru,textViewYanlis,textViewSoruSayi,textViewKalanSure;
    private ImageView imageViewBayrak;
    private Button buttonA,buttonB,buttonC,buttonD;
    private ArrayList<Bayraklar> sorular;
    private ArrayList<Bayraklar> yanlisSecenekler;
    private Bayraklar dogruSoru;
    private Veritabani vt;

    //Soru sayaçları
    private int soruSayac = 0 ;
    private int dogruSayac = 0;
    private int yanlisSayac = 0;

    //Zaman işleri
    private CountDownTimer mCountDownTimer;
    private static long START_TIME_MILIS=6600;
    private boolean mTimerRunning=true;
    private long LEFT_TIME_MILIS;

    private ArrayList<Bayraklar> secenekler = new ArrayList<>();
    private HashSet<Bayraklar> secenekleriKaristirmaListe = new HashSet<>();
    
    //Animasyon
    private Animation uptodownquiz,lefttorightquiz,downtoupquiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewDogru = findViewById(R.id.textViewDogru);
        textViewYanlis = findViewById(R.id.textViewYanlis);
        textViewSoruSayi = findViewById(R.id.textViewSoruSayi);
        textViewKalanSure=findViewById(R.id.textViewKalanSure);
        imageViewBayrak = findViewById(R.id.imageViewBayrak);
        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);
        buttonC = findViewById(R.id.buttonC);
        buttonD = findViewById(R.id.buttonD);
        
        uptodownquiz= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodownquiz);
        lefttorightquiz= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.lefttorightquiz);
        downtoupquiz= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.downtoupquiz);

        animasyonBaslat();

        vt = new Veritabani(this);

        sorular = new BayraklarDao().rasgele10getir(vt);

        soruYukle();


            buttonA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dogruKontrol(buttonA,true);
                }
            });

            buttonB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dogruKontrol(buttonB,true);
                }
            });

            buttonC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dogruKontrol(buttonC,true);
                }
            });

            buttonD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dogruKontrol(buttonD,true);
                }
            });


    }

    public void soruYukle(){
        resetButton();
        textViewSoruSayi.setText((soruSayac+1)+". SORU");
        textViewDogru.setText("Doğru : "+(dogruSayac));
        textViewYanlis.setText("Yanlış : "+(yanlisSayac));

        dogruSoru = sorular.get(soruSayac);

        yanlisSecenekler = new BayraklarDao().rasgele3YanlisSecenekGetir(vt,dogruSoru.getBayrak_id());

        imageViewBayrak.setImageResource(getResources().getIdentifier(dogruSoru.getBayrak_resim()
                ,"drawable",getPackageName()));

        //Tüm secenekleri hashset yardımıyla karıştırma
        secenekleriKaristirmaListe.clear();
        secenekleriKaristirmaListe.add(dogruSoru);//Doğru secenek
        secenekleriKaristirmaListe.add(yanlisSecenekler.get(0));
        secenekleriKaristirmaListe.add(yanlisSecenekler.get(1));
        secenekleriKaristirmaListe.add(yanlisSecenekler.get(2));

        //Hashset ile butonlara dinamik şekilde yazı yazdıramadığımızdan arraylist dönüşümü yaptık.
        secenekler.clear();

        for(Bayraklar b: secenekleriKaristirmaListe){
            secenekler.add(b);
        }
        //Secenekleri buttonlara yerleştirdik.
        buttonA.setText(secenekler.get(0).getBayrak_ad());
        buttonB.setText(secenekler.get(1).getBayrak_ad());
        buttonC.setText(secenekler.get(2).getBayrak_ad());
        buttonD.setText(secenekler.get(3).getBayrak_ad());

        if(soruSayac<=9){
            LEFT_TIME_MILIS=START_TIME_MILIS;
            startCountDown();
        }
        else {
            mCountDownTimer.cancel();
        }
    }

    private void startCountDown(){
        mCountDownTimer=new CountDownTimer(LEFT_TIME_MILIS,1000) {
            @Override
            public void onTick(long l) {
                LEFT_TIME_MILIS=l;
                yaziGuncelle();

            }

            @Override
            public void onFinish() {
                LEFT_TIME_MILIS=0;
                yaziGuncelle();

            }
        }.start();

    }
    private void resetTimer(){
        LEFT_TIME_MILIS=START_TIME_MILIS;
        textViewKalanSure.setText("Kalan süre : "+(LEFT_TIME_MILIS/1000));

    }

    private Button secilenButton(){
        View v=findViewById(android.R.id.content).getRootView();
        Button b=new Button(this);
        switch(v.getId()) {
            case R.id.buttonA:
                b=  buttonA;
                break;
            case R.id.buttonB:
                b=    buttonB;
                break;
            case R.id.buttonC:
                b=    buttonC;
            break;
            case R.id.buttonD:
                b=    buttonD;
            break;
            default:
                b=null;
        }
        return b;

    }

    private void buttonGoster(String yazi){
        View v=findViewById(android.R.id.content).getRootView();
        String a=buttonA.getText().toString().trim();
        String b=buttonB.getText().toString().trim();
        String c=buttonC.getText().toString().trim();
        String d=buttonD.getText().toString().trim();
        if(yazi==a){
            buttonB.setText("");
            buttonA.setVisibility(View.VISIBLE);
            buttonB.setVisibility(View.VISIBLE);
            buttonC.setVisibility(View.INVISIBLE);
            buttonD.setVisibility(View.INVISIBLE);
            buttonA.setClickable(false);
            buttonC.setClickable(false);
            buttonD.setClickable(false);
            buttonB.setText("Diğer soruya geçmek için tıkla");
            buttonB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soruSayacKontrol();
                }
            });
        }
        else if(yazi==b){
            buttonC.setText("");
            buttonA.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.VISIBLE);
            buttonC.setVisibility(View.VISIBLE);
            buttonD.setVisibility(View.INVISIBLE);
            buttonA.setClickable(false);
            buttonB.setClickable(false);
            buttonD.setClickable(false);
            buttonC.setText("Diğer soruya geçmek için tıkla");
            buttonC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soruSayacKontrol();
                }
            });
        }
        else if(yazi==c){
            buttonD.setText("");
            buttonA.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.INVISIBLE);
            buttonC.setVisibility(View.VISIBLE);
            buttonD.setVisibility(View.VISIBLE);
            buttonA.setClickable(false);
            buttonB.setClickable(false);
            buttonC.setClickable(false);
            buttonD.setText("Diğer soruya geçmek için tıkla");
            buttonD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soruSayacKontrol();
                }
            });
        }
        else {
            buttonC.setText("");
            buttonA.setVisibility(View.INVISIBLE);
            buttonB.setVisibility(View.INVISIBLE);
            buttonC.setVisibility(View.VISIBLE);
            buttonD.setVisibility(View.VISIBLE);
            buttonA.setClickable(false);
            buttonB.setClickable(false);
            buttonD.setClickable(false);
            buttonC.setText("Diğer soruya geçmek için tıkla");
            buttonC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soruSayacKontrol();
                }
            });
        }
    }
    private void resetButton(){
        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);

        buttonA.setClickable(true);
        buttonB.setClickable(true);
        buttonC.setClickable(true);
        buttonD.setClickable(true);

    }

    private void yaziGuncelle(){
        Button b=secilenButton();
        textViewKalanSure.setText("Kalan süre : "+(LEFT_TIME_MILIS/1000));

        if(LEFT_TIME_MILIS<=150){
            textViewKalanSure.setText("Süreniz dolmuştur");
            textViewKalanSure.setTextColor(Color.RED);
            dogruKontrol(b,false);

        }
        else {
            textViewKalanSure.setTextColor(Color.BLACK);
            if(b!=null){
                dogruKontrol(b,true);
            }
            if(LEFT_TIME_MILIS<3750){
                textViewKalanSure.setTextColor(Color.RED);
            }
        }
    }


    public void dogruKontrol(Button button,boolean b){

        String dogruCevap = dogruSoru.getBayrak_ad().trim();

        if(b){
            String buttonYazi = button.getText().toString().trim();
            Log.e("Bastıgın cevap",buttonYazi);
            Log.e("dogru cevap ",dogruCevap);

            if(buttonYazi.equals(dogruCevap)){
                dogruSayac++;
                textViewDogru.setText("Doğru : "+(dogruSayac));
                textViewYanlis.setText("Yanlış : "+(yanlisSayac));
                soruSayacKontrol();
                mCountDownTimer.cancel();
            }else{
                yanlisSayac++;
                textViewDogru.setText("Doğru : "+(dogruSayac));
                textViewYanlis.setText("Yanlış : "+(yanlisSayac));
                buttonGoster(dogruCevap);
                mCountDownTimer.cancel();
            }

        }
        else {
            yanlisSayac++;
            textViewDogru.setText("Doğru : "+(dogruSayac));
            textViewYanlis.setText("Yanlış : "+(yanlisSayac));
            buttonGoster(dogruCevap);

        }
    }

    public void soruSayacKontrol(){

        soruSayac++;

        //soru sayısı 10 olduysa sonuca git
        if(soruSayac < 10){
            resetTimer();
            mCountDownTimer.cancel();
            soruYukle();

        }else{
            mCountDownTimer.cancel();
            Intent i = new Intent(QuizActivity.this,ResultActivity.class);
            i.putExtra("dogruSayac",dogruSayac);
            startActivity(i);
            finish();
        }
    }
    
    public void animasyonBaslat(){
        textViewKalanSure.setAnimation(uptodownquiz);
        textViewDogru.setAnimation(uptodownquiz);
        textViewYanlis.setAnimation(uptodownquiz);
        textViewSoruSayi.setAnimation(uptodownquiz);
        imageViewBayrak.setAnimation(lefttorightquiz);
        buttonA.setAnimation(lefttorightquiz);
        buttonB.setAnimation(lefttorightquiz);
        buttonC.setAnimation(lefttorightquiz);
        buttonD.setAnimation(lefttorightquiz);
        ;
    }
    public void animasyonSil(){
        View v=findViewById(android.R.id.content).getRootView();
        v.clearAnimation();
    }
}
