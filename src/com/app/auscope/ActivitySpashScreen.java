package com.app.auscope;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivitySpashScreen extends Activity 
{
	private Intent mWorldViewIntent;
	private final long delayTime = 750;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		PreferencesManager.init(this.getApplicationContext());
		Fn.init(this.getApplicationContext());
		
		mHandler.postDelayed(mCountUpdater, delayTime);
		mWorldViewIntent = new Intent(this, ActivityWorldView.class);
	}
	
	private Handler mHandler = new Handler();
	private Runnable mCountUpdater = new Runnable() 
	{
        public void run()
        {
           startActivity(mWorldViewIntent);
        }
    };
    
    protected void onResume() 
    {
    	super.onResume();
    	mHandler.postDelayed(mCountUpdater, delayTime);
    }

}
