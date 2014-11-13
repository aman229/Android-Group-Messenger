package com.example.groupstudyapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewConnections extends Activity {

	ArrayAdapter<String> listAdapter;
	ListView listview;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_connections);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserFriends");
		query.whereEqualTo("userId", ParseUser.getCurrentUser().getEmail());
		listview=(ListView)findViewById(R.id.connectionsList);
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
		                list.add((String) groups.get("friendUserId"));
		                Log.v(" friendfretched", (String)groups.get("friendUserId"));
		            }
		            //list.add("yolo");
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
}
