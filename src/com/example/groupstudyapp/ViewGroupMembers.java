package com.example.groupstudyapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class ViewGroupMembers extends Activity{
	ArrayAdapter<String> listAdapter;
	ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_group_members);
		Intent i=getIntent();
		final String grpId=i.getExtras().getString("grpId");
		//final String grpName=i.getExtras().getString("grpName");
		listview=(ListView)findViewById(R.id.membersList);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserConnections");
		query.whereEqualTo("userGroup", grpId);
		final ArrayList<String> list = new ArrayList<String>();
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("Loading");
		Log.v("hello", "yolo");
		pd.show();
		listAdapter = new ArrayAdapter<String>(this, R.layout.group_row, list);
		
		Log.v("In try block", "yolo");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
		        if (e == null) {
		            Log.d("score", "Retrieved " + scoreList.size() + " scores");
		            for (ParseObject groups : scoreList) {
		                list.add((String) groups.get("userId"));
		                Log.v("group members fretched", (String)groups.get("userId"));
		            }
		            
		            listview.setAdapter(listAdapter);
		    	    listview.setTextFilterEnabled(true);
		    	    pd.cancel();
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		        if (scoreList.size()==0)
		        	Toast.makeText(getApplicationContext(), "No group members", Toast.LENGTH_LONG).show();
		    }
		});
	}
}
