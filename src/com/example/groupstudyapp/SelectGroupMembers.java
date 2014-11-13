package com.example.groupstudyapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectGroupMembers extends Activity {
	String[] userids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_group_members);
		Intent i=getIntent();
		final String grpId=i.getExtras().getString("grpId");
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
				//Toast.makeText(getApplicationContext(), values[position], Toast.LENGTH_LONG).show();
			}
	    });
	    
	}
	
	@Override
	public void onBackPressed(){
		Intent i=new Intent(this, MyGroupsActivity.class);
		startActivity(i);
	}
}
