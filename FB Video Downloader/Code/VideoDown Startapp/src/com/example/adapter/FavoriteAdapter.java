package com.example.adapter;

import java.util.List;

import com.apps.videodownloader.R;
import com.example.favorite.DatabaseHandler;
import com.example.favorite.Pojo;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

 

public class FavoriteAdapter extends BaseAdapter {

	LayoutInflater inflate;
	Activity activity;
	private List<Pojo> data;
	String dburl;
	private DatabaseHandler db;
	private static final String TABLE_NAME = "Favorite";
	private static final String KEY_URL = "url";

	public FavoriteAdapter(List<Pojo> contactList, Activity activity)
	{
		this.activity=activity;
		this.data=contactList;
		inflate = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		db = new DatabaseHandler(activity);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	class GroupItem
	{

		public  TextView name;
		public  ImageView img_cancel;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi=null;
		final  GroupItem holder =new GroupItem();
		vi=inflate.inflate(R.layout.fav_item, null);

		holder.name=(TextView)vi.findViewById(R.id.text_fav);
		holder.img_cancel=(ImageView)vi.findViewById(R.id.image_cancel);
		dburl=data.get(position).getNameUrl();

		holder.img_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(activity, "click", Toast.LENGTH_SHORT).show();
				//db.RemoveFav(new Pojo(dburl));
				RemoveFav();


			}
		});

		holder.name.setText(data.get(position).getNameUrl().toString());

		return vi;
	}
	public void RemoveFav()
	{

		db.RemoveFav(new Pojo(dburl));
		Log.e("delete",""+dburl);

		Toast.makeText(activity, "Removed from Bookmark", Toast.LENGTH_SHORT).show();
		//menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.favourit));

	}
}

