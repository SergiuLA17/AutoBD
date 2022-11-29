package com.example.proftest.Services;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ReadFileFieldUI {
    private String selectTable;
    private List<String> listFields= new ArrayList<>();
    private List<String> listTable = new ArrayList<>();
    private LinkedHashMap<String, List<String>> fieldUI = new LinkedHashMap<>();

    public void fieldUIProcess() {
        try (@SuppressLint("SdCardPath") BufferedReader br = new BufferedReader(new FileReader("/mnt/sdcard/download/FieldsUI.txt"))) {
            String strCurrentLine;
            StringBuilder tableName = new StringBuilder();
            StringBuilder fieldName = new StringBuilder();
            boolean isField = false;
            int i = 0;
            List<String> tmpList= new ArrayList<>();
            //citim fiecare linie din fisier
            while ((strCurrentLine = br.readLine()) != null) {
                for (char c:strCurrentLine.toCharArray()) {
                    if(c == ':') {
                        isField = true;
                        continue;
                    }
                    if(isField) {
                        if(c == ',') {
                            tmpList.add(fieldName.toString().replace(",",""));
                            listFields.add(fieldName.toString().replace(",",""));
                            fieldName.setLength(0);
                        }
                        fieldName.append(c);
                    } else {
                        tableName.append(c);
                    }

                }
                if(i == 0) {
                    selectTable = tableName.toString();
                }
                isField = false;
                tmpList.add(fieldName.toString().replace(",",""));
                listFields.add(fieldName.toString().replace(",",""));
                fieldName.setLength(0);
                fieldUI.put(tableName.toString(), new ArrayList<>(tmpList));
                listTable.add(tableName.toString());
                tableName.setLength(0);
                tmpList.clear();

                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSelectTable() {
        return selectTable;
    }

    public List<String> getListFieldsForJoin() {
        return listFields;
    }

    public List<String> getListTableNamesSelect() {
        return listTable;
    }

    public LinkedHashMap<String, List<String>> getFieldUI() {
        return fieldUI;
    }
}
