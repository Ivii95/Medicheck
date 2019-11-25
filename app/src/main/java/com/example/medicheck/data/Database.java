package com.example.medicheck.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Avisos.db";

    private static final String TABLA_NOMBRE = "avisos";
    private static final String COLUMNA_ID = "avisos_id";
    private static final String COLUMNA_FARMACO = "farmaco";
    private static final String COLUMNA_ANO = "ano";
    private static final String COLUMNA_MES = "mes";
    private static final String COLUMNA_DIA = "dia";


    private static final String SQL_CREAR = "CREATE TABLE "
            + TABLA_NOMBRE + " ( " + COLUMNA_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_FARMACO + " TEXT NOT NULL, "
            + COLUMNA_ANO + "INTEGER NOT NULL,"
            + COLUMNA_MES + "INTEGER NOT NULL,"
            + COLUMNA_DIA + " INTEGER NOT NULL)";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_NOMBRE);
        db.execSQL(SQL_CREAR);
    }

    public void agregar(Avisos aviso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMNA_FARMACO, aviso.farmaco);
        values.put(COLUMNA_ANO, aviso.añoMesDia.getYear());
        values.put(COLUMNA_MES, aviso.añoMesDia.getMonthValue());
        values.put(COLUMNA_DIA, aviso.añoMesDia.getDayOfMonth());
        db.insert(TABLA_NOMBRE, null, values);
        db.close();
    }

    public ArrayList<Avisos> obtener() {
        ArrayList<Avisos> avisos = new ArrayList<>();
        Avisos aviso = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_FARMACO, COLUMNA_ANO, COLUMNA_MES, COLUMNA_DIA};
        // _id = ?    String.valueOf(id)
        Cursor cursor =
                db.query(TABLA_NOMBRE,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
        //Cursor cursor = db.rawQuery("SELECT * FROM avisos", null);


        if (cursor != null) {
            cursor.moveToFirst();
            do {
                aviso = new Avisos();
                aviso.setId(cursor.getInt(cursor.getColumnIndex(COLUMNA_ID)));
                aviso.setFarmaco(cursor.getString(cursor.getColumnIndex(COLUMNA_FARMACO)));
                aviso.setAñoMesDia(LocalDate.of(cursor.getInt(cursor.getColumnIndex(COLUMNA_ANO)),
                        cursor.getInt(cursor.getColumnIndex(COLUMNA_MES)), cursor.getInt(cursor.getColumnIndex(COLUMNA_DIA))));
                avisos.add(aviso);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return avisos;
    }

    public boolean eliminar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLA_NOMBRE,
                    " _id = ?",
                    new String[]{String.valueOf(id)});
            db.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }
}
