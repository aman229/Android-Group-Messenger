package com.example.groupstudyapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AddNewGroupMember extends Activity {
	String[] userids;
	String grpId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean netStatus=netStat();
		if (netStatus){
		setContentView(R.layout.activity_add_new_group_member);
		Intent i=getIntent();
		grpId=i.getExtras().getString("grpId");
		final String grpName=i.getExtras().getString("grpName");
		final ListView listview=(ListView)findViewById(R.id.usersList);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserFriends");
		query.whereEqualTo("userId", ParseUser.getCurrentUser().getEmail());
		final ArrayList<String> list = new ArrayList<String>();
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("Loading");
		pd.show();
		final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.group_row, list);
		try{
			Log.v("In try block", "yolo");
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
			        if (e == null) {
			            Log.d("score", "Retrieved " + scoreList.size() + " scores");
			            userids=new String[scoreList.size()];
			            int i=0;
			            for (ParseObject groups : scoreList) {
			                list.add((String) groups.get("friendUserId"));
			                Log.v("user added: ", (String) groups.get("friendUserId"));
			                userids[i]=(String) groups.get("friendUserId");
			                i++;
			            }
			            
			    	    listview.setAdapter(listAdapter);
			    	    listview.setTextFilterEnabled(true);
			    	    pd.cancel();
			        } else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
			        if (scoreList.size()==0)
			        	Toast.makeText(getApplicationContext(), "You do not have any connections", Toast.LENGTH_LONG).show();
			    }
			});
		}
		catch(Exception e){
			Log.e("some exception:", e.toString());
		}

	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	
	    	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean alreadyExists=checkMember(userids[position]);
				if (!alreadyExists){
				try {
					ParseObject addUsr=new ParseObject("UserConnections");
					addUsr.put("userGroup", grpId);
					addUsr.put("groupName", grpName);
					addUsr.put("userId", userids[position]);
					addUsr.save();
					Toast.makeText(getApplicationContext(),
                            "Group Member Added",
                            Toast.LENGTH_LONG).show();
				} catch (ParseException e) {
					Log.e("error", e.getMessage());
				}
				}
				else
				Toast.makeText(getApplicationContext(), "User Already Exists in Group", Toast.LENGTH_LONG).show();
			}
	    });
		}
		else {
			Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
		}
	    
	}
	boolean retValue;
	boolean checkMember(String userid){
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserConnections");
		query.whereEqualTo("userGroup", grpId);
		query.whereEqualTo("userId", userid);
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
