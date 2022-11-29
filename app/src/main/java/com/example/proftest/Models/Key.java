package com.example.proftest.Models;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

public class Key {
    private String primaryKey;
    private String tablePrimaryKey;
    private String foreignKey;
    private String foreignKeyTable;

    public Key(String primaryKey, String tablePrimaryKey, String foreignKey, String foreignKeyTable) {
        this.primaryKey = primaryKey;
        this.tablePrimaryKey = tablePrimaryKey;
        this.foreignKey = foreignKey;
        this.foreignKeyTable = foreignKeyTable;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTablePrimaryKey() {
        return tablePrimaryKey;
    }

    public void setTablePrimaryKey(String tablePrimaryKey) {
        this.tablePrimaryKey = tablePrimaryKey;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getForeignKeyTable() {
        return foreignKeyTable;
    }

    public void setForeignKeyTable(String foreignKeyTable) {
        this.foreignKeyTable = foreignKeyTable;
    }

    @NotNull
    @Override
    public String toString() {
        return "Key{" +
                "primaryKey='" + primaryKey + '\'' +
                ", tablePrimaryKey='" + tablePrimaryKey + '\'' +
                ", foreignKey='" + foreignKey + '\'' +
                ", foreignKeyTable='" + foreignKeyTable + '\'' +
                '}';
    }
}
