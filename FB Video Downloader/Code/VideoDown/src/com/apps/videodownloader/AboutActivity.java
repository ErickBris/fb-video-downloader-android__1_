package com.apps.videodownloader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import com.apps.videodownloader.R;
 
public class AboutActivity extends ActionBarActivity {

	WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		Toolbar toolbar = (android.support.v7.widget.Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle(getString(R.string.menu_about));
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		webview = (WebView)findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("file:///android_asset/index.html");

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
		switch (menuItem.getItemId()) 
		{
		case android.R.id.home: 
			onBackPressed();
			break;

		default:
			return super.onOptionsItemSelected(menuItem);
		}
		return true;
	}

}
