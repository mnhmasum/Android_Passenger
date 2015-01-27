package com.netcabs.fragments;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.netcabs.adapter.TaxiAdapter;
import com.netcabs.asynctask.GetDurationAsyncTask;
import com.netcabs.database.PreferenceUtil;
import com.netcabs.datamodel.LocationSearchInfo;
import com.netcabs.datamodel.PickUpInfo;
import com.netcabs.datamodel.TaxiInfo;
import com.netcabs.interfacecallback.OnRequestComplete;
import com.netcabs.internetconnection.InternetConnectivity;
import com.netcabs.latlon.GPSTracker;
import com.netcabs.passenger.ConfirmBookingActivity;
import com.netcabs.passenger.MainMenuActivity;
import com.netcabs.passenger.PickUpLocationSearchActivity;
import com.netcabs.passenger.R;
import com.netcabs.passengerinfo.PassengerApp;
import com.netcabs.utils.ConstantValues;

public class MapFragment extends Fragment implements OnItemClickListener, OnClickListener, OnMarkerClickListener {
	
	private TextView txtView; 
	private Button btnMap;
	private Button btnSearchPickUpLocation;
	private Button btnTaxiList;
	private ListView lstViewTaxiList;
	private Button btnBookNow;
	private View view;
	private int tabIndex = 0;
	private RelativeLayout relativeMap;
	private TaxiAdapter taxiAdapter;
	private AsyncTask<Void, Void, Void> geoAddressAsyncTask;
	private LinearLayout linearLayoutTitle;
	private LinearLayout linearLayoutAddress;
	
	// Map integration
	private double mcurrent_lat;
	private double mcurrent_lon;
	private MarkerOptions markerOptions;
	public static GoogleMap googleMap;
	private MapView mapView;
	private RelativeLayout relativeLayout;
	
	Marker lastOpenned = null;
	
	public Intent playerService;
	private Address searchedlocation;
	private Intent intent;
	private MainMenuActivity mainMenuActivity;
	private Geocoder gcd;
	
	private TextView txtViewMarkerTaxiNameByTag;
	int taxiPosition = 0;
	
	
	
	private HashMap<Marker, TaxiInfo> mMarkersHashMap;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_map, null);
		mainMenuActivity = (MainMenuActivity) getActivity();
		
		try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
        	
        }
		
		mapView = (MapView) view.findViewById(R.id.map_view);
		mapView.onCreate(savedInstanceState);
		
		initMap();
		initViews();
		setListener();
		loadData();
		
		intent = new Intent(getActivity(), com.netcabs.servicehttppost.BroadcastService.class);
		return view;
	}
	
	private void updateUI(Intent intent) {
		LatLng userLatLng = null;
		mMarkersHashMap = new HashMap<Marker, TaxiInfo>();
		
		googleMap.clear();
    	String status = intent.getStringExtra("status"); 
    	//LatLng 
    	if(PassengerApp.getInstance().getSearchInfo() != null) {
    		if(PassengerApp.getInstance().getSearchInfo() != null){
    			userLatLng = new LatLng(PassengerApp.getInstance().getSearchInfo().getLocationLatitude(), PassengerApp.getInstance().getSearchInfo().getLocationLongitude());
    			   
    		} else {
    			userLatLng = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
    		}
    	} else {
			userLatLng = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
    	}
    	Log.e("my lat lon", "---"+userLatLng.latitude+ ":::"+userLatLng.longitude);
		
    	if (status.equals("2001")) {
			if (PassengerApp.getInstance().getTaxiInfoList() != null) {
				//taxiAdapter = new TaxiAdapter(getActivity(), PassengerApp.getInstance().getTaxiInfoList());
	    		for (final TaxiInfo info : PassengerApp.getInstance().getTaxiInfoList()) {
	        		final LatLng cur_Latlng = new LatLng(info.getTaxiLat(), info.getTaxiLon());
	        		
	        		Log.e("lat & lon is:", "----"+ cur_Latlng.latitude +"::"+cur_Latlng.longitude);
	        		if(userLatLng != null) {
		        		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		        			if(InternetConnectivity.isConnectedToInternet(getActivity())) {
			        			new GetDurationAsyncTask(getActivity(), userLatLng, cur_Latlng,  new OnRequestComplete() {
			        				
			        				@Override
			        				public void onRequestComplete(String result) {
			        					Log.e("Duration is:", "::::"+result);
			        					String [] distanceDuration = result.split("//");
			        					
			        					if (PassengerApp.getInstance().getTaxiInfoList() != null) {
			        						
			        						//PassengerApp.getInstance().getTaxiInfoList().get(index).setTimeDurationToReach(distanceDuration[1].toString());
			        						if(PassengerApp.getInstance().getTaxiInfoList().size() > 0) {
	//		        							if(info.getTaxiLat() == cur_Latlng.latitude && info.getTaxiLon() == cur_Latlng.longitude) {
	//		        								info.setTimeDurationToReach(distanceDuration[1].toString());
	//		        							}
			        							
			        							info.setTimeDurationToReach(distanceDuration[1].toString());
			        						}
			        					}
			        					
			        					Log.e("I am here with distance", "---------" + distanceDuration[1]);    
			        					
			        		      		//Here Window
			        				}
			        			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			        		} else {
			        			Toast.makeText(getActivity(), ConstantValues.INTERNET_CONNECTION_FAILURE_MSG, Toast.LENGTH_SHORT).show();
			        		}
		        		} else {
		        			if(InternetConnectivity.isConnectedToInternet(getActivity())) {
			        			new GetDurationAsyncTask(getActivity(), userLatLng, cur_Latlng,  new OnRequestComplete() {
			        				
			        				@Override
			        				public void onRequestComplete(String result) {
			        					Log.e("Duration is:", "::::"+result);
			        					String [] distanceDuration = result.split("//");
			        					
			        					if (PassengerApp.getInstance().getTaxiInfoList() != null) {
			        						
			        						//PassengerApp.getInstance().getTaxiInfoList().get(index).setTimeDurationToReach(distanceDuration[1].toString());
			        						if(PassengerApp.getInstance().getTaxiInfoList().size() > 0){
	//		        							if(info.getTaxiLat() == cur_Latlng.latitude && info.getTaxiLon() == cur_Latlng.longitude) {
	//		        								info.setTimeDurationToReach(distanceDuration[1].toString());
	//		        							}
			        							
			        							info.setTimeDurationToReach(distanceDuration[1].toString());
			        						}
			        					}
			        					
			        					Log.e("I am here with distance", "---------" + distanceDuration[1]);    
			        					
			        		      		//Here Window
			        				}
			        			}).execute("");
			        		} else {
			        			Toast.makeText(getActivity(), ConstantValues.INTERNET_CONNECTION_FAILURE_MSG, Toast.LENGTH_SHORT).show();
			        		}
		        		}
	        		}
	        		
	        		
	        		
	        		MarkerOptions markerOption = new MarkerOptions().position(cur_Latlng);
	                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon_g));

	                Marker currentMarker = googleMap.addMarker(markerOption);
	                mMarkersHashMap.put(currentMarker, info);

	                googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
	        		
	        		
	    		} //end for loop
	    		
	    		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
   				 
					@Override
					public boolean onMarkerClick(Marker marker) {
						marker.showInfoWindow();
						return true;
					}
    	        });
	    		
	    		
    		} else {
    			//When Taxi list is null
			}
    		
    	} else {
    		// When status is not 2001
    	}
    	
    }
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateUI(intent);
		}
	};
	
	private void initMap() {
		
		//mMarkersHashMap = new HashMap<Marker, TaxiInfo>();
		
		gcd = new Geocoder(getActivity(), Locale.getDefault());
		mapView = (MapView) view.findViewById(R.id.map_view);
		
		GPSTracker gps = new GPSTracker(getActivity());
		mcurrent_lat = gps.getLatitude();
		mcurrent_lon = gps.getLongitude();
		
		final LatLng cur_Latlng = new LatLng(mcurrent_lat, mcurrent_lon);
		googleMap = mapView.getMap();
	
		googleMap.setMyLocationEnabled(true);
		//googleMap.setTrafficEnabled(true);
		//googleMap.set
		PassengerApp.getInstance().setGmap(googleMap);
		
		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			
			@Override
			public void onCameraChange(CameraPosition arg0) {
				geoAddressAsyncTask = new GeoAddressAsyncTask(arg0.target.latitude, arg0.target.longitude).execute();
				//googleMap.clear();
				Log.i("Value is on MAP", "________" + arg0.target);

			}
			
		});
		
		if(googleMap != null) {
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
			
		} else {
			Log.i("Map is null", "_________________");
		}
		
	}

	private void loadData() {
		btnSearchPickUpLocation.setBackgroundResource(R.drawable.change_btn);
		btnSearchPickUpLocation.setVisibility(View.VISIBLE);
		PassengerApp.getInstance().setPassengerId(new PreferenceUtil(getActivity()).getUserID());
		if(new PreferenceUtil(getActivity()).getLOGIN_TYPES() != null) {
			PassengerApp.getInstance().setLogInType(Integer.parseInt(new PreferenceUtil(getActivity()).getLOGIN_TYPES()));
		}
	}

	private void setListener() {
		relativeLayout.setOnClickListener(this);
		lstViewTaxiList.setOnItemClickListener(this);
		btnMap.setOnClickListener(this);
		btnTaxiList.setOnClickListener(this);
		btnBookNow.setOnClickListener(this);
		btnSearchPickUpLocation.setOnClickListener(this);
		
		googleMap.setOnMarkerClickListener(this);
	}

	private void initViews() {
		linearLayoutTitle =(LinearLayout)getActivity().findViewById(R.id.liner_layout_title);
		linearLayoutAddress =(LinearLayout)getActivity().findViewById(R.id.liner_layout_address);
		linearLayoutTitle.setVisibility(View.INVISIBLE);
		linearLayoutAddress.setVisibility(View.VISIBLE);
		
		relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_main);
		txtView = (TextView) getActivity().findViewById(R.id.txt_view_address);
		txtView.setText("Location Searching");
		
		btnMap = (Button) view.findViewById(R.id.btn_map);
		btnTaxiList = (Button) view.findViewById(R.id.btn_list);
		
		lstViewTaxiList = (ListView) view.findViewById(R.id.lst_view_taxi);
		btnBookNow = (Button) view.findViewById(R.id.btn_book_now);
		
		btnSearchPickUpLocation = (Button) getActivity().findViewById(R.id.btn_search_location_name);
		
		relativeMap = (RelativeLayout) view.findViewById(R.id.relative_map);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relative_main:
			PassengerApp.getInstance().hideKeyboard(getActivity(), v);
			break;
			
		case R.id.btn_book_now:
			if(!InternetConnectivity.isConnectedToInternet(getActivity())) {
				Toast.makeText(getActivity(), ConstantValues.INTERNET_CONNECTION_FAILURE_MSG, Toast.LENGTH_SHORT).show();
				return;
			} 
			
			if(PassengerApp.getInstance().getTaxiInfoList() != null) {
				if(PassengerApp.getInstance().getSearchInfo() != null) {
					PickUpInfo pickUpInfo = new PickUpInfo();
					pickUpInfo.setLocationName(PassengerApp.getInstance().getSearchInfo().getLocationName());
					pickUpInfo.setLocationLatitude(PassengerApp.getInstance().getSearchInfo().getLocationLatitude());
					pickUpInfo.setLocationLongitude(PassengerApp.getInstance().getSearchInfo().getLocationLongitude());
					PassengerApp.getInstance().setPickUpLocationInfo(pickUpInfo);
					mainMenuActivity.isLock = true;
					startActivity(new Intent(getActivity(), ConfirmBookingActivity.class));
				} else {
					Toast.makeText(getActivity(), "Address can not be retrived from geo location", Toast.LENGTH_SHORT).show();
				}
				
			} else {
				Toast.makeText(getActivity(), "There are no drivers available", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.btn_map:
			if(tabIndex != 1) {
				tabIndex = 1;
				btnMap.setBackgroundResource(R.drawable.map_btn_y);
				btnTaxiList.setBackgroundResource(R.drawable.list_btn_w);
				lstViewTaxiList.setVisibility(View.INVISIBLE);
				relativeMap.setVisibility(View.VISIBLE);
			}
			break;
			
		case R.id.btn_list:
			if(tabIndex != 2) {
				tabIndex = 2;
				btnMap.setBackgroundResource(R.drawable.map_btn_w);
				btnTaxiList.setBackgroundResource(R.drawable.list_btn_y);
				lstViewTaxiList.setVisibility(View.VISIBLE);
				relativeMap.setVisibility(View.INVISIBLE);
				
				if(PassengerApp.getInstance().getTaxiInfoList() != null){
					taxiAdapter = new TaxiAdapter(getActivity(), PassengerApp.getInstance().getTaxiInfoList());
					lstViewTaxiList.setAdapter(taxiAdapter);
				}else{
					lstViewTaxiList.setAdapter(null);
				}
				
/*				if(PassengerApp.getInstance().getSearchInfo() != null) {
//					if(taxiAdapter != null) {
//					//	taxiAdapter.notifyDataSetChanged();
//						taxiAdapter = new TaxiAdapter(getActivity(), PassengerApp.getInstance().getTaxiInfoList());
//						lstViewTaxiList.setAdapter(taxiAdapter);
//					} else {
					if(PassengerApp.getInstance().getTaxiInfoList() != null){
						taxiAdapter = new TaxiAdapter(getActivity(), PassengerApp.getInstance().getTaxiInfoList());
						lstViewTaxiList.setAdapter(taxiAdapter);
					}else{
						lstViewTaxiList.setAdapter(null);
					}
					//}
				}*/
				
			}
			break;
			
		case R.id.btn_search_location_name:
			mainMenuActivity.isLock = true;
			Intent intent = new Intent(getActivity(), PickUpLocationSearchActivity.class).putExtra("SEARCH_TYPE", 0);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	@Override
    public void onResume() {
        super.onResume();
        initMap();
        initViews();
        mapView.onResume();
        if(PassengerApp.getInstance().getSearchInfo() != null) {
        	if(googleMap != null) {
        		googleMap.clear();
		    	LatLng latLng = new LatLng(PassengerApp.getInstance().getSearchInfo().getLocationLatitude(), PassengerApp.getInstance().getSearchInfo().getLocationLongitude());
			    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				googleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
			    txtView.setText(PassengerApp.getInstance().getSearchInfo().getLocationName());
			    Log.e("lat , long & place name", "--------"+ PassengerApp.getInstance().getSearchInfo().getLocationLatitude()+":::"+ PassengerApp.getInstance().getSearchInfo().getLocationLongitude()+"::::"+PassengerApp.getInstance().getSearchInfo().getLocationName());
			  //  PassengerApp.getInstance().setSearchInfo(null);
        	}
        } else {
        	 Log.e("lat , long & place name", "--------i am in else");
        }
        
        /* Start Broadcast Service */
        if(InternetConnectivity.isConnectedToInternet(getActivity())) {
        	getActivity().startService(intent);
    		getActivity().registerReceiver(broadcastReceiver, new IntentFilter(com.netcabs.servicehttppost.BroadcastService.BROADCAST_ACTION));
        } else {
        	Toast.makeText(getActivity(), ConstantValues.INTERNET_CONNECTION_FAILURE_MSG, Toast.LENGTH_SHORT).show();
        }
    }
	
	private class GeoAddressAsyncTask extends AsyncTask<Void, Void, Void> {
		double Lat, Lon;
		List<Address> addresses = null;
		
		public GeoAddressAsyncTask(double Lat, double Lon) {
			this.Lat = Lat;
			this.Lon = Lon;
		}
	
		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				addresses = gcd.getFromLocation(Lat, Lon, 1);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i("Value found", "________________");
			if (addresses != null) {
				if (addresses.size() > 0) {
					Log.i("Value found", "________________");
					LocationSearchInfo info = new LocationSearchInfo();
					String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
//					String address = "";
//					for(int i = 0; i<addresses.get(0).getMaxAddressLineIndex(); i++){
//						if(address.equals("")){
//							address = addresses.get(0).getAddressLine(i);
//						} else {
//							address = address+", "+addresses.get(0).getAddressLine(i);
//						}
//						
//					}
					String addressWithoutNullValue = address.replace("null", "");
					Log.e("Address is 1", "" + addressWithoutNullValue);
					String formatedAddress = "";
					formatedAddress = addressWithoutNullValue.replace(", ,", ",");
					Log.e("Address is 2", "" + formatedAddress);
					if (formatedAddress.length() > 0 && formatedAddress.charAt(formatedAddress.length()-1)==',') {
						formatedAddress = formatedAddress.substring(0, formatedAddress.length()-2);
					}
					
					info.setLocationName(formatedAddress);
					//info.setLocationName(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getSubLocality() + ", " + addresses.get(0).getAdminArea());
					
					info.setLocationLatitude(Lat);
					info.setLocationLongitude(Lon);
					PassengerApp.getInstance().setSearchInfo(info);
					txtView.setText(formatedAddress);
					//txtView.setText(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getSubLocality() + ", " + addresses.get(0).getAdminArea());
					
				} else {
					Log.i("Value is null", "______empty__________");
				}
				   
			} else {
				Log.i("Value is null", "________________");
			}
			
		}
	}
	
	
    @Override
    public void onPause() {
        super.onPause();
      // geoAddressAsyncTask.cancel(true);
        try {
        	getActivity().unregisterReceiver(broadcastReceiver);
    		getActivity().stopService(intent);
            mapView.onPause();
        } catch (Exception e) {
        	Log.i("ServiceMainMap","****" + e.getMessage());
        }
        
    }

    @Override
    public void onDestroy() {
    	//getActivity().stopService(playerService);
    	try {
    		getActivity().unregisterReceiver(broadcastReceiver);
    		getActivity().stopService(intent);
    	} catch (Exception e) {
    		Log.i("ServiceMainMap","****" + e.getMessage());
    	}
    	
    	mapView.onDestroy();
        super.onDestroy();
    }

	@Override
	public boolean onMarkerClick(Marker arg0) {
		if (lastOpenned != null) {
	        // Close the info window
	        lastOpenned.hideInfoWindow();

	        // Is the marker the same marker that was already open
	        if (lastOpenned.equals(arg0)) {
	            // Nullify the lastOpenned object
	            lastOpenned = null;
	            // Return so that the info window isn't openned again
	            return true;
	        } 
	    }

	    // Open the info window for the marker
		arg0.showInfoWindow();
	    // Re-assign the last openned such that we can close it later
	    lastOpenned = arg0;

	    // Event was handled by our code do not launch default behaviour.
	    return true;
	}
	
	public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        	
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
        	LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = mInflater.inflate(R.layout.info_window_row, null);
            
            TaxiInfo info = mMarkersHashMap.get(marker);
            
            TextView txtViewMarkerLabel1 = (TextView) v.findViewById(R.id.marker_label1);
            TextView txtViewMarkerLabel2  = (TextView) v.findViewById(R.id.marker_label2);
            final TextView txtViewMarkerLabel3  = (TextView) v.findViewById(R.id.marker_label3);
            ImageView imgViewTaxiLogo = (ImageView) v.findViewById(R.id.marker_icon);
            
            new AQuery(getActivity()).id(imgViewTaxiLogo).image(info.getTaxiLogo(), true, true, 50, R.drawable.texi_logo);
            
           // txtViewMarkerTaxiNameByTag = (TextView) v.findViewWithTag(index);
            //txtViewMarkerTaxiNameByTag.setText( "New " + PassengerApp.getInstance().getTaxiInfoList().get(index).getTaxiName());
            
            txtViewMarkerLabel1.setText(info.getTaxiName());
            txtViewMarkerLabel2.setText(info.getTaxiNumber());
            
//    		if(InternetConnectivity.isConnectedToInternet(getActivity())) {
//    			LatLng userLatLng = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
//    			LatLng taxiLoc = new LatLng(info.getTaxiLat(), info.getTaxiLon());
//    			new GetDurationAsyncTask(getActivity(), userLatLng, taxiLoc,  new OnRequestComplete() {
//    				
//    				@Override
//    				public void onRequestComplete(String result) {
//    					Log.e("Duration is:", "::::"+result);
//    					String [] distanceDuration = result.split("//");
//    					
//    					if (PassengerApp.getInstance().getTaxiInfoList() != null) {
//    						//PassengerApp.getInstance().getTaxiInfoList().get(index).setTimeDurationToReach(distanceDuration[1].toString());
//    							txtViewMarkerLabel3.setText(distanceDuration[1].toString());
//    					}
//    					
//    					Log.e("I am here with distance", "---------" + distanceDuration[1]);    
//    					
//    		      		//Here Window
//    				}
//    			}).execute();
//    		} else {
//    			Toast.makeText(getActivity(), ConstantValues.INTERNET_CONNECTION_FAILURE_MSG, Toast.LENGTH_SHORT).show();
//    		}
            
           if(info.getTimeDurationToReach() == null) {
        	   txtViewMarkerLabel3.setText("ETA: ");
            } else {
            	txtViewMarkerLabel3.setText("ETA: "+ info.getTimeDurationToReach());
            }
            
            return v;
        }
    }
    
}
