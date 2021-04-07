package com.example.vallebarberia.utilidades;

public class Utilidades {

    //constantes campos tabla cortes
    public static final String CUT_TABLE = "cortes";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CUT_TYPE = "cut_type";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_DATE = "date";



    public static final String CREAR_TABLA_CORTES =
            "CREATE TABLE " + CUT_TABLE +
            "(" + FIELD_ID + " Integer primary key autoincrement not null," +
             FIELD_NAME + " TEXT,"
            + FIELD_CUT_TYPE + " TEXT,"
            + FIELD_VALUE + " INTEGER,"
            + FIELD_DATE + " INTEGER)";

}
