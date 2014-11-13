package com.example.groupstudyapp;


import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import android.app.Application;
import android.util.Log;

public class GroupApp extends Application{
	@Override
	public void onCreate(){
		super.onCreate();
		Parse.initialize(this, "key1", "key2");
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
}

