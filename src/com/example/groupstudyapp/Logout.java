package com.example.groupstudyapp;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Logout extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logout);
		ParseUser.logOut();
		
	}
	
	@Override
	public void onBackPressed(){
		Intent i=new Intent(this, CheckLogin.class);
		startActivity(i);
	}
}
