package com.example.medicheck.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "avisos.db";

    private static final String TABLA_NOMBRE = "avisos";
    private static final String COLUMNA_ID = "uid";
    private static final String COLUMNA_FARMACO = "farmaco";
    private static final String COLUMNA_ANO = "year";
    private static final String COLUMNA_MES = "mes";
    private static final String COLUMNA_DIA = "dia";


    private String SQL_CREAR_SIMPLE = "create table " + TABLA_NOMBRE + " ( " + COLUMNA_ID + " integer primary key autoincrement, "
            + COLUMNA_FARMACO + " text not null,"
            + COLUMNA_ANO + " integer not null ,"
            + COLUMNA_MES + " integer not null ,"
            + COLUMNA_DIA + " integer not null )";

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= 28) {
            db.disableWriteAheadLogging();
            db.execSQL("PRAGMA foreign_keys=FALSE");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR_SIMPLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_NOMBRE);
        onCreate(db);
    }

    public void updateDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_NOMBRE);
        onCreate(db);
        db.close();
    }

    /*public void agregar(Avisos aviso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMNA_ID, 2);
        values.put(COLUMNA_FARMACO, aviso.farmaco);
        //values.put(COLUMNA_ANO, aviso.añoMesDia.getYear());
        //values.put(COLUMNA_MES, aviso.añoMesDia.getMonthValue());
        //values.put(COLUMNA_DIA, aviso.añoMesDia.getDayOfMonth());
        db.insert(TABLA_NOMBRE, null, values);
    }*/
    public void agregar(Avisos aviso) {
        SQLiteDatabase db = this.getWritableDatabase();
        int ano = aviso.añoMesDia.getYear();
        int mes = aviso.añoMesDia.getMonthValue();
        int dia = aviso.añoMesDia.getDayOfMonth();
        db.execSQL("INSERT INTO " + TABLA_NOMBRE + "(" +
                COLUMNA_ID + "," +
                COLUMNA_FARMACO + "," +
                COLUMNA_ANO + "," +
                COLUMNA_MES + "," +
                COLUMNA_DIA + ")"
                + "VALUES (NULL,'" + aviso.getFarmaco() + "', " + ano + "," + mes + "," + dia + ")");
        //db.close();
    }

    public ArrayList<Avisos> obtener() {

        ArrayList<Avisos> avisos = new ArrayList<>();
        try {
            Avisos aviso = null;
            SQLiteDatabase db = this.getReadableDatabase();
            String[] projection = {COLUMNA_ID, COLUMNA_FARMACO}; //, COLUMNA_ANO, COLUMNA_MES, COLUMNA_DIA};
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
        } catch (Exception e) {

        }
        return avisos;
    }

    public boolean eliminar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLA_NOMBRE,
                    " " + COLUMNA_ID + " = ?",
                    new String[]{String.valueOf(id)});
            db.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }
}
