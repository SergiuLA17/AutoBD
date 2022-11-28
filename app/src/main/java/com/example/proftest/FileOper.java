package com.example.proftest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileOper {
    String tablesN;
    int numberOfTables;

    public FileOper() {
    }

    public FileOper(String tablesN) {
        this.tablesN = tablesN;
    }

    public FileOper(int n, String tablesN) {
        this.tablesN = tablesN;
        this.numberOfTables = n;
    }

    public String readTable(String tablesN) {
        String aDataRow = "";
        String aBuffer = "";
        final String rez;
        {
            try {

                File myFile = new File("/mnt/sdcard/download/" + tablesN + ".txt");
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }

                myReader.close();
            } catch (Exception e) {

                Log.d("ERROR", "Eloare la citire TablesM");
            }

        }
        rez = aBuffer;
		System.out.println(rez);
        return rez;
    }
}

