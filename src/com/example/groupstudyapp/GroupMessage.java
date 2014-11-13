package com.example.groupstudyapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

import org.apache.http.ParseException;

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
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupMessage extends Activity {
	ListView messagesListView;
	EditText messageSendingTextbox;
	Button sendButton;
	String groupID;
	String groupNAME;
	Date grpDate;

	Timer timer;
	TimerTask timertask;
	Handler timerhandler = new Handler();

	Set <String>messageSet=new HashSet<String>();
	HashMap<String, String> msgMap=new HashMap<String, String>();
	final ArrayList<String> messageList = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	Date lastMsgDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		groupID = i.getExtras().getString("groupId");
		groupNAME = i.getExtras().getString("groupName");
		setTitle(groupNAME);
		grpDate = (Date) i.getExtras().get("grpCreatedAt");
		Log.v("Selected Group", groupID.toString());
		boolean netStatus = netStat();
		if (netStatus) {
			setContentView(R.layout.activity_group_message);
			messageSendingTextbox = (EditText) findViewById(R.id.newMessage);
			sendButton = (Button) findViewById(R.id.postmessage);
			messagesListView = (ListView) findViewById(R.id.messages);
			adapter = new ArrayAdapter<String>(this, R.layout.group_message,
					messageList);

			ParseQuery<ParseObject> query = ParseQuery
					.getQuery("GroupMessages");
			query.whereEqualTo("groupId", groupID);
			query.orderByAscending("createdAt");
			//query.setLimit(15);
			final ProgressDialog fetchingPogressDialog = new ProgressDialog(this);
			fetchingPogressDialog.setMessage("Loading");
			fetchingPogressDialog.show();

			try {
				Log.v("In try block", "yolo");
				query.findInBackground(new FindCallback<ParseObject>() {
					public void done(List<ParseObject> messages,
							com.parse.ParseException e) {
						if (e == null) {
							if (messages.size() > 0)
								lastMsgDate = messages.get(0).getCreatedAt();
							else
								lastMsgDate = grpDate;
							Log.d("score", "Retrieved " + messages.size()
									+ " scores");
							for (ParseObject groups : messages) {
								messageSet.add(groups.getObjectId());
								messageList.add((String) groups.get("MessageText")+" ~"+(String) groups.get("sentBy"));
								Log.v("GRP: ",
										(String) groups.get("MessageText"));
							}
						/*	for (String msg : msgMap.values())
								messageList.add(msg);
							//Collections.reverse(messageList);
*/
							messagesListView.setAdapter(adapter);
							messagesListView.setTextFilterEnabled(true);
							fetchingPogressDialog.cancel();
						} else {
							Log.d("score", "Error: " + e.getMessage());
						}
					}
				});
			} catch (Exception e) {
				Log.e("some exception:", e.toString());
			}

			sendButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.v("Button ", "Clicked");
					/*
					 * list.add(String.valueOf(ed.getText()));
					 * adapter.notifyDataSetChanged();
					 */

					Log.v("Button ",
							"Channges made" + String.valueOf(messageSendingTextbox.getText()));
					final String msg = String.valueOf(messageSendingTextbox.getText());
					/*
					 * new Thread(new Runnable(){ public void run(){ try{
					 * ParseObject newMessage=new ParseObject("GroupMessages");
					 * newMessage.put("MessageText", msg);
					 * newMessage.put("groupId", groupID);
					 * newMessage.put("sentBy",
					 * ParseUser.getCurrentUser().getEmail());
					 * newMessage.save(); Log.v("thread", this.toString()); }
					 * catch (com.parse.ParseException e) { // TODO
					 * Auto-generated catch block e.printStackTrace(); } }
					 * }).start();
					 */
					ParseObject newMessage = new ParseObject("GroupMessages");
					newMessage.put("MessageText", msg);
					newMessage.put("groupId", groupID);
					newMessage.put("sentBy", ParseUser.getCurrentUser()
							.getEmail());
					newMessage.saveInBackground();
					Log.v("After making changes", "yolo");
					messageSendingTextbox.setText(null);
					
				}
			});

			timer = new Timer();
			initializeTimerTask();
			timer.schedule(timertask, 3000, 1000);
		} else {
			Toast.makeText(getApplicationContext(), "No Network Connection",
					Toast.LENGTH_LONG).show();
		}

	}

	private void displayListView() {
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.group_row, messageList);
		messagesListView.setAdapter(adapter);
		messagesListView.setTextFilterEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.groupxml, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.addGroupMember:
			Intent i = new Intent(this, AddNewGroupMember.class);
			i.putExtra("grpId", groupID);
			i.putExtra("grpName", groupNAME);
			startActivity(i);
			return true;
		case R.id.viewGroupMembers:
			Intent i2 = new Intent(this, ViewGroupMembers.class);
			i2.putExtra("grpId", groupID);
			i2.putExtra("grpName", groupNAME);
			startActivity(i2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void initializeTimerTask() {
		timertask = new TimerTask() {

			@Override
			public void run() {
				timerhandler.post(new Runnable() {

					@Override
					public void run() {
						ParseQuery<ParseObject> updatequery = ParseQuery
								.getQuery("GroupMessages");
						updatequery.whereEqualTo("groupId", groupID);
						// updatequery.whereEqualTo("flag", 0);
						updatequery.whereGreaterThan("createdAt", lastMsgDate);
						updatequery.addAscendingOrder("createdAt");

						try {
							updatequery
									.findInBackground(new FindCallback<ParseObject>() {
										public void done(
												List<ParseObject> messages,
												com.parse.ParseException e) {
											if (e == null) {
												Log.d("score", "Retrieved "
														+ messages.size()
														+ " scores");
												for (ParseObject groups : messages) {
													if (!messageSet.contains(groups.getObjectId())){
														messageSet.add(groups.getObjectId());
													/*if (!msgMap.containsKey(groups.getObjectId())){
													msgMap.put(groups.getObjectId(), (String) groups.get("MessageText"));*/
													lastMsgDate = groups
															.getCreatedAt();
													messageList.add((String) groups.get("MessageText")+" ~"+(String) groups.get("sentBy"));
													Log.v("thread",
															this.toString());
													}
												}
												
												adapter.notifyDataSetChanged();

											} else {
												Log.d("score",
														"Error: "
																+ e.getMessage());
											}
										}
									});
						} catch (ParseException e) {
							Log.e("Parse Error", e.getMessage());
						}

					}

				});

			}

		};
	}

	boolean netStat() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
		if (netInfo == null || !netInfo.isConnected()) {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
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
		} else
			return true;
	}
	
	@Override
	public void onBackPressed(){
		Intent i=new Intent(this, MyGroupsActivity.class);
		startActivity(i);
	}

}
