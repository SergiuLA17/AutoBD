package com.example.proftest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CreateQueryService {
    ReadFileFieldUI readFileFieldUI = new ReadFileFieldUI();
    DeserializableDB deserializableDB = new DeserializableDB();
    ArrayList<Tables> tables = deserializableDB.des();
    //keyle prezente in baza de date
    ArrayList<Key> keyList = new ArrayList<>();
    //fieldurile cdrute e utilizator
    LinkedHashMap<String, List<String>> fieldUIMap = new LinkedHashMap<>();
    //perechile de chei
    LinkedHashMap<String, String> keys = new LinkedHashMap<>();
    //subdenumirile tabelelor
    HashMap<String, String> subnameTables = new HashMap<>();
    //lista de tabele pentru join
    List<String> listOfFieldsUI = new ArrayList<>();
    //lista de tabele pentru select
    List<String> listOfTableUI = new ArrayList<>();
    //filed-table map
    HashMap<String, String> fieldTable = new HashMap<>();
    String selectTable;

    String foreignKey;
    String foreignKeyTableName;

    StringBuilder selectGenerate = new StringBuilder().append("select ");
    StringBuilder joingenerate = new StringBuilder();

    public void createFieldTableHashMap() {
        for (Tables table : tables) {
            for (int i = 0; i < table.getListOfTableFields().size(); i++) {
                fieldTable.put(table.getListOfTableFields().get(i), table.getName());
            }
        }
    }

    //cream keyList pentru baza incarcata
    public void createKeys() {
        boolean hasForeignKey = false;
        for (Tables table : tables) {
            for (int j = 0; j < table.getListOfTableFields().size(); j++) {
                String tableName = table.getName();
                String fieldName = table.getListOfTableFields().get(j);
                String id = fieldName.substring(0, 2);
                if (!id.equals("ID")) {
                    hasForeignKey = findKeys(fieldName, tableName);
                    if (hasForeignKey) {
                        keyList.add(new Key(table.getListOfTableFields().get(j), tableName, foreignKey, foreignKeyTableName));
                    }
                }
            }
        }
    }


    //extragem keyurile din baza de date
    public boolean findKeys(String field, String tableNotSearch) {
        String fieldName = field.replace("ID", "");
        for (Tables table : tables) {
            if (!table.getName().equals(tableNotSearch)) {
                for (int j = 0; j < table.getListOfTableFields().size(); j++) {
                    if (fieldName.equals(table.getListOfTableFields().get(j).replace("ID", ""))) {
                        foreignKey = table.getListOfTableFields().get(j);
                        foreignKeyTableName = table.getName();
                        return true;
                    }
                }
            }

        }
        return false;
    }

    //extragem lista de campuri dorete de utilizator

    public void createtSubnames() {
        subnameTables.put(selectTable, selectTable.toLowerCase().substring(0, 4));
        for (int i = 0; i < tables.size(); i++) {
            subnameTables.put(tables.get(i).getName(), tables.get(i).getName().substring(0, 4).toLowerCase());
        }
    }

    public void createHashMapKey() {
        StringBuilder key = new StringBuilder();
        for (Key value : keyList) {
            key.setLength(0);
            key.append(subnameTables.get(value.getForeignKeyTable())).append(".").append(value.getForeignKey()).append("=").append(subnameTables.get(value.getTablePrimaryKey())).append(".").append(value.getPrimaryKey());
            keys.put(value.getTablePrimaryKey(), key.toString());
        }
    }

    //creem query-ul
    public void generateQuery() {
        selectGenerate.setLength(0);
        joingenerate.setLength(0);
        selectGenerate.append("select ");
        createFieldTableHashMap();
        createtSubnames();
        createHashMapKey();
//

        for (int i = 0; i < fieldUIMap.size(); i++) {
            for (int j = 0; j < fieldUIMap.get(listOfTableUI.get(i)).size(); j++) {
                String subname = subnameTables.get(listOfTableUI.get(i));
                String field = fieldUIMap.get(listOfTableUI.get(i)).get(j);
                selectGenerate.append(subname).append(".").append(field).append(" as ").append(field.toLowerCase()).append(", ");
            }
        }
        selectGenerate.delete(selectGenerate.length() - 2, selectGenerate.length());
        selectGenerate.append(" from ").append(selectTable).append(" as ").append(subnameTables.get(selectTable)).append(" ");


        //create join

        for (int i = 1; i < listOfTableUI.size(); i++) {
            String tableName = listOfTableUI.get(i);
            String subName = subnameTables.get(tableName);
            String pairOfKey = keys.get(tableName);

            joingenerate.append(" inner join ").append(tableName).append(" as ").append(subName).append(" on ").append(pairOfKey).append(" ");

        }

    }

    public String createQuery() throws IOException {
        //citim fisierul unde sunt indicate campurile dorite de utilizator sa fie afisate
        readFileFieldUI.fieldUIProcess();
        fieldUIMap = readFileFieldUI.getFieldUI();
        selectTable = readFileFieldUI.getSelectTable();
        listOfFieldsUI = readFileFieldUI.getListFieldsForJoin();
        listOfTableUI = readFileFieldUI.getListTableNamesSelect();

        System.out.println("Select table: " + selectTable);
        System.out.println("Fields for select: " + fieldUIMap);
        System.out.println("Fields for join: " + listOfFieldsUI);
        System.out.println("Tables for select: " + listOfTableUI);

        createKeys();
        generateQuery();


        return selectGenerate.toString() + joingenerate.toString();

    }
}
