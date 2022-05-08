package com.apps.videodownloader;
//// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
//// Jad home page: http://www.geocities.com/kpdus/jad.html
//// Decompiler options: braces fieldsfirst space lnc 
//
//package com.videodownloader;
//
//import android.app.Dialog;
//import android.app.DownloadManager;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import java.io.File;
//import java.util.EmptyStackException;
//
//// Referenced classes of package com.facebookvideodownloader.free:
////            StreamVideo
//
//public class DownloadDialog extends DialogFragment
//{
//
//	private static final String TAG_VIDURL = "video_url";
//
//	public DownloadDialog()
//	{
//	}
//
//	public void downloadFB(String s, String s1)
//	{
//		try
//		{
//			String s2 = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append(File.separator).append("FacebookVideos2").append(File.separator).toString();
//			if (!(new File(s2)).exists())
//			{
//				(new File(s2)).mkdir();
//			}
//			s1 = (new StringBuilder("file://")).append(s2).append("/").append(s1).append(".mp4").toString();
//			 
//			DownloadManager.Request it = new DownloadManager.Request(Uri.parse(s));
//			it.setDestinationUri(Uri.parse(s1));
//			it.setNotificationVisibility(1);
//
//			getActivity().getApplicationContext();
//			((DownloadManager)getActivity().getSystemService("download")).enqueue(it);
//			Toast.makeText(getActivity(), "Download Started", 0).show();
//			return;
//		}
//		// Misplaced declaration of an exception variable
//		catch (EmptyStackException bundleey)
//		{
//			Log.e("Error", bundleey.getMessage());
//
//		}
//		Toast.makeText(getActivity(), "Download Failed", 0).show();
//	}
//	public View onCreateView(LayoutInflater layoutinflater, final ViewGroup vidData, final Bundle vidID)
//	{
//		layoutinflater = layoutinflater.inflate(R.layout.download_alert, vidData, vidID);
//		getDialog().setTitle("Choose Option");
//		vidID = getArguments();
//		vidData = vidID.getString("vid_data");
//		vidID = vidID.getString("vid_id");
//		getFragmentManager().beginTransaction();
//		((Button)layoutinflater.findViewById(R.id.button_download)).setOnClickListener(new android.view.View.OnClickListener() {
//
//			final DownloadDialog this$0;
//			private final String val$vidData;
//			private final String val$vidID;
//
//			public void onClick(View view)
//			{
//				downloadFB(vidData, vidID);
//				getDialog().dismiss();
//			}
//
//
//			{
//				this$0 = DownloadDialog.this;
//				vidData = s;
//				vidID = s1;
//				super();
//			}
//		});
//		((Button)layoutinflater.findViewById(R.id.button_watch)).setOnClickListener(new android.view.View.OnClickListener() {
//
//			final DownloadDialog this$0;
//			private final String val$vidData;
//
//			public void onClick(View view)
//			{
//				streamFB(vidData);
//				getDialog().dismiss();
//			}
//
//
//			{
//				this$0 = DownloadDialog.this;
//				vidData = s;
//				super();
//			}
//		});
//		((Button)layoutinflater.findViewById(R.id.button_cancel)).setOnClickListener(new android.view.View.OnClickListener() {
//
//			final DownloadDialog this$0;
//
//			public void onClick(View view)
//			{
//				getDialog().dismiss();
//			}
//
//
//			{
//				this$0 = DownloadDialog.this;
//				super();
//			}
//		});
//		return layoutinflater;
//	}
//
//	public void streamFB(String s)
//	{
//		try
//		{
//			Intent intent = new Intent(getActivity().getApplicationContext(), StreamVideo.class);
//			intent.putExtra("video_url", s);
//			startActivity(intent);
//			Toast.makeText(getActivity(), "Streaming Started", 0).show();
//			return;
//		}
//		// Misplaced declaration of an exception variable
//		catch  
//		{
//
//		}
//		Toast.makeText(getActivity(), "Streaming Failed", 0).show();
//	}
//}
