package com.example.groupstudyapp;

import java.util.ArrayList;
import java.util.Date;
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
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyGroupsActivity extends Activity {
	


	protected static final String ACTIVITY_NAME = null;
	String[] groupID;
	String[] groupNAME;
	Date[] grpDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		boolean netStatus=netStat();
		if (netStatus){
		setContentView(R.layout.activity_my_groups);
		final Intent intent = new Intent(this, GroupMessage.class);

		final ListView listview=(ListView)findViewById(R.id.groupslist);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserConnections");
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
			            groupID=new String[scoreList.size()];
			            groupNAME=new String[scoreList.size()];
			            grpDate=new Date[scoreList.size()];
			            int i=0;
			            for (ParseObject groups : scoreList) {
			                list.add((String) groups.get("groupName"));
			                groupID[i]=(String) groups.get("userGroup");
			                groupNAME[i]=(String) groups.get("groupName");
			                grpDate[i]=groups.getCreatedAt();
			                Log.v("created at: ", groups.getCreatedAt().toString());
			                i++;
			            }
			            
			    	    listview.setAdapter(listAdapter);
			    	    listview.setTextFilterEnabled(true);
			    	    pd.cancel();
			        } else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
			        if (scoreList.size()==0)
			        	Toast.makeText(getApplicationContext(), "No Records Found", Toast.LENGTH_LONG).show();
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
					//intent.putExtra(ACTIVITY_NAME, values[position]);
					intent.putExtra("groupId", groupID[position]);
					intent.putExtra("groupName", groupNAME[position]);
					intent.putExtra("grpCreatedAt", grpDate[position]);
					startActivity(intent);
					Log.v("Goint to group, selected=", groupID[position]+" "+groupNAME[position]+" "+grpDate[position].toString());
					finish();
				//Toast.makeText(getApplicationContext(), values[position], Toast.LENGTH_LONG).show();
			}
	    });
		}
	    
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.title_section2:
	            this.startActivity(new Intent(this, CreateNewGroup.class));
	            return true;
	        case R.id.title_section3:
	            this.startActivity(new Intent(this, Settings.class));
	            return true;
	        case R.id.title_section4:
	            this.startActivity(new Intent(this, Logout.class));
	            return true;
	        case R.id.title_section5:
	            this.startActivity(new Intent(this, NewConnection.class));
	            return true;
	        case R.id.title_section6:
	            this.startActivity(new Intent(this, ViewConnections.class));
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
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
	@Override
	public void onBackPressed(){
		Intent i=new Intent(this, CheckLogin.class);
		startActivity(i);
	}
}
