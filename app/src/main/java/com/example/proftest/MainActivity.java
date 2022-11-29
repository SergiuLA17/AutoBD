package com.example.proftest;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.proftest.Models.Tables;
import com.example.proftest.SaveData.DeserializableDB;
import com.example.proftest.SaveData.SerializableDB;
import com.example.proftest.Services.CreateQuery;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


@SuppressLint("SdCardPath")
public class MainActivity extends Activity {
    String DBName = "CatPrSales5T76";
    CreateQuery test = new CreateQuery();
    ArrayList<Tables> tables = new ArrayList<>();
    ArrayList<Tables> tablesForSerializable = new ArrayList<>();
    public DecimalFormat df = new DecimalFormat("#.##");
    EditText et1, et2, et3;
    EditText txtData;
    FileOper tabel;
    SQLiteDatabase db;
    TextView tv;
    DBHelper dbHelper;
    String[][] cc = new String[20][20];
    String[][] cc1 = new String[20][20];
    String[] tabNames = new String[20];
    String tabStruct, tabContent;
    int numberOfTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtData = findViewById(R.id.txtData);
        txtData.setHint("Enter some lines of data here...");
        tv = findViewById(R.id.textView1);
        et1 = findViewById(R.id.editText1);
        et2 = findViewById(R.id.editText2);
        et3 = findViewById(R.id.editText3);
        tv.setMovementMethod(new ScrollingMovementMethod());
        String whereC = "  NF > ?  OR PF=? ";
        whereC = "  age > ? ";
        String whereV = " 0";
        et1.setText(whereC);
        et2.setText("Editable Values for Restriction(s):  ");
        et3.setText(whereV);
        cc[1][1] = "11";
        cc[1][2] = "12";
        cc1 = cc;
        Log.d(" cc=cc1", "cc[1][1]=  " + cc1[1][1] + "cc[1][2]=  " + cc1[1][2]);


    }


    public void createDB(View v) {
        dbHelper = new DBHelper(this, DBName);
        db = dbHelper.getWritableDatabase();
        Log.d("Create DB=", "The DB " + DBName + "  was created OR Opened the exiting one!");
    }

    public void createTAB(View v) {
        String aDataRow = "";
        String aBuffer = "";
        String tt;
        int nf;
        String[] fieldsN = new String[10];
        String[] fieldsT = new String[10];
        tv.setText("before The     was created   \n");

        tabel = new FileOper();
        String tabN = "TablesM";
        Log.d("Read tabN=", tabN);
        aBuffer = tabel.readTable(tabN);
        tt = aBuffer;
        aBuffer = "";
        String[] tn = tt.split("\n");
        int nt = tn.length;
        numberOfTables = nt;
        Log.d("", "N=" + String.valueOf(nt) + "  Nume tabel=" + tn[0] + ",  Nume tabel2=" + tn[1]);
        System.arraycopy(tn, 0, tabNames, 0, nt);
        for (String s : tn) {
            boolean te = exists(s);
            {
                tabStruct = tabel.readTable(s + "s");
                tabContent = tabel.readTable(s);
                String[] tfS = tabStruct.split("\n");// structura
                String[] tfC = tabContent.split("\n");//  Content
                nf = tfS.length;
                for (int j = 0; j < nf; j++) {
                    String[] fields = tfS[j].split("\t");
                    fieldsN[j] = fields[0];
                    fieldsT[j] = fields[1];
                }
                Log.d("Tabelul : ", s);
                Log.d("Fields", fieldsN[0] + " , " + fieldsT[0] + " , " + fieldsN[1] + " , " + fieldsT[1]);

                // create table i
                tv.setText("before The  " + s + "   was created   \n");
                boolean tableExists = false;

                Log.d("before try Table:", s + "   to create???   ");
                try {
                    System.out.println("s" + s + " " + "field: " + Arrays.toString(fieldsN) + " fieldsT: " + Arrays.toString(fieldsT) + " nf:" + nf);
                    // creating a table
                    dbHelper.createT(db, s, fieldsN, fieldsT, nf);
                    tableExists = true;

                    Log.d("Table:", "The  " + s + "   was created   ");
                    tv.setText("The  " + s + "   was created   \n");
                } catch (Exception e) {
                    // /* fail */
                    Log.d("Table:", "The table " + s + "  was existing, and was not created again   ");
                    tv.setText("The table " + s + "  was existing, and was not created again   ");

                }
            }

        }
        tablesForSerializable = dbHelper.list;
        SerializableDB serializableDB = new SerializableDB();
        serializableDB.seriTable(tablesForSerializable);
        tv.setText("Data was serializable");


    }

    public boolean exists(String table) {
        Cursor c = null;
        boolean tableExists = false;
        try {
            c = db.query(table, null,
                    null, null, null, null, null);
            tableExists = true;
            Log.d("About existing ", "The table " + table + "  exists! :))))");
        } catch (Exception e) {
            /* fail */
            Log.d("The table is missing", table + " doesn't exist :(((");
        }
        return tableExists;
    }

    @SuppressLint("SetTextI18n")
    public void fillTAB(View v) {
        String tabN;
        for (int i = 0; i < numberOfTables; i++) {
            tabN = tabNames[i];
            boolean te = exists(tabN);

            Log.d("Before if", tabN);
            if (te) {  //if the tb exists then fill it
                Log.d("Inside  if", "The table:  " + tabN + "   Exists");
                //the table exests:  	//clear the table
                db.delete(tabN, null, null);
                Log.d("After delete", tabN);
                //fill the table
                String tabContent;
                int nf;
                tabel = new FileOper();
                tabContent = tabel.readTable(tabN);
                Log.d("After table content", tabN);
                String[] tfC = tabContent.split("\n");//  Content
                int nr = tfC.length;
                String[] fieldsN = tfC[0].split("\t");
                String[] fieldsT = tfC[1].split("\t");
                int nft = fieldsT.length;
                nf = fieldsN.length;
                // insert rows
                ContentValues cv = new ContentValues();
                int sw;
                double nnf;
                for (int j = 2; j < nr; j++) {
                    cv.clear();
                    if (tabN.equals("detbord")) {
                        String[] rcd = tfC[j].split("\t");
                        nnf = Float.parseFloat(rcd[2]) * 0.15 + Float.parseFloat(rcd[3]) * 0.15
                                + Float.parseFloat(rcd[4]) * 0.1 + Float.parseFloat(rcd[5]) * 0.2
                                + Float.parseFloat(rcd[6]) * 0.4;
                        tfC[j] = tfC[j] + "\t" + df.format(nnf);
                    }


                    String[] rcd = tfC[j].split("\t");
                    if (tabN.equals("SalesM"))
                        tv.setText("tfC[0]=" + tfC[0] + "\n" + "tfC[1]=" + tfC[1] + "\n" + "nf=" + nf + "nft=" + nft + "\n");
                    for (int k = 0; k < nf; k++) {
                        sw = Integer.parseInt(fieldsT[k]);
                        switch (sw) {
                            case 1:
                                if (tabN.equals("SalesM"))
                                    Log.d("DALESM", "FT" + fieldsN[k] + "FV=" + Integer.valueOf(rcd[k].toString()));

                                cv.put(fieldsN[k], Integer.valueOf(rcd[k].toString()));
                                break;
                            case 2:
                                cv.put(fieldsN[k], rcd[k].toString());
                                break;
                            default:
                                break;
                        }
                    }
                    db.insert(tabN, null, cv);


                }
                Log.d("Datele in tabel", "------" + tabN);
                Cursor cc = null;
                cc = db.query(tabN, null, null, null, null, null, null);
                logCursor(cc);
                cc.close();
                Log.d("Datele in tabel", "--- ---");
            }
        }

    }

    public void studL(View v) throws IOException {
        DeserializableDB deserializableDB = new DeserializableDB();
        tables.clear();
        tables = deserializableDB.des();
        tv.setText("Data was deserializable");
        System.out.println(tables);

        dbHelper = new DBHelper(this, DBName);
        db = dbHelper.getWritableDatabase();
        Log.d("Create DB=", "The DB " + DBName + "  was created OR Opened the exiting one!");
        Log.d("Group By", "--- INNER JOIN with rawQuery---");

        String whereC = et1.getText().toString().trim();
        String whereV = et3.getText().toString().trim();

        whereV = whereV.replace(" ", "");
        String[] s2 = whereV.split(";");

        int np = s2.length;

        Log.d("Numar de parametri", "parametri= " + np);
        Log.d("WhereC=", "whereC= " + whereC);
        Log.d("WhereV=", "whereV= " + whereV);
        for (int j = 0; j < np; j++)
            s2[j] = s2[j].trim();

        String studLista1 = "";

        studLista1 = "select sale.IDSales as idsale, auto.AutoName as autoName, auto.Age as age,  buyer.BuyerName as buyerName" +
                " from SalesM as sale " +
                "inner join AutoM as auto on sale.IDAuto = auto.AutoID " +
                "inner join BuyerM as buyer on sale.IDBuyer = buyer.BuyerID " +
                "where " + whereC;


        String query = test.createQuery() +
                "where " + whereC;
        System.out.println(query);
        studLista1 = query;


        Cursor c;

        try {
            c = db.rawQuery(studLista1, s2);
            logCursor1(c);
            c.close();
            Log.d("End Lista", "End Lista");
            txtData.setText("Example of Conditions: " + whereC);
        } catch (Exception e) {
            tv.setText("Baza de date nu a fost incarcata, asigurtiva ca ati accesat butonul   \n" + e);
        }


    }


    // afisare in LOG din Cursor
    void logCursor(Cursor c) {
        String str2 = "Results For the Fields : ";
        String cnn = "";
        if (c != null) {
            if (c.moveToFirst()) {
                String str, str1;
                int klu = 0, rr = 0;
                do {
                    rr = 0;
                    str = "";
                    if (klu == 0) //  the first record
                    {
                        for (String cn : c.getColumnNames()) { //if(rr<6)
                            {
                                str = str.concat(c.getString(c.getColumnIndex(cn)) + "; ");
                                cnn = cnn.concat(cn + " ; ");
                                if (rr == 2) cnn = cnn + "\n";
                            }

                            rr++;
                        }

                    }
                    rr = 0;
                    str = "";
                    if (klu > 0) {
                        for (String cn : c.getColumnNames()) {
                            if (rr > -1) {
                                str = str.concat(c.getString(c.getColumnIndex(cn)) + "; ");
                                cnn = cnn.concat(cn + " ; ");
                            }
                            rr++;
                        }

                    }
                    if (klu == 0) {
                        str2 = str2 + cnn;
                        tv.setText(cnn + "\n");
                    }
                    klu++;
                    rr++;
                    str1 = str + "\n";
                    tv.setText(tv.getText() + str1);

                } while (c.moveToNext());

            }
        } else
            Log.d("Rindul", "Cursor is null");
    }

    // afisare in LOG din Cursor
    void logCursor1(Cursor c) {
        try {
            Log.d("COLUMNS NR=", "nc=" + c.getColumnCount());
        } catch (Exception e) {
        }

        tv.setText("");
        String str2 = "Results For the Fields : ";
        String cnn = "", coln = "";
        if (c != null) {
            if (c.moveToFirst()) {
                String str, str1;

                int klu = 0, rr = 0;
                do {
                    rr = 0;
                    str = "";

                    for (String cn : c.getColumnNames()) {
                        if (rr > -1) {
                            str = str.concat(c.getString(c.getColumnIndex(cn)) + "; ");
                            cnn = cnn.concat(cn + " ; ");
                        }
                        rr++;
                        Log.d("COLUMNS NR=", "nc=" + c.getColumnCount() + ", rr=" + rr);
                    }

                    if (klu == 0) {
                        str2 = str2 + cnn;
                        tv.setText(cnn + "\n");
                    }
                    klu++;
                    rr++;
                    str1 = str + "\n";
                    Log.d(" Rindul=", str);
                    tv.setText(tv.getText() + str1);

                } while (c.moveToNext());

            }
        } else
            Log.d("Rindul", "Cursor is null");
    }

    public void SaveData(View v) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("UpDate");
        menu.add("IneqGraphSol");
        menu.add("MovRotate");
        menu.add("Grid");
        menu.add("GraphFun");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}





