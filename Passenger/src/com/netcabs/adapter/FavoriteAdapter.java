package com.netcabs.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.netcabs.asynctask.FavouriteDestinationAsyncTask;
import com.netcabs.datamodel.FavoriteInfo;
import com.netcabs.interfacecallback.OnRequestComplete;
import com.netcabs.internetconnection.InternetConnectivity;
import com.netcabs.passenger.R;
import com.netcabs.passengerinfo.PassengerApp;
import com.netcabs.utils.ConstantValues;


public class FavoriteAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Activity context;
	private ArrayList<FavoriteInfo> favoriteList;
	private boolean isDestination = false;
	private String type = "0";

	public FavoriteAdapter(Activity context, ArrayList<FavoriteInfo> favoriteList, boolean isDestination) {
		this.context = context;
		this.favoriteList = favoriteList;
		this.isDestination = isDestination;
	}

	@Override
	public int getCount() {
		if(favoriteList != null) {
			return favoriteList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return favoriteList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
	
		if(isDestination){
			type = "2";
		}else{
			type = "1";
		}
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.favorite_row, null);
			holder = new ViewHolder();
			holder.txtViewLocationName = (TextView) convertView.findViewById(R.id.txt_view_location_name);
			holder.btnFavorite = (Button) convertView.findViewById(R.id.btn_favorite);
			holder.btnFavorite .setTag(position);
			
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(favoriteList.get(position).getIsFav() == 1) {
			holder.btnFavorite.setBackgroundResource(R.drawable.star_icon_s_y);
		} else {
			holder.btnFavorite.setBackgroundResource(R.drawable.star_icon_s_b);
		}
		
		holder.txtViewLocationName.setText(favoriteList.get(position).getLocationName().toString());
		
		holder.btnFavorite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final int index =((Integer) v.getTag());
				if(InternetConnectivity.isConnectedToInternet(context)) {
					if(favoriteList.get(index).getIsFav() == 1) {
						new FavouriteDestinationAsyncTask(context, new OnRequestComplete() {
							
							@Override
							public void onRequestComplete(String result) {
								if("2001".equals(result)) {
									favoriteList.get(index).setIsFav(0);
									holder.btnFavorite.setBackgroundResource(R.drawable.star_icon_s_b);
								} else if("3001".equals(result)) {
									
								} else if("4001".equals(result)) {
													
								} else {
									
								}
							}
						}).execute(ConstantValues.FUNC_ID_FAVOURITE_DESTINATION, PassengerApp.getInstance().getPassengerId(), favoriteList.get(index).getLocationLongitude(), favoriteList.get(index).getLocationLatitude(),favoriteList.get(index).getLocationName(), type, "0" );
					} else if (favoriteList.get(index).getIsFav() == 0) {
						new FavouriteDestinationAsyncTask(context, new OnRequestComplete() {
							
							@Override
							public void onRequestComplete(String result) {
								if("2001".equals(result)) {
									favoriteList.get(index).setIsFav(1);
									holder.btnFavorite.setBackgroundResource(R.drawable.star_icon_s_y);
								} else if("3001".equals(result)) {
									
								} else if("4001".equals(result)) {
													
								} else {
									
								}
							}
						}).execute(ConstantValues.FUNC_ID_FAVOURITE_DESTINATION, PassengerApp.getInstance().getPassengerId(), favoriteList.get(index).getLocationLongitude(), favoriteList.get(index).getLocationLatitude(), favoriteList.get(index).getLocationName(), type, "1" );
			
					}
				}
				else {
					Toast.makeText(context, ConstantValues.INTERNET_CONNECTION_FAILURE_MSG, Toast.LENGTH_SHORT).show();
				}
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView txtViewLocationName;
		Button btnFavorite;
	}

}
