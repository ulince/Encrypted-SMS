package com.example.sendsms;

import java.util.ArrayList;
import java.util.List;

import com.example.utils.Field;
import com.example.utils.Int;
import com.exmaple.aes.AES;
import com.exmaple.aes.Utils;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class ReadSms extends ListActivity {
	
	String key;

	 @Override
	   protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       //setContentView(R.layout.activity_read_sms);
	       
	       //txtKey = (EditText) findViewById(R.id.editTextkey);
	       //mList = (ListView)findViewById(R.id.smsNumberText);
	       
	       Intent intent = getIntent();
	       Bundle extras = intent.getExtras();
	       key = extras.getString("key");
	 
	       List<SMSData> smsList = new ArrayList<SMSData>();
	        
	       Uri uri = Uri.parse("content://sms/inbox");
	       Cursor c= getContentResolver().query(uri, null, null ,null,null);
	       startManagingCursor(c);
	        
	       // Read the sms data and store it in the list
	       if(c.moveToFirst()) {
	           for(int i=0; i < c.getCount(); i++) {
	               SMSData sms = new SMSData();
	               sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
	               sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
	               smsList.add(sms);
	                
	               c.moveToNext();
	           }
	       }
	       c.close();
	        
	       // Set smsList in the ListAdapter
	      setListAdapter(new ListAdapter(this, smsList));
	       //mList.setAdapter(new ListAdapter(this, smsList));
	   }
	 
	   @Override
	   protected void onListItemClick(ListView l, View v, int position, long id) {
	       SMSData sms = (SMSData)getListAdapter().getItem(position);
	       Toast.makeText(getApplicationContext(), decrypt(sms.getBody(),key), Toast.LENGTH_LONG).show();
	       //Toast.makeText(getApplicationContext(), decrypt("LUdK4MprY5hDS6Peo0UsqQ==",key), Toast.LENGTH_LONG).show();
	   }
	   
	   
	   
	   /*
	    * @return String containing the encrypted string
	    */
	   public String decrypt(String ciphertext, String k){
		   int state[][] = Utils.stringToBytesDecrypt(ciphertext);
			int key[][] = Utils.stringToBytesEncrypt(k);
			String plaintext;

			Int N = new Int(2);
			Field<Int> zero = new Field<Int>(N);

			AES aes = new AES(state, key);
			aes.decrypt();
			state = aes.getState();
			plaintext = Utils.bytesToStringDecrypt(state);
			return plaintext;
	   }
}
