package com.apps.videodownloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import com.apps.videodownloader.R;

public class SimpleEula {

	private String EULA_PREFIX = "eula_";
	private Activity mActivity; 

	public SimpleEula(Activity context) {
		mActivity = context; 
	}

	private PackageInfo getPackageInfo() {
		PackageInfo pi = null;
		try {
			pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi; 
	}

	public void show() {
		PackageInfo versionInfo = getPackageInfo();
		//checkShowTutorial();
		// the eulaKey changes every time you increment the version number in the AndroidManifest.xml
		final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
		if(hasBeenShown == false){

			// Show the Eula
			String title = mActivity.getString(R.string.help);

			//Includes the updates as well so users know what changed. 
			String message = mActivity.getString(R.string.Message);

			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton(R.string.Close, new Dialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					// Mark this version as read.
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean(eulaKey, true);
					editor.commit();
					dialogInterface.dismiss();
				}
			})
			.setNegativeButton(R.string.watch_tutorial, new Dialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Close the activity as they have declined the EULA
					//Toast.makeText(mActivity, "new actiivty", Toast.LENGTH_SHORT).show();
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean(eulaKey, true);
					editor.commit();
					dialog.dismiss();
					checkShowTutorial();

				}

			});
			builder.create().show();
		}
	}
	private void checkShowTutorial(){
		int oldVersionCode = T_PrefConstants.getAppPrefInt(mActivity, "version_code");
		int currentVersionCode = T_SAppUtil.getAppVersionCode(mActivity);

		if(currentVersionCode>oldVersionCode){
			mActivity.startActivity(new Intent(mActivity,T_ProductTourActivity.class));
			mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			T_PrefConstants.putAppPrefInt(mActivity, "version_code", currentVersionCode);
		}
	}
}
