package com.example.groupstudyapp;


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
import com.parse.LogInCallback;
import com.parse.ParseUser;

public class AuthenticationActivity extends Activity {

	public static final String EXTRA_UNAME = null;
	public static final String EXTRA_UPASS = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authentication, menu);
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
	
	public void login(View view){
		ConnectivityManager connMgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo=connMgr.getActiveNetworkInfo();
		if (netInfo!=null && netInfo.isConnected()){
			final Intent mainintent = new Intent(this, MyGroupsActivity.class);
			EditText usname=(EditText) findViewById(R.id.Username);
			EditText pass=(EditText) findViewById(R.id.Password);
			String username=usname.getText().toString();
			String password=pass.getText().toString();
			if (username.equals("") && password.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Please fill in the credentials",
                        Toast.LENGTH_LONG).show();

            }
			else {
			ParseUser.logInInBackground(username, password,
                    new LogInCallback() {
                        public void done(ParseUser user, com.parse.ParseException e) {
                            if (user != null) {
                            	String currUserId=user.getObjectId();
                            	mainintent.putExtra("userID", currUserId);
                            	Log.v("userID fetched1:", currUserId);
                                startActivity(mainintent);
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Logged in",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                            	Toast.makeText(
                                        getApplicationContext(),
                                        "No such user exist, please signup",
                                        Toast.LENGTH_LONG).show();
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
	
	public void register(View view){
		Intent intent2=new Intent(this, RegistrationActivity.class);
		this.startActivity(intent2);
	}
}
