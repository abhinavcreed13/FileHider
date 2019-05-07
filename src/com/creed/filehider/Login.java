package com.creed.filehider;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	 private EditText  username=null;
	   private EditText  password=null;
	   private TextView attempts;
	   private Button login;
	   SQLcontroller controller=new SQLcontroller(this);
	   String name, name2, filename, pathname, ptype, pext;
	   Intent intent;
	   Bundle extras;
	   String action;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		 username = (EditText)findViewById(R.id.editText1);
	      password = (EditText)findViewById(R.id.editText2);
	      login = (Button)findViewById(R.id.button1);
	      File dsc = Environment.getExternalStorageDirectory();
			String dsc2 = dsc + "/Android/data/com.creed.filehider/";
			String path="/Android/data/com.creed.filehider/";
			intent = getIntent();
		      extras = intent.getExtras();
		      action = intent.getAction();
	      try
	      {
	    	String val=controller.getcountuser();
	    	if(val.compareTo("0")==0)
	    	{
	    	Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
	      controller.insertuser("creed", "1918");
	    	}
	      }
	      catch(Exception e)
	      {}
	}
	public void login(View view){
	     
	      if(controller.checkuser(username.getText().toString(), password.getText().toString()))
	      {
	    	  Toast.makeText(getApplicationContext(), "Welcome!!", 
	    		      Toast.LENGTH_SHORT).show();
	      Intent myIntent = new Intent(Login.this, HomeActivity.class);
	      if(action.compareTo("android.intent.action.MAIN")==0)
	      {
	      }
	      else
	      {
	    	  myIntent.putExtras(intent);
		      myIntent.putExtras(extras);
		      myIntent.setAction(action);	  
	      }
	      Toast.makeText(getApplicationContext(), action, 
    		      Toast.LENGTH_LONG).show();
	      Login.this.startActivity(myIntent);
	   }	
	      else
	      {
	    	  Toast.makeText(getApplicationContext(), "Invalid Username or Password!", 
	    		      Toast.LENGTH_SHORT).show();
	    		      
	      }
	      
	      }
}
