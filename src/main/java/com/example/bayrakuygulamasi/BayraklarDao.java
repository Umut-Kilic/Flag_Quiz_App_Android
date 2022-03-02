package com.example.bayrakuygulamasi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by kasimadalan on 1.05.2018.
 */

public class BayraklarDao {

    public ArrayList<Bayraklar> rasgele10getir(Veritabani vt){
        ArrayList<Bayraklar> bayraklarArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM bayraklar ORDER BY RANDOM()  LIMIT 10",null);

        while (c.moveToNext()){
            Bayraklar b = new Bayraklar(c.getInt(c.getColumnIndex("bayrak_id"))
                    ,c.getString(c.getColumnIndex("bayrak_ad"))
                    ,c.getString(c.getColumnIndex("bayrak_resim")));

            bayraklarArrayList.add(b);
        }

        return bayraklarArrayList;

    }

    public ArrayList<Bayraklar> rasgele3YanlisSecenekGetir(Veritabani vt,int bayrak_id){
        ArrayList<Bayraklar> bayraklarArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM bayraklar WHERE bayrak_id != "+bayrak_id+" ORDER BY RANDOM()  LIMIT 3",null);

        while (c.moveToNext()){
            Bayraklar b = new Bayraklar(c.getInt(c.getColumnIndex("bayrak_id"))
                    ,c.getString(c.getColumnIndex("bayrak_ad"))
                    ,c.getString(c.getColumnIndex("bayrak_resim")));

            bayraklarArrayList.add(b);
        }

        return bayraklarArrayList;

    }


}
