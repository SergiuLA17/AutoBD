package com.example.proftest;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;


public class Tables implements Serializable {
    private String name;
    private ArrayList<String> listOfTableFields = new ArrayList<>();

    public Tables(String name) {
        this.name = name;
    }

    public void addElement(String fieldName){
        this.listOfTableFields.add(fieldName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getListOfTableFields() {
        return listOfTableFields;
    }

    @NotNull
    @Override
    public String toString() {
        return "Tables{" +
                "name='" + name + '\'' +
                ", list=" + listOfTableFields +
                '}';
    }
}
