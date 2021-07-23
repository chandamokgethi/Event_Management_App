package com.bac.pedalpals.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.Nullable;

import com.bac.pedalpals.model.Event;

public class EventDatabase extends SQLiteOpenHelper {

    Context context;
    public  static final String tbName = "Event";
    private  static final String ColId = "Id";
    private  static final String ColLocation = "Location";
    private  static final String ColImage = "Image";
    private  static final String ColType = "Type";
    private  static final String ColDate = "date";
    private  static final String ColEmail = "email";
    private  static final String ColContact = "Contact";
    private  static final String ColDesc = "Description";
    public  static final String dbName = "DBEvent";

    public EventDatabase(@Nullable Context context) {
        super(context,dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "  + tbName + "("+
                ColId + " integer PRIMARY KEY AUTOINCREMENT, "+
                ColDesc + " text, "+
                ColType + " text ,"+
                ColLocation + " text, "+
                ColDate + " text,"+
                ColContact + " text,"+
                ColEmail + " text,"+
                ColImage + " BLOB );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + tbName);
        onCreate(db);

    }

    public long insert(Event event){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        //query to insert record into  database.

        String query = "INSERT INTO " + tbName + "(" +ColDesc + "," +ColType + "," + ColLocation + "," + ColDate +","+ ColContact +","+ ColEmail +","  + ColImage + ") VALUES(?,?,?,?,?,?,?) ";

        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(query);

        sqLiteStatement.clearBindings();

        sqLiteStatement.bindString(1,event.getDesc());
        sqLiteStatement.bindString(2,event.getType());
        sqLiteStatement.bindString(3,event.getLocation());
        sqLiteStatement.bindString(4,event.getDate());
        sqLiteStatement.bindString(5,event.getContact());
        sqLiteStatement.bindString(6,event.getEmail());
        sqLiteStatement.bindBlob(7,event.getImg());

       long s = sqLiteStatement.executeInsert();

       return  s;
       // System.out.println("Inserting into the Database");

    }

    public void update(Event event){


        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        //query to insert record into  database.

        String query = "UPDATE " + tbName + " SET "+ ColContact + " =?," + ColLocation + " =?, " + ColImage + " = ?, " + ColType + " = ?, " + ColDesc + "=?, " + ColDate + " =? WHERE  " + ColId + "=? ";

        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(query);

        sqLiteStatement.clearBindings();

        sqLiteStatement.bindString(1,event.getContact());
        sqLiteStatement.bindString(2,event.getLocation());
        sqLiteStatement.bindBlob(3,event.getImg());
        sqLiteStatement.bindString(4,event.getType());
        sqLiteStatement.bindString(5,event.getDesc());
        sqLiteStatement.bindString(6,event.getDate());
        sqLiteStatement.bindLong(7,event.getId());
         sqLiteStatement.execute();
         System.out.println("had to do it..");
        sqLiteStatement.close();

    }

    public void queryData(String query){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(query);
    }

    public void delete(Event event){


        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        //query to insert record into  database.

        String query = "DELETE FROM " + tbName + " WHERE " +ColId + " = ?";

        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(query);

        sqLiteStatement.clearBindings();
        sqLiteStatement.bindLong(1,event.getId());

        sqLiteStatement.execute();

        sqLiteStatement.close();
    }

    public Cursor getEvent(String query){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        return  sqLiteDatabase.rawQuery(query,null);

    }
}
