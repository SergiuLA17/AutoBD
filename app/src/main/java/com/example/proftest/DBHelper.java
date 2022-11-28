package com.example.proftest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";
    String[] FieldsN;    // fields names
    int numberOfFields;
    String[] FieldsT;
    String DBname;
    String tableN;
    SQLiteDatabase db;
    ArrayList<Tables> list = new ArrayList<>();

    public DBHelper(Context context, String dbname) {
        super(context, dbname, null, 1);
        this.DBname = dbname;
    }

    public DBHelper(Context context, SQLiteDatabase db, String tableN,
                    String[] FieldsN, String[] FieldsT, int nf) {
        super(context, "myDBM", null, 1);
        this.numberOfFields = nf;
        this.FieldsN = FieldsN;
        this.FieldsT = FieldsT;
        this.tableN = tableN;
        this.db = db;
    }

    public ArrayList<Tables> getList() {
        return list;
    }

    public void createT(SQLiteDatabase db, String tableN, String[] FieldsN,
                        String[] FieldsT, int numberOfFields) {
        Tables tables = new Tables(tableN);
        String ss = "create table " + tableN + "(";
        for (int i = 0; i < numberOfFields - 1; i++) {
            ss = ss + FieldsN[i] + "  " + FieldsT[i] + ", ";
        }

        for (int i = 0; i < numberOfFields; i++) {
            tables.addElement(FieldsN[i]);
        }

        ss = ss + FieldsN[numberOfFields - 1] + "  " + FieldsT[numberOfFields - 1];
        ss = ss + ");";
        list.add(tables);
        Log.d("Create Table:", tableN + "  :  " + ss);
        db.execSQL(ss);
        Log.d("a fost creat", "tabelul" + tableN + "  ," + ss);
        System.out.println(list);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
