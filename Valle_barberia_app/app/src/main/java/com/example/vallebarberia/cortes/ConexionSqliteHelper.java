package com.example.vallebarberia.cortes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.vallebarberia.utilidades.Utilidades;

public class ConexionSqliteHelper extends SQLiteOpenHelper {


    public ConexionSqliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creación de la base de datos
        db.execSQL(Utilidades.CREAR_TABLA_CORTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE if exists cortes");
        onCreate(db);
    }
}
