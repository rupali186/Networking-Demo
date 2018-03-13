package com.example.rupali.jsonplaceholder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RUPALI on 13-03-2018.
 */

public class OpenHelper extends SQLiteOpenHelper {
    private static OpenHelper openHelper;

    public static OpenHelper getOpenHelper(Context context) {
        if(openHelper==null){
            openHelper=new OpenHelper(context.getApplicationContext());
        }
        return openHelper;
    }

    public OpenHelper(Context context) {
        super(context,Contract.DATABASE_NAME , null, Contract.VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String userSQL="CREATE TABLE "+Contract.ContractUser.TABLE_NAME+" ( "+Contract.ContractUser.ID+" INTEGER PRIMARY KEY , "
                +Contract.ContractUser.NAME+" TEXT , "+Contract.ContractUser.USERNAME+" TEXT , "+
                Contract.ContractUser.EMAIL+" TEXT )";
        sqLiteDatabase.execSQL(userSQL);
        String postSQL="CREATE TABLE "+Contract.ContractPost.TABLE_NAME+" ( "+Contract.ContractPost.ID+" INTEGER PRIMARY KEY , "
                +Contract.ContractPost.TITLE+" TEXT , "+Contract.ContractPost.BODY+" TEXT , "+Contract.ContractPost.USERID+
                " INTEGER , FOREIGN KEY  ( "+Contract.ContractPost.USERID+" ) REFERENCES "+Contract.ContractUser.TABLE_NAME+" ( "
                +Contract.ContractUser.ID+" ) ON DELETE CASCADE )";
        sqLiteDatabase.execSQL(postSQL);
        String commentSQL="CREATE TABLE "+Contract.ContractComments.TABLE_NAME+" ( "+Contract.ContractComments.ID+
                " INTEGER PRIMARY KEY , "+Contract.ContractComments.POSTID+" INTEGER , "+Contract.ContractComments.NAME+
                " TEXT , "+Contract.ContractComments.BODY+" TEXT , "+Contract.ContractComments.EMAIL+" TEXT ,FOREIGN KEY ( "+
                Contract.ContractComments.POSTID+" ) REFERENCES "+Contract.ContractPost.TABLE_NAME+" ( "+Contract.ContractPost.ID
                +" ) ON DELETE CASCADE )";
        sqLiteDatabase.execSQL(commentSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
