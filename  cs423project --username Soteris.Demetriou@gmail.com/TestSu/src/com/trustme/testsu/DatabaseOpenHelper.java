package com.trustme.testsu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "TRUSTME";
    
    public static final String COLUMN_ID = "_id";
    public static final String DATABASE_TABLE_NAME = "contact";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MIDDLE_NAME = "middle_name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";
    public static final String DATABASE_TABLE_CREATE =
                "CREATE TABLE contact (" +
                COLUMN_ID + " INTEGER PRIMARY KEY autoincrement," +
                COLUMN_NAME + " TEXT," +
                COLUMN_MIDDLE_NAME + " TEXT," +
                COLUMN_SURNAME + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PHONE + " TEXT," +
                COLUMN_ADDRESS + " TEXT"+
                ");";

    DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}