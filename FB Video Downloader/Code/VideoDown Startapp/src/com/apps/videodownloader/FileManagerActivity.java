package com.apps.videodownloader;

import java.io.File;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
	
	private StartAppAd startAppAd = new StartAppAd(this); 

 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartAppAd.init(this, getString(R.string.startapp_dev_id), getString(R.string.startapp_app_id,false));
		setContentView(R.layout.filemanager_layout);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle("File Manager");
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		init_phone_video_grid();
		registerForContextMenu(videolist);
		
		startAppAd = new StartAppAd(this);
		StartAppAd.showSlider(this);
		
		startAppAd.loadAd(new AdEventListener() {

			@Override
			public void onReceiveAd(Ad arg0) {
				// TODO Auto-generated method stub
				startAppAd.showAd(); // show the ad
				startAppAd.loadAd(); // load the next ad
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0) {
				// TODO Auto-generated method stub

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