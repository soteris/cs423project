package com.trustme.testsu;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContactsDataSource {
	private final String TAG = "ContactsDataSource";
	private SQLiteDatabase database;
	private DatabaseOpenHelper dbHelper;
	
	public ContactsDataSource(Context context) {
	    dbHelper = new DatabaseOpenHelper(context);
	    if (dbHelper != null)
	    {
	    	Log.i(TAG, "Database object returned");
	    }
	  }

	  public SQLiteDatabase open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	    return database;
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Contact createContact(String name, String middle_name, String surname, String email, String phone, String address) {
		ContentValues  values = new ContentValues ();
	    values.put(DatabaseOpenHelper.COLUMN_NAME, name); //column_name, column_value
	    long insertId = database.insert(DatabaseOpenHelper.DATABASE_TABLE_NAME, null,
	        values);
	    
	    Cursor cursor = database.query(DatabaseOpenHelper.DATABASE_TABLE_NAME,
	        null, null, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Contact newContact = cursorToContact(cursor,1);
	    cursor.close();
	    
	    return newContact;
	  }
	  
	  private Contact cursorToContact(Cursor cursor, int column_number) {
		    Contact contact = new Contact();
		    contact.setId(cursor.getLong(0));
		    contact.setName(cursor.getString(column_number));
		    return contact;
		  }

	  public void deleteComment(Contact contact) {
	    long id = contact.getId();
	    System.out.println("Comment deleted with id: " + id);
	    database.delete(DatabaseOpenHelper.DATABASE_TABLE_NAME, DatabaseOpenHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<Contact> getAllContacts() {
	    List<Contact> contacts = new ArrayList<Contact>();

	    Cursor cursor = database.query(DatabaseOpenHelper.DATABASE_TABLE_NAME,
	        null, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Contact contact = cursorToContact(cursor, 1);
	      contacts.add(contact);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return contacts;
	  }
}
