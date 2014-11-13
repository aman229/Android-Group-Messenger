package com.example.groupstudyapp;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CreateNewGroup extends Activity {
	
	EditText ed;
	Button bt;
	Intent i;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean netStatus=netStat();
		if (netStatus){
		setContentView(R.layout.activity_create_new_group);
		bt=(Button)findViewById(R.id.creategroupbutton);
		ed=(EditText)findViewById(R.id.groupname);
		Log.v("Reached here", "1");
		i=new Intent(this, SelectGroupMembers.class);
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String grpname=String.valueOf(ed.getText());
				Log.v("Reached here", "");
				i.putExtra("grpName", grpname);
				try{
					ParseObject newGrp=new ParseObject("Groups");
					newGrp.put("Name", grpname);
					newGrp.save();
					i.putExtra("grpId", newGrp.getObjectId());
					i.putExtra("grpName", grpname);
					Log.v("Grp id", newGrp.getObjectId());
					ParseObject addUsr=new ParseObject("UserConnections");
					addUsr.put("userId",ParseUser.getCurrentUser().getEmail());
					addUsr.put("userGroup", newGrp.getObjectId());
					addUsr.put("groupName", grpname);
					addUsr.save();
					Log.v("Credentials: ", ParseUser.getCurrentUser().getEmail()+" "+newGrp.getObjectId()+" "+grpname);
					Toast.makeText(getApplicationContext(),
                            "Group Created Successfully",
                            Toast.LENGTH_LONG).show();
				}catch (ParseException e){
					Log.e("Error Creating Group: ", e.getMessage());
				}
				startActivity(i);
			}
		});
	}
		else 
			Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
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
