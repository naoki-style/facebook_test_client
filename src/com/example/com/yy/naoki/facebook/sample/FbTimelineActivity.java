package com.example.com.yy.naoki.facebook.sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FbTimelineActivity extends ListActivity {
	private static final String API_KEY = "259382907515291";
	private Facebook mFacebook = null;
	// permission (https://developers.facebook.com/docs/authentication/permissions/)
	private final static String[] permissions = {"publish_stream", "read_stream", "offline_access"};

	private AsyncFacebookRunner mAsyncFbRunner = null;
	private JSONObject mJobj = null;
	private ArrayList<String> msgList = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_item);
		
		// create Facebook Object and AsyncFacebookRunner
		mFacebook = new Facebook(API_KEY);
		mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
		
		// login
		mFacebook.authorize(FbTimelineActivity.this, permissions, new DialogListener() {

			@Override
			public void onComplete(Bundle values) { // Called when Authorize is done
				Toast.makeText(getApplicationContext(), "Authorize Completed", Toast.LENGTH_SHORT).show();
//				mAsyncFbRunner.request("me/feed", new PostRequestListener() {
				mAsyncFbRunner.request("me/home", new PostRequestListener() {
					@Override
					public void onComplete(String response, Object state) {
						try {
							String tmp_str = null;
							mJobj = new JSONObject(response);
							JSONArray dataArray = mJobj.getJSONArray("data");
							
							for(int i=0; i<dataArray.length(); i++) {
								JSONObject obj = dataArray.getJSONObject(i);
								if(obj.has("from")) {
									JSONObject internalObj = obj.getJSONObject("from");
									if(internalObj.has("name")) {
										tmp_str = internalObj.getString("name") + "\n";
									}
								}
								if(obj.has("message")) {
									tmp_str += obj.getString("message") + "\n";
								}
								if(null != tmp_str) {
									msgList.add(tmp_str);
								}
								tmp_str = null;
							}
							Intent intent=new Intent(FbTimelineActivity.this, FbTimelineActivity.class);
							intent.putStringArrayListExtra("msgList", msgList);
							FbTimelineActivity.this.startActivity(intent);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onIOException(IOException e, Object state) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFileNotFoundException(
							FileNotFoundException e, Object state) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onMalformedURLException(
							MalformedURLException e, Object state) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFacebookError(FacebookError e, Object state) {
						// TODO Auto-generated method stub
						
					}
				});
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFacebookError(FacebookError e) { // Called when a Facebook responds to a dialog with an error.
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(DialogError e) { // Called when a Facebook responds to a dialog with an error.
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCancel() { // Called when a Facebook responds to a dialog with an error.
				Toast.makeText(getApplicationContext(), "Authorize Canceled", Toast.LENGTH_SHORT).show();
				
			}
			
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		ArrayList<String> list = intent.getStringArrayListExtra("msgList");
		if(list == null) return;
		ListView listView = (ListView) findViewById(android.R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
	}

}
