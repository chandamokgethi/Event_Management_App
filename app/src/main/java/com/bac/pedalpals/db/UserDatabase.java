package com.bac.pedalpals.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.Nullable;

import com.bac.pedalpals.model.User;

public class UserDatabase extends SQLiteOpenHelper {

    Context context;
    public  static final String dbName = "Event";
    public  static final String tbName = "Users";
    private  static final String ColId = "Id";
    private  static final String ColEmail = "Email";
    private  static final String ColUsername = "Username";
    private  static final String ColPassword = "Password";
    private  static final String ColRank = "Review";





    public UserDatabase(@Nullable Context context) {
        super(context, dbName, null, 1, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

         db.execSQL("CREATE TABLE " + tbName + " ( " +
                    ColId + " integer primary key AUTOINCREMENT," +
                    ColEmail + " text,"+
                    ColUsername + " text," +
                    ColPassword + " text," +
                    ColRank + " INTEGER DEFAULT 0);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           db.execSQL("DROP TABLE IF EXISTS " + tbName);
           onCreate(db);
    }

    public long registerUser(User user){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("Email", user.getEmail());
        contentValues.put("Username", user.getUsername());
        contentValues.put("Password", user.getPassword());
        long value = db.insert(tbName, null, contentValues);
        db.close();
        return value;
    }

    public  boolean checkUser(User user){
        boolean exist = false;

        String[] columns = {ColId};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = ColUsername + "=?" + " and " + ColPassword + "=?";
        String[] selectionArgs = {user.getUsername(), user.getPassword()};
        Cursor cursor = db.query(tbName,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if(count > 0)
        {
            exist = true;
        }



        return exist;
    }

    public void reviewUser(User user)
    {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "UPDATE " + tbName + " SET " + ColRank + " =? WHERE  " + ColId + "=? ";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(query);

        sqLiteStatement.clearBindings();

        sqLiteStatement.bindLong(1,  user.getReview());
        sqLiteStatement.bindLong(2,user.getId());

        sqLiteStatement.execute();

        sqLiteStatement.close();


    }

    public Cursor getUser(String query){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        return  sqLiteDatabase.rawQuery(query,null);

    }
}
