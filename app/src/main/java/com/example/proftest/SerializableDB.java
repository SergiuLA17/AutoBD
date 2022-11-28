package com.example.proftest;

import android.annotation.SuppressLint;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SerializableDB {
        public void seriTable(ArrayList<Tables> tables)
        {
            try
            {
                @SuppressLint("SdCardPath") FileOutputStream fos = new FileOutputStream("/mnt/sdcard/download/listData.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(tables);
                oos.close();
                fos.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }
