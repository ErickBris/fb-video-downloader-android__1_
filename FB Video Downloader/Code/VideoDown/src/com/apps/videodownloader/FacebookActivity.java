package com.apps.videodownloader;

import java.io.File;
import java.util.EmptyStackException;
import com.apps.videodownloader.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

 
public class FacebookActivity extends ActionBarActivity {

	//private static final String target_url = "https://m.facebook.com/";
	Toolbar toolbar;
	private static WebView webView; 
	String vid_data;
	ProgressBar bar;
	int check = 0;
	private AdView mAdView;
//	private InterstitialAd mInterstitial;
//	private InterstitialAd mInterstitialtime;
	private StartAppAd startAppAd = new StartAppAd(this); 
	
	SharedPreferences prefs;
	private CountDownTimer countDownTimer;
	private boolean timerHasStarted = false;
	private final long startTime = 60 * 1000;
	private final long interval = 1 * 1000;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartAppAd.init(this, getString(R.string.startapp_dev_id), getString(R.string.startapp_app_id,false));
		setContentView(R.layout.webview_layout);
	 
		new SimpleEula(this).show(); 
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle("FB Video Downloader");
		this.setSupportActionBar(toolbar);
		
		countDownTimer = new MyCountDownTimer(startTime, interval);

		if (!timerHasStarted) {
			countDownTimer.start();
			timerHasStarted = true;
			//startB.setText("STOP");
		} else {
			countDownTimer.cancel();
			timerHasStarted = false;
			//  startB.setText("RESTART");
		}
		
		//Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
		
		webView = (WebView) findViewById(R.id.webView_web);
		bar = (ProgressBar) findViewById(R.id.load);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new FBDownloader(FacebookActivity.this), "FBDownloader");
		
		// Look up the AdView as a resource and load a request.
		mAdView = (AdView) findViewById(R.id.adView);
		mAdView.loadAd(new AdRequest.Builder().build());
		
		startAppAd = new StartAppAd(this);
		StartAppAd.showSlider(this);
		
		startAppAd.loadAd(new AdEventListener() {

			@Override
			public void onReceiveAd(Ad arg0) {
				// TODO Auto-generated method stub
				startAppAd.loadAd(); // load the next ad
				startAppAd.showAd(); // show the ad
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0) {
				// TODO Auto-generated method stub

			}
		});
		
//		mInterstitial = new InterstitialAd(FacebookActivity.this);
//		mInterstitial.setAdUnitId(getResources().getString(R.string.admob_intertestial_id));
//		mInterstitial.loadAd(new AdRequest.Builder().build());
//
//
//		mInterstitial.setAdListener(new AdListener() {
//			@Override
//			public void onAdLoaded() {
//				// TODO Auto-generated method stub
//				super.onAdLoaded();
//				if (mInterstitial.isLoaded()) {
//					mInterstitial.show();
//				}
//			}
//		});
		
		webView.setWebViewClient(new WebViewClient() {

			public void onLoadResource(WebView webview, String s)
			{
				webView.loadUrl("javascript:(function prepareVideo() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;console.log(i);var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');}}})()");
				webView.loadUrl("javascript:( window.onload=prepareVideo;)()");
			}

			public void onPageFinished(WebView webview, String s)
			{
				webView.loadUrl("javascript:(function() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\");');}}})()");
				Log.e("WEBVIEWFIN", s);
			}



		});
		
		
 	webView.setOnKeyListener(new View.OnKeyListener() {
 			@Override
 			public boolean onKey(View v, int keyCode, KeyEvent event) {
 				if (event.getAction() == KeyEvent.ACTION_DOWN) {
 					WebView webView = (WebView) v;
 
 					switch (keyCode) {
 					case KeyEvent.KEYCODE_BACK:
 						if (webView.canGoBack()) {
 							webView.goBack();
 							return true;
 						}
 						break;
 					}
 				}
 
 				return false;
 			}
 		});

 	webView.setWebChromeClient(new WebChromeClient() {
 
 			@Override
 			public void onProgressChanged(WebView view, int newProgress) {
 				// TODO Auto-generated method stub
 				super.onProgressChanged(view, newProgress);
 
 				if (newProgress == 100) {
 					bar.setVisibility(View.GONE);
 
 				} else {
 					bar.setVisibility(View.VISIBLE);
 					//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
 					//					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
 				}
 
 
 			}
 
 		});
		
		CookieSyncManager.createInstance(FacebookActivity.this);
		CookieManager.getInstance().setAcceptCookie(true);
		CookieSyncManager.getInstance().startSync();
		webView.loadUrl("https://m.facebook.com/");
		 
	}



	public class FBDownloader {
		Context mContext;


		FBDownloader(Context c) {
			mContext = c;
		}


		@JavascriptInterface
		public void processVideo(String s, String s1)
		{
			//Log.e("vid_data", s);
			//Log.e("vid_id", s1);
			final String vid_data=s;
			final String vid_id=s1;
			
			
 		final Dialog dialog = new Dialog(FacebookActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			// Include dialog.xml file
			dialog.setContentView(R.layout.download_alert);
			// Set dialog title
	
			dialog.show();
	
			Button btn_watch=(Button)dialog.findViewById(R.id.button_watch);
			Button btn_download=(Button)dialog.findViewById(R.id.button_download);
			Button btn_cancel=(Button)dialog.findViewById(R.id.button_cancel);
	
			btn_watch.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				 
					
					streamFB(vid_data);
					Toast.makeText(FacebookActivity.this, "Streaming Started", 0).show();
					dialog.dismiss();
				}
			});
	
			btn_download.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					FacebookActivity.this.runOnUiThread(new Runnable() {
				        @Override public void run() {
				        	
							startAppAd.loadAd(); // load the next ad
							startAppAd.showAd(); // show the ad
				        	
//				            if (mInterstitial.isLoaded()) {
//				            	mInterstitial.show();
//				         }
				        }
				    });
					downloadFB(vid_data, vid_id);
					dialog.dismiss();
				}
			});
	
			btn_cancel.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					dialog.dismiss();
				}
			});
		}
	}

 	public void downloadFB(String s, String s1)
		{
 		
 		
			try
			{
				String s2 = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append(File.separator).append("Download").append(File.separator).toString();
				if (!(new File(s2)).exists())
				{
					(new File(s2)).mkdir();
				}
				s1 = (new StringBuilder("file://")).append(s2).append("/").append(s1).append(".mp4").toString();
	
				DownloadManager.Request it = new DownloadManager.Request(Uri.parse(s));
				it.setDestinationUri(Uri.parse(s1));
				it.setNotificationVisibility(1);
	
				FacebookActivity.this.getApplicationContext();
				((DownloadManager)FacebookActivity.this.getSystemService("download")).enqueue(it);
				Toast.makeText(FacebookActivity.this, "Added to Download Queue", 0).show();
				return;
			}
			// Misplaced declaration of an exception variable
			catch (EmptyStackException bundleey)
			{
				Log.e("Error", bundleey.getMessage());
	
			}
			Toast.makeText(FacebookActivity.this, "Download Failed", 0).show();
		}
		
		public void streamFB(String s)
		{
			try
			{
				Intent intent = new Intent(FacebookActivity.this.getApplicationContext(), StreamVideo.class);
				intent.putExtra("video_url", s);
				startActivity(intent);
				Toast.makeText(FacebookActivity.this, "Streaming Started", 0).show();
				return;
			}
			// Misplaced declaration of an exception variable
			catch (EmptyStackException bundleey)
			{
				Log.e("Error", bundleey.getMessage());
			Toast.makeText(FacebookActivity.this, "Streaming Failed", 0).show();
		}
	}
		
		private void checkShowTutorial(){
			int oldVersionCode = FT_PrefConstants.getAppPrefInt(FacebookActivity.this, "version_code");
			int currentVersionCode = FT_SAppUtil.getAppVersionCode(FacebookActivity.this);

			if(currentVersionCode>oldVersionCode){
				FacebookActivity.this.startActivity(new Intent(FacebookActivity.this,FT_ProductTourActivity.class));
				FacebookActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				FT_PrefConstants.putAppPrefInt(FacebookActivity.this, "version_code", currentVersionCode);
			}
		}
		
 
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public static void downloadFile(final Context context, final String url,
				final String contentDisposition, final String mimetype, String fileNamee) {
			try {
				DownloadManager download = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
				Uri nice = Uri.parse(url);
				DownloadManager.Request it = new DownloadManager.Request(nice);
				// fileName = URLUtil.guessFileName(url,contentDisposition, mimetype);

				Log.e("filename", ""+fileNamee);

				it.setTitle(fileNamee);
				it.setDescription(url);
				if (android.os.Build.VERSION.SDK_INT >= 11) {
					it.allowScanningByMediaScanner();
					it.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				}
				String location = context.getSharedPreferences("settings",0).getString("download",
						Environment.DIRECTORY_DOWNLOADS);
				Log.e("location", ""+location);
				it.setDestinationInExternalPublicDir(location, fileNamee);
				Log.i("Barebones", "Downloading" + fileNamee);
				download.enqueue(it);

			} catch (NullPointerException e) {
				Log.e("Barebones", "Problem downloading");
				Toast.makeText(context, "Error Downloading File",
						Toast.LENGTH_SHORT).show();
			} catch (IllegalArgumentException e) {
				Log.e("Barebones", "Problem downloading");
				Toast.makeText(context, "Error Downloading File",
						Toast.LENGTH_SHORT).show();
			} catch (SecurityException ignored) {

			}
		}

		//Checking for an internet connection
		private boolean checkConnectivity()
		{
			boolean enabled = true;

			ConnectivityManager connectivityManager = (ConnectivityManager) FacebookActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();

			if ((info == null || !info.isConnected() || !info.isAvailable()))
			{

				//setting the dialog title with custom color theme
				TextView title = new TextView(FacebookActivity.this);
				title.setBackgroundColor(title.getContext().getResources().getColor(R.color.theme));
				title.setTextColor(title.getContext().getResources().getColor(R.color.black));
				title.setText(getString(R.string.error));
				title.setTextSize(23f);
				//title.setLayoutParams(ViewGroup.MATCH_PARENT);
				title.setPadding(20, 15, 10, 15);

				enabled = false;
				Builder builder = new Builder(FacebookActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setMessage(getString(R.string.noconnection));
				builder.setCancelable(false);
				builder.setNeutralButton(R.string.ok, null);
				builder.setCustomTitle(title);
				builder.create().show();		
			}
			return enabled;			
		}
 		
		
		public class MyCountDownTimer extends CountDownTimer {
			public MyCountDownTimer(long startTime, long interval) {
				super(startTime, interval);
			}

			@Override
			public void onFinish() {
				
				
				startAppAd.loadAd(new AdEventListener() {

					@Override
					public void onReceiveAd(Ad arg0) {
						// TODO Auto-generated method stub
						startAppAd.loadAd(); // load the next ad
						startAppAd.showAd(); // show the ad
						countDownTimer.start();
					}

					@Override
					public void onFailedToReceiveAd(Ad arg0) {
						// TODO Auto-generated method stub

					}	 

				});

//				mInterstitialtime = new InterstitialAd(FacebookActivity.this);
//				mInterstitialtime.setAdUnitId(getResources().getString(R.string.admob_intertestial_id));
//				mInterstitialtime.loadAd(new AdRequest.Builder().build());
//				Log.e("mInterstitial", ""+mInterstitialtime);
//				mInterstitialtime.setAdListener(new AdListener() {
//					@Override
//					public void onAdLoaded() {
//						// TODO Auto-generated method stub
//						super.onAdLoaded();
//						if (mInterstitialtime.isLoaded()) {
//							mInterstitialtime.show();
//						}
//
//					}
//
//					@Override
//					public void onAdClosed() {
//						// TODO Auto-generated method stub
//						super.onAdClosed();
//						countDownTimer.start();
//					}
//
//				});

			}

			@Override
			public void onTick(long millisUntilFinished) {
				//text.setText("" + millisUntilFinished / 1000);
			}

		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {

			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_main, menu);


			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem menuItem)
		{       
			switch (menuItem.getItemId()) 
			{
			
			case R.id.about:
				
				Intent about = new Intent(FacebookActivity.this,AboutActivity.class);
				startActivity(about);
				
				return true;
				
			case R.id.Download:
				
				Intent fmIntent = new Intent(FacebookActivity.this,FileManagerActivity.class);
				startActivity(fmIntent);
				
				return true;
			
			case R.id.more_app: 
				
 				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse(getString(R.string.play_more_apps))));
				
				break;
				
			case R.id.share: 
				
				 
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "I Would like to share this with you. Here You Can Download This Application from PlayStore " + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName());
				sendIntent.setType("text/plain");			 
				startActivity(sendIntent);
				
				break;

			default:
				return super.onOptionsItemSelected(menuItem);
			}
			return true;
		}

	 	@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_BACK) {
				// Toast.makeText(appContext, "BAck", Toast.LENGTH_LONG).show();
				AlertDialog.Builder alert = new AlertDialog.Builder(
						FacebookActivity.this);
				alert.setTitle(getString(R.string.app_name));
				alert.setIcon(R.drawable.app_icon);
				alert.setMessage("Are You Sure You Want To Quit?");

				alert.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						countDownTimer.cancel();
						finish();
					}


				});

				alert.setNegativeButton("Rate App",
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						final String appName = getPackageName();//your application package name i.e play store application url
						try {
							startActivity(new Intent(Intent.ACTION_VIEW,
									Uri.parse("market://details?id="
											+ appName)));
						} catch (android.content.ActivityNotFoundException anfe) {
							startActivity(new Intent(
									Intent.ACTION_VIEW,
									Uri.parse("http://play.google.com/store/apps/details?id="
											+ appName)));
						}

					}
				});
				alert.show();
				return true;
			}

			return super.onKeyDown(keyCode, event);

		}
		
}

