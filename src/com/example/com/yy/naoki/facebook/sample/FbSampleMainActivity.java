package com.example.com.yy.naoki.facebook.sample;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FbSampleMainActivity extends Activity {
	private final static String APP_ID = "259382907515291";
	private final static String APP_SECRET = "8e51465265caf3bb8013b4180460d12f";
	private Facebook mFacebook;
	// permission (https://developers.facebook.com/docs/authentication/permissions/)
	private final static String[] permissions = {"publish_stream"};
	private AsyncFacebookRunner mAsyncFbRunner = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_sample_main);
        
        // Create Facebook object with Application ID which is gotten when you register your application to facebook developer site
        mFacebook = new Facebook(APP_ID);
		mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
        
        // when you click "login" button
        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		        if(!mFacebook.isSessionValid()) { // to check session is expired?
		        	mFacebook.authorize(FbSampleMainActivity.this, permissions, new DialogListener() {

						@Override
						public void onComplete(Bundle values) {
							Toast.makeText(getApplicationContext(), "Authorize Success", Toast.LENGTH_SHORT).show();
							
						}

						@Override
						public void onFacebookError(FacebookError e) {
							Toast.makeText(getApplicationContext(), "Authorize Facebook Error", Toast.LENGTH_SHORT).show();
							
						}

						@Override
						public void onError(DialogError e) {
							Toast.makeText(getApplicationContext(), "Authorize Error", Toast.LENGTH_SHORT).show();
							
						}

						@Override
						public void onCancel() {
							Toast.makeText(getApplicationContext(), "Authorize Cancel", Toast.LENGTH_SHORT).show();
							
						}
		        	});
		        }
			}

        });

        // when you click "Show Timeline" button
        Button timelineButton = (Button) findViewById(R.id.timeline);
        timelineButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//		        if(!mFacebook.isSessionValid()) { // to check session is expired?
		        	Intent intent = new Intent(FbSampleMainActivity.this, FbTimelineActivity.class);
		        	startActivity(intent);
//		        }
			}

        });
        
        // when you click "Post" button
        Button post = (Button) findViewById(R.id.post);
        post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        EditText comment = (EditText) findViewById(R.id.comment);
		        final String body = comment.getText().toString();
				Bundle bundle = new Bundle();
				bundle.putString("message", body);
//				if(!mFacebook.isSessionValid()) {
					mAsyncFbRunner.request("/me/feed", bundle, "POST", new PostRequestListener(), null);
//				}
			}
        	
        });

        // when you click "Post Dialog" button
        Button postDialog = (Button) findViewById(R.id.postDialog);
        postDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        EditText comment = (EditText) findViewById(R.id.comment);
		        final String body = comment.getText().toString();
				Bundle bundle = new Bundle();
				bundle.putString("message", body);
				mFacebook.dialog(FbSampleMainActivity.this, "feed", bundle, new DialogListener() {

					@Override
					public void onComplete(Bundle values) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFacebookError(FacebookError e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onError(DialogError e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fb_sample_main, menu);
        return true;
    }
}
