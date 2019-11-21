package com.example.medicheck.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Avisos.db";

    public static final String TABLA_NOMBRES = "avisos";
    public static final String COLUMNA_ID = "avisos_id";
    public static final String COLUMNA_FARMACO = "farmaco";
    public static final String COLUMNA_DIAHORA = "diahora";

    private static final String SQL_CREAR = "create table "
            + TABLA_NOMBRES + "(" + COLUMNA_ID
            + " integer primary key autoincrement, " + COLUMNA_FARMACO
            + " text not null, " + COLUMNA_DIAHORA
            + " text not null);";

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

    public void agregar(String farmaco, String diahora) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMNA_FARMACO, farmaco);
        values.put(COLUMNA_DIAHORA, diahora);
        db.insert(TABLA_NOMBRES, null, values);
        db.close();

    }
}
