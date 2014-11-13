package com.example.groupstudyapp;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		i=new Intent(this, CheckLogin.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void registernewuser(View view){
		ConnectivityManager connMgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo=connMgr.getActiveNetworkInfo();
		if (netInfo!=null && netInfo.isConnected()){
			EditText fname=(EditText) findViewById(R.id.Firstname);
			EditText lname=(EditText) findViewById(R.id.Lastname);
			EditText email=(EditText) findViewById(R.id.Email);
			EditText pass=(EditText) findViewById(R.id.Password);
			EditText mobNo=(EditText) findViewById(R.id.Mobileno);
			final String ufname=fname.getText().toString();
			final String ulname=lname.getText().toString();
			String uemail=email.getText().toString();
			String password=pass.getText().toString();
			String mobno=mobNo.getText().toString();
			if (ufname.equals("") || ulname.equals("") ||uemail.equals("") || password.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Please complete the sign up form",
                        Toast.LENGTH_LONG).show();
            }
			else {
				ParseUser user = new ParseUser();
                user.setUsername(uemail);
                user.setEmail(uemail);
                user.setPassword(password);
                user.put("firstname", ufname);
                user.put("lastname",  ulname);
                user.put("mobileno",  mobno);
                user.signUpInBackground(new SignUpCallback() {
                	  public void done(com.parse.ParseException e) {
                	    if (e == null) {
                	    	Toast.makeText(getApplicationContext(),
                                    "Signup Successful!!!",
                                    Toast.LENGTH_LONG).show();
                	    	
                	    	startActivity(i);
                	    	
                	    }
                	    else {
                	    	Toast.makeText(getApplicationContext(),
                                    "Error Ocurred!! Please try again",
                                    Toast.LENGTH_LONG).show();
                	    	Log.e("Error: ",  e.getMessage());
                	    }
                	  }
                });
			}
		}
		else {
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
			dlgAlert.setMessage("Could not connect to the network");
            dlgAlert.setTitle("Error");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            
		}
	}
}
