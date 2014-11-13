package com.example.groupstudyapp;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class CheckLogin extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "Re1VDKadSoKy6AMX4LcOtN5cgnH1mKlaK1GAOCsA", "4W9XpoaPBBKaH2suFNDuqCALBaB8vQVewX5K6Vaj");
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParsePush.subscribeInBackground("channel");
		ParsePush.subscribeInBackground("", new SaveCallback() {
			  @Override
			  public void done(ParseException e) {
			    if (e != null) {
			      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
			    } else {
			      Log.e("com.parse.push", "failed to subscribe for push", e);
			    }
			  }
			});
		boolean netStatus=netStat();
		if (netStatus){
		ParseUser us1=ParseUser.getCurrentUser();
		if (us1!=null){
			//startService(new Intent(this, CheckMessageService.class));
			String userId=us1.getObjectId();
			Intent mainIntent=new Intent(this, MyGroupsActivity.class);
			mainIntent.putExtra("userID", userId);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	Log.v("userID fetched1:", userId);
            startActivity(mainIntent);
		}
		else {
			Intent loginIntent=new Intent(this, AuthenticationActivity.class);
			loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(loginIntent);
		}
		}
	}
	
	boolean netStat(){
		ConnectivityManager connMgr=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo=connMgr.getActiveNetworkInfo();
		if (netInfo==null || !netInfo.isConnected()){
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
		
		return false;
		}
		else return true;
	}
}
