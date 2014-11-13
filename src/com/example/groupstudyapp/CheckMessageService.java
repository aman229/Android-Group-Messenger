package com.example.groupstudyapp;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class CheckMessageService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate(){
		Toast.makeText(getApplicationContext(),
                "Service Created",
                Toast.LENGTH_LONG).show();
		/*HandlerThread thread = new HandlerThread("ServiceStartArguments",
	            Process.THREAD_PRIORITY_BACKGROUND);
	    thread.start();*/
	}
	
	/*@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	      // Let it continue running until it is stopped.
	      Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	      
	      final ArrayList<String> list = new ArrayList<String>();      
	      ParseQuery<ParseObject> fetchgroups=ParseQuery.getQuery("UserConnections");
	      fetchgroups.whereEqualTo("userId", ParseUser.getCurrentUser().getEmail());
			try{
				fetchgroups.findInBackground(new FindCallback<ParseObject>() {
				    public void done(List<ParseObject> groups, com.parse.ParseException e) {
				        if (e == null) {
				            Log.d("score", "Retrieved " + groups.size() + " scores");
				            for (ParseObject group : groups) {
				                list.add((String) group.get("userGroup"));
				                Log.d("grps fetched ", (String) group.get("userGroup"));
				            }
				            
				        } else {
				            Log.d("score", "Error: " + e.getMessage());
				        }
				    }
				});
			}catch (ParseException e){
				Log.e("Parse Error", e.getMessage());
			}
			
			ParseQuery<ParseObject> fetchmessages=ParseQuery.getQuery("GroupMessages");
			fetchmessages.whereContainedIn("groupId", list);
			fetchmessages.whereGreaterThan("createdAt", lastDate);
			
				try{
					fetchmessages.findInBackground(new FindCallback<ParseObject>() {
					    public void done(List<ParseObject> messages, com.parse.ParseException e) {
					        if (e == null) {
					            Log.d("score", "Retrieved " + groups.size() + " scores");
					            for (ParseObject group : groups) {
					                list.add((String) group.get("userGroup"));
					            }
					            
					        } else {
					            Log.d("score", "Error: " + e.getMessage());
					        }
					    }
					});
				}catch (ParseException e){
					Log.e("Parse Error", e.getMessage());
				}
	      
	      return START_STICKY;
	   }*/

}
