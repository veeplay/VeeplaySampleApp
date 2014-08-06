package com.appscend.vastdemotest;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.appscend.media.APSMediaBuilder;
import com.appscend.media.APSMediaPlayer;
import com.appscend.utilities.VPUtilities;

public class FullscreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		APSMediaBuilder builder = new APSMediaBuilder();
		try {
			builder.configureFromURL(new URL("JSON URL"));
			builder.debugMode=true;
		} catch (MalformedURLException e) {
			builder=null;
			e.printStackTrace();
		}
		if(builder!=null) {
			
			APSMediaPlayer.getInstance().init(this, true);
			APSMediaPlayer.getInstance().setSize(VPUtilities.getWidth(), VPUtilities.getHeight());
			APSMediaPlayer.getInstance().restrictRootedAccess = false;
			RelativeLayout layout = new RelativeLayout(this);
			layout.setBackgroundColor(Color.WHITE);
			layout.addView(APSMediaPlayer.getInstance().viewController());
			setContentView(layout);

			APSMediaPlayer.getInstance().playMediaUnits(builder.mediaUnits());
			//APSMediaPlayer.getInstance().toggleFullscreen();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		APSMediaPlayer.getInstance().pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		APSMediaPlayer.getInstance().resumePlay();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		APSMediaPlayer.getInstance().performPostTapAction();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		APSMediaPlayer.getInstance().finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//Log.d("VastPlayer","cf changed: "+newConfig.orientation+" "+Configuration.ORIENTATION_LANDSCAPE);
		// WHEN orientation changes, resize the whole player, and then recompute the correct size of the video
		APSMediaPlayer.getInstance().setSize(VPUtilities.getWidth(), VPUtilities.getHeight());
		APSMediaPlayer.getInstance().computeSurfaceSize();
		APSMediaPlayer.getInstance().orientationWillChange();
	}

}
