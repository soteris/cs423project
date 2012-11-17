package com.trustme.testsu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.AsyncTask;
import android.util.Log;

import com.trustme.testsu.utils.Constants;

/**
* 
* A thread task that will modify the Preferences File of the 
*  Superuser app
* @author sdemetr2
*/
public class ModifySuPrefsTask extends AsyncTask<Void, Void, Void>{
	public static final String TAG = "ModifySuPrefsTask";
	
	protected ModifySuPrefsTask() { }
	
	@Override
	protected Void doInBackground(Void... unused){
		try {
			
			Thread.sleep(50);
		}
		catch (InterruptedException localInterruptedException)
		{
			Log.d("MyPlugin", "RestartTask received an InterruptedException");
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void justEyeCandy){
		super.onPostExecute(justEyeCandy);
		modifySuPrefs();
	}
	
	/**
	 * modify the preferences xml file of the Superuser app
	 */
	private void modifySuPrefs() {
		// TODO Auto-generated method stub
        File file = new File (Constants.SU_PREFERENCES_PATH + Constants.SU_PREFERENCES_FILENAME + ".bp");
        
        FileInputStream fs = null;
        InputStreamReader in = null;
        BufferedReader br = null;
        
        StringBuffer sb = new StringBuffer();
        String textinLine;
        String textToEdit = "<boolean name=\"pref_notifications\" value=\"true\" />";
        String newText = "<boolean name=\"pref_notifications\" value=\"false\" />";
        
        try {
        	fs = new FileInputStream(file);
            in = new InputStreamReader(fs);
            br = new BufferedReader(in);
	        
	        while(true)
            {

                textinLine=br.readLine();
                Log.i(TAG, "textinLine.length:" + textinLine);
                if(textinLine == null)
                    break;
                sb.append(textinLine);
            }
	        //Log.i(TAG, "sb:" + sb.toString());
            int cnt1 = sb.indexOf(textToEdit);
            Log.i(TAG, "cnt1: " + cnt1 + ", cnt1+textToEdit.length():" + cnt1+textToEdit.length() );
            sb.replace(cnt1,cnt1+textToEdit.length(),newText);
            
            fs.close();
            in.close();
            br.close();
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "FileNotFoundException: " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "IOException: " + e);
		}
        
        try{
            FileWriter fstream = new FileWriter(file);
            BufferedWriter outobj = new BufferedWriter(fstream);
            outobj.write(sb.toString());
            outobj.close();

        }catch (Exception e){
          System.err.println("Error: " + e.getMessage());
        }
        
		
	}
}
