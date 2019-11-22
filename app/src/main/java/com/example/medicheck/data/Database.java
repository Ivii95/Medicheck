package com.example.medicheck.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    private static final String SQL_CREAR = "create table "
            + TABLA_NOMBRE + "(" + COLUMNA_ID
            + " integer primary key autoincrement, " + COLUMNA_FARMACO
            + " text not null, "
            + COLUMNA_ANO + "integer not null,"
            + COLUMNA_MES + "integer not null,"
            + COLUMNA_DIA + " integer not null);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

    public ArrayList<Avisos> obtenerById(int id) {
        ArrayList<Avisos> avisos = new ArrayList<>();
        Avisos aviso = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_FARMACO, COLUMNA_ANO, COLUMNA_MES, COLUMNA_DIA};
        // _id = ?
        Cursor cursor =
                db.query(TABLA_NOMBRE,
                        projection,
                        "",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null,
                        null);


        while (cursor != null) {
            aviso = new Avisos();
            aviso.setId(cursor.getInt(0));
            aviso.setFarmaco(cursor.getString(1));
            aviso.setAñoMesDia(LocalDate.of(cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));
            avisos.add(aviso);
            cursor.moveToNext();
        }
        db.close();
        return avisos;
    }
}
