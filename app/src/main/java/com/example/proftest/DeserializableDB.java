package com.example.proftest;

import android.annotation.SuppressLint;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class DeserializableDB {

    public ArrayList<Tables> des(){
        ArrayList<Tables> namesList = new ArrayList<>();

        try
        {
            @SuppressLint("SdCardPath") FileInputStream fis = new FileInputStream("/mnt/sdcard/download/listData.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);

            namesList = (ArrayList<Tables>) ois.readObject();
            System.out.println(namesList);

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return namesList;
    }
}
