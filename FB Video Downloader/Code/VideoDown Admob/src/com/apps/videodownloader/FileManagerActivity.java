package com.apps.videodownloader;

import java.io.File;
import java.util.Locale;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat.AdapterContextMenuInfo;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileManagerActivity extends ActionBarActivity {
	

	 
	Toolbar toolbar;
	private Cursor videocursor;
	private int video_column_index;
	ListView videolist;
	int count;
	String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
			MediaStore.Video.Thumbnails.VIDEO_ID };
	private AdView mAdView;
	private InterstitialAd mInterstitial;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filemanager_layout);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle("File Manager");
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		init_phone_video_grid();
		registerForContextMenu(videolist);
		
		// Look up the AdView as a resource and load a request.
		mAdView = (AdView) findViewById(R.id.adView);
		mAdView.loadAd(new AdRequest.Builder().build());
		
		mInterstitial = new InterstitialAd(FileManagerActivity.this);
		mInterstitial.setAdUnitId(getResources().getString(R.string.admob_intertestial_id));
		mInterstitial.loadAd(new AdRequest.Builder().build());


		mInterstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// TODO Auto-generated method stub
				super.onAdLoaded();
				if (mInterstitial.isLoaded()) {
					mInterstitial.show();
				}
			}
		});

		final File dir = new File(Environment.getExternalStorageDirectory()+"/Download/");

		MediaScannerConnection.scanFile(this, new String[] {

				dir.getAbsolutePath()},

				null, new MediaScannerConnection.OnScanCompletedListener() {

				public void onScanCompleted(String path, Uri uri)

				{


				}

				});
	}

	@SuppressWarnings("deprecation")
	private void init_phone_video_grid() {
		System.gc();
		String[] proj = { MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.SIZE };
		 String[] selectionArgs=new String[]{"%Download%"};
		videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				proj, MediaStore.Video.Media.DATA +" like?", selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
		count = videocursor.getCount();
		videolist = (ListView) findViewById(R.id.latest_grid);
		videolist.setAdapter(new VideoAdapter(getApplicationContext()));
		videolist.setOnItemClickListener(videogridlistener);
	}

	private OnItemClickListener videogridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			System.gc();
			video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
			videocursor.moveToPosition(position);
			String filename = videocursor.getString(video_column_index);
			Log.e("name", ""+filename);
			Uri intentUri = Uri.parse(filename);
			Intent intent = new Intent(Intent.ACTION_VIEW,intentUri);
			intent.setAction(Intent.ACTION_VIEW);
			//		        intent.setDataAndType(intentUri, 
			intent.setDataAndType(Uri.parse(filename), "video/*");
			//intent.setPackage("com.mxtech.videoplayer.ad");
			startActivity(intent);
		}
	};

	public class VideoAdapter extends BaseAdapter {
		private Context vContext;

		public VideoAdapter(Context c) {
			vContext = c;
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			System.gc();
			ViewHolder holder;
			String id = null;
			convertView = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(vContext).inflate(
						R.layout.listitem, parent, false);
				holder = new ViewHolder();
				holder.txtTitle = (TextView) convertView
						.findViewById(R.id.txtTitle);
				holder.txtSize = (TextView) convertView
						.findViewById(R.id.txtSize);
				holder.thumbImage = (ImageView) convertView
						.findViewById(R.id.imgIcon);

				video_column_index = videocursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
				videocursor.moveToPosition(position);
				id = videocursor.getString(video_column_index);
				video_column_index = videocursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
				videocursor.moveToPosition(position);
				// id += " Size(KB):" +
				 videocursor.getString(video_column_index);
				holder.txtTitle.setText(id);
			//	holder.txtSize.setText(" Size(KB):"+ videocursor.getString(video_column_index));

				String[] proj = { MediaStore.Video.Media._ID,
						MediaStore.Video.Media.DISPLAY_NAME,
						MediaStore.Video.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(
						MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj,
						MediaStore.Video.Media.DISPLAY_NAME + "=?",
						new String[] { id }, null);
				cursor.moveToFirst();
				long ids = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Video.Media._ID));

				ContentResolver crThumb = getContentResolver();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(
						crThumb, ids, MediaStore.Video.Thumbnails.MICRO_KIND,
						options);
				holder.thumbImage.setImageBitmap(curThumb);
				curThumb = null;

			} /*
			 * else holder = (ViewHolder) convertView.getTag();
			 */
			return convertView;
		}
	}

	static class ViewHolder {

		TextView txtTitle;
		TextView txtSize;
		ImageView thumbImage;
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.menu_main, menu);
//
//
//		return super.onCreateOptionsMenu(menu);
//	}
	
 

//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		// TODO Auto-generated method stub
//		super.onCreateContextMenu(menu, v, menuInfo);
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
//		int index = info.position;
//
//		getMenuInflater().inflate(R.menu.context_menu, menu);
//
//
//
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//
//
//		switch(item.getItemId()) {
//		case R.id.menu_rename:
//			int index = info.position;
//			video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
//			videocursor.moveToPosition(index);
//			final String oldName = videocursor.getString(video_column_index);
// 	
//			final Dialog dialog = new Dialog(FileManagerActivity.this);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//			// Include dialog.xml file
//			dialog.setContentView(R.layout.rename_alert);
//			// Set dialog title
//
//			dialog.show();
//
//			final EditText edtname=(EditText)dialog.findViewById(R.id.edt_vname);
//			Button btn=(Button)dialog.findViewById(R.id.button_ok);
//
//			edtname.setText(oldName);
//
//			btn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					final String newname;
//					newname=edtname.getText().toString();
//					final File dir = new File(Environment.getExternalStorageDirectory()+"/Download/");
//					 
//					if(dir.exists()){
//						File from = new File(dir,oldName);
//						File to = new File(dir,newname);
//						if(from.exists())
//							from.renameTo(to);
//						Log.e("yes", "yes");
//					}
//					 
//					videolist.setAdapter(new VideoAdapter(getApplicationContext()));
//					//Log.e("newnamechange", ""+newname);
//					dialog.dismiss();
//				}
//			});
//
//			return true;
//		case R.id.menu_delete:
//			int indexd = info.position;
//			video_column_index = videocursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
//			videocursor.moveToPosition(indexd);
//			final String oldNamede = videocursor.getString(video_column_index);
//			String my_Path = Environment.getExternalStorageDirectory()+"/Download/"+oldNamede;  
//			//final File dir = new File(Environment.getExternalStorageDirectory()+"Download");
//			Log.e("path", ""+my_Path);
//			File f = new File(my_Path);
//			Boolean deleted = f.delete();
//			
//			Log.e("delete", ""+deleted);
//
//			return true;
//
//
//
//		default:
//			return super.onContextItemSelected(item);
//		}
//	}

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