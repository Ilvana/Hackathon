package com.elmana.hackathon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilvana on 10/12/2015.
 */
public class Baza extends SQLiteOpenHelper {

        // All Static variables
        // Database Version
        private static final int DATABASE_VERSION = 1;

        // Database Name
        private static final String DATABASE_NAME = "bazaUredjaja";

        // Contacts table name
        private static final String TABLE_UREDJAJI = "Uredjaji";

        // Contacts Table Columns names
        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "ime";
        private static final String KEY_PH_NO = "adresa";

        public Baza(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_UREDJAJI_TABLE = "CREATE TABLE " + TABLE_UREDJAJI + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_PH_NO + " TEXT" + ")";
            db.execSQL(CREATE_UREDJAJI_TABLE);
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UREDJAJI);

            // Create tables again
            onCreate(db);
        }

    //DODAVANJE UREDJAJA
    public void dodajUredjaj(Uredjaj uredjaj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, uredjaj.getIme_uredjaja()); // Contact Name
        values.put(KEY_PH_NO, uredjaj.getAdresa_uredjaja()); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_UREDJAJI, null, values);
        db.close(); // Closing database connection
    }

    public Uredjaj nadjiUredjaj(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_UREDJAJI, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Uredjaj contact = new Uredjaj(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    public List<Uredjaj> sviUredjaji() {
        List<Uredjaj> listaUredjaja = new ArrayList<Uredjaj>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_UREDJAJI;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Uredjaj uredjaj = new Uredjaj();
                uredjaj.setID(Integer.parseInt(cursor.getString(0)));
                uredjaj.setIme_uredjaja(cursor.getString(1));
                uredjaj.setAdresa_uredjaja(cursor.getString(2));
                // Adding contact to list
                listaUredjaja.add(uredjaj);
            } while (cursor.moveToNext());
        }

        // return contact list
        return listaUredjaja;
    }

    public int brojUredjaja() {
        String countQuery = "SELECT  * FROM " + TABLE_UREDJAJI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void brisanjeUredjaja(Uredjaj uredjaj) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_UREDJAJI, KEY_ID + " = ?",
                new String[]{String.valueOf(uredjaj.getID())});
        db.close();
    }

}
