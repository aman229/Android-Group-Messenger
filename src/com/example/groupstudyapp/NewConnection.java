package com.example.groupstudyapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NewConnection extends Activity {

	ListView lvList;
	EditText ed;
	Button bt;
	String groupID;
	final ArrayList<String> list = new ArrayList<String>();
	String[] searchUserId;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 boolean netStatus=netStat();
	 if (netStatus){
	 setContentView(R.layout.activity_new_connection);
	 ed = (EditText) findViewById(R.id.searchname);
	 bt = (Button) findViewById(R.id.finduserbutton);
	 lvList = (ListView) findViewById(R.id.userslist);

	final ProgressDialog pd = new ProgressDialog(this);
	
	 bt.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.v("Search Button ", "Clicked");
			String searchnumber=String.valueOf(ed.getText());
			Log.v("Search Button ", String.valueOf(ed.getText()));
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("mobileno", searchnumber);
			
			
			 pd.setMessage("Loading");
			 pd.show();
			 try{
					//Log.v("In try block", "yolo");
					query.findInBackground(new FindCallback<ParseUser>() {
					    public void done(List<ParseUser> scoreList, com.parse.ParseException e) {
					        if (e == null) {
					            Log.d("score", "Retrieved " + scoreList.size() + " scores");
					            searchUserId=new String[scoreList.size()];
					            int i=0;
					            for (ParseUser users : scoreList) {
					                list.add((String) users.get("firstname")+" "+(String)users.get("lastname"));
					                //Log.v(list.get(0).toString(), "yolo");
					                searchUserId[i]=(String) users.getEmail();
					                i++;
					            }
					            displayListView();
					            
					    	    //lvList.addFooterView(ed);
					    	    pd.cancel();
					        } else {
					            Log.d("score", "Error: " + e.getMessage());
					        }
					        if (scoreList.size()==0)
					        	Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_LONG).show();
					    }
					});
				}
				catch(Exception e){
					Log.e("some exception:", e.toString());
				}
			 
		}
	 });
	 lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.v("before adding : ", searchUserId[position]);
				boolean alreadyExists=checkMember(searchUserId[position]);
				if (!alreadyExists){
				try{
					
					ParseObject addFriend=new ParseObject("UserFriends");
					addFriend.put("userId", ParseUser.getCurrentUser().getEmail());
					addFriend.put("friendUserId", searchUserId[position]);
					addFriend.save();
					Toast.makeText(getApplicationContext(),
                            "Friend Added",
                            Toast.LENGTH_LONG).show();
				}catch (com.parse.ParseException e){
					Log.e("error", e.getMessage());
				}
				}
				else
					Toast.makeText(getApplicationContext(), "Connection Already Exists", Toast.LENGTH_LONG).show();
			}
	    });
	 }
	 else 
		 Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
	 }
			

	 
	 private void displayListView(){
		 final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.group_row, list);
		 lvList.setAdapter(adapter);
		 lvList.setTextFilterEnabled(true);
	 }
	 
	 boolean retValue;
		boolean checkMember(String userid){
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserFriends");
			query.whereEqualTo("userId", ParseUser.getCurrentUser().getEmail());
			query.whereEqualTo("friendUserId", userid);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
			        if (e == null) {
			            Log.d("score", "Retrieved " + scoreList.size() + " scores");
			            if (scoreList.size()==0)
			            	retValue=false;
			            else retValue=true;
			        } else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
			    }
			});
			return retValue;
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
