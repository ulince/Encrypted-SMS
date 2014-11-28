package com.example.sendsms;

import com.example.utils.Field;
import com.example.utils.Int;
import com.exmaple.aes.*;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

   Button sendBtn;
   Button readBtn;
   EditText txtphoneNo;
   EditText txtMessage;
   EditText txtKey;
   TextView encrypted;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      sendBtn = (Button) findViewById(R.id.btnSendSMS);
      readBtn = (Button) findViewById(R.id.btnReadSMS);
      txtphoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
      txtMessage = (EditText) findViewById(R.id.editTextSMS);
      txtKey= (EditText) findViewById(R.id.editTextkey);
      encrypted = (TextView)findViewById(R.id.encryptedText);

      sendBtn.setOnClickListener(new View.OnClickListener() {
         public void onClick(View view) {
            sendSMSMessage();
         }
      });
      
      readBtn.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Bundle b = new Bundle();
			String key = txtKey.getText().toString().trim();
			b.putString("key", key);
			Intent sendTo = new Intent(MainActivity.this,
					com.example.sendsms.ReadSms.class);
			sendTo.putExtras(b);
			startActivity(sendTo);
			
		}
	});

   }
   protected void sendSMSMessage() {
      Log.i("Send SMS", "");

      String phoneNo = txtphoneNo.getText().toString();
      String message = txtMessage.getText().toString();
      String key = txtKey.getText().toString();
      message = encrypt(message, key);
      encrypted.setText(message);
      

      try {
         SmsManager smsManager = SmsManager.getDefault();
         smsManager.sendTextMessage(phoneNo, null, message, null, null);
         Toast.makeText(getApplicationContext(), "SMS sent.",
         Toast.LENGTH_LONG).show();
      } catch (Exception e) {
         Toast.makeText(getApplicationContext(),
         "SMS faild, please try again.",
         Toast.LENGTH_LONG).show();
         e.printStackTrace();
      }
   }
 
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   
   /*
    * @return String containing the encrypted string
    */
   public String encrypt(String plaintext, String k){
	   int state[][] = Utils.stringToBytesEncrypt(plaintext);
		int key[][] = Utils.stringToBytesEncrypt(k);
		String cipherText;

		Int N = new Int(2);
		Field<Int> zero = new Field<Int>(N);

		AES aes = new AES(state, key);
		aes.encrypt();
		state = aes.getState();
		cipherText = Utils.bytesToStringEncrypt(state);
		return cipherText;
   }
   

   
   
   
   
      
   
}