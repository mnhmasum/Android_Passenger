package com.netcabs.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.netcabs.datamodel.Country;
import com.netcabs.datamodel.CreditCardInfo;
import com.netcabs.datamodel.FavoriteInfo;
import com.netcabs.datamodel.JourneyReportInfo;
import com.netcabs.datamodel.MyBookingInfo;
import com.netcabs.datamodel.PastBookingDetailsInfo;
import com.netcabs.datamodel.PaymentsInfo;
import com.netcabs.datamodel.ProfileDetailsInfo;
import com.netcabs.datamodel.SeenUnSeenInfo;
import com.netcabs.datamodel.TaxiInfo;
import com.netcabs.passengerinfo.PassengerApp;

public class CommunicationLayer {
	
	//API #1 is called in Asyn Class

	//API #2
	public static String getCardRegistrationData(String func_id, String id, String card_type, String card_num, String card_holder_name, String card_exp_date, String cvv, String country_id, String zip_code, String is_default) throws Exception {
		
		Log.i("CARD_ID","*****" + id + " " + card_type + " " + card_num + " " + country_id);
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>(10);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("card_type", card_type));
		postData.add((NameValuePair) new BasicNameValuePair("car_num", card_num));
		postData.add((NameValuePair) new BasicNameValuePair("card_holder_name", card_holder_name));
		postData.add((NameValuePair) new BasicNameValuePair("card_exp_date", card_exp_date));
		postData.add((NameValuePair) new BasicNameValuePair("cvv", cvv));
		postData.add((NameValuePair) new BasicNameValuePair("country_id", country_id));
		postData.add((NameValuePair) new BasicNameValuePair("zip_code", zip_code));
		postData.add((NameValuePair) new BasicNameValuePair("is_default", is_default));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		
		Log.i("CARD_REGISTRATION_EXAMPLE","*****" + serverResponse);
		return parseCardRegistrationData(serverResponse);
	}

	private static String parseCardRegistrationData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		PassengerApp.getInstance().setPaymentId(jDataObj.getString("payment_id"));
		
		return jDataObj.getString("status_code");
	}
	
	//API #3
	public static String getMobileSetPin(String func_id, String id, String activation_code) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("activation_code", activation_code));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("server response for mobile code ", "___________"+serverResponse);
		return parseMobileSetPin(serverResponse);
	}

	private static String parseMobileSetPin(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #4
	public static String getSetPin(String func_id, String id, String pin) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("pin", pin));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseSetPin(serverResponse);
	}

	private static String parseSetPin(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #5
	public static String getLogInData(String func_id, String profile_type, String profile_id, String email, String password, String device_type, String device_token, String registration_key, String ip) throws Exception {
		
		Log.i("LOGIN INFO","ptype:" + profile_type +  "pid:" + profile_id + "email:" + email + "pass:" + password + "deviceType:" + device_type + "token:" + device_token + "regKey:" + registration_key + "ip: " + ip);
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>(6);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("profile_type", profile_type));
		postData.add((NameValuePair) new BasicNameValuePair("profile_id", profile_id));
		postData.add((NameValuePair) new BasicNameValuePair("email", email));
		postData.add((NameValuePair) new BasicNameValuePair("password", password));
		postData.add((NameValuePair) new BasicNameValuePair("device_type", device_type));
		postData.add((NameValuePair) new BasicNameValuePair("device_token", device_token));
		postData.add((NameValuePair) new BasicNameValuePair("registration_key", registration_key));
		postData.add((NameValuePair) new BasicNameValuePair("ip", ip));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.e("informattion", "__________"+postData);
		Log.i("server response for login", "__________"+serverResponse);
		return parseLogInData(serverResponse, Integer.parseInt(profile_type));
	}

	private static String parseLogInData(String jData, int type) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		
		if(jDataObj.getString("status").equals("1")) {
			if(jDataObj.getString("status_code").equals("2001")){
				PassengerApp.getInstance().setLogInType(type);
				PassengerApp.getInstance().setIsMobileVerified(jDataObj.getInt("is_mobile_verified"));
				PassengerApp.getInstance().setPassengerId(jDataObj.getString("id").toString().trim());
			}else{
				
			}
		}
		
		return jDataObj.getString("status_code");
	}
	
	//API #6
	public static String getLogInWithPin(String func_id, String id, String pin, String device_type, String device_token, String registration_key, String ip) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(7);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("pin", pin));
		postData.add((NameValuePair) new BasicNameValuePair("device_type", device_type));
		postData.add((NameValuePair) new BasicNameValuePair("device_token", device_token));
		postData.add((NameValuePair) new BasicNameValuePair("registration_key", registration_key));
		postData.add((NameValuePair) new BasicNameValuePair("ip", ip));
		
		Log.i("Post data is", "____________"+postData);

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("server response for login with Pin", "__________"+serverResponse);
		return parseLogInWithPin(serverResponse);
	}

	private static String parseLogInWithPin(String jData) throws JSONException {
		//PreferenceUtil preferenceUtil = new PreferenceUtil(context);
		JSONObject jDataObj = new JSONObject(jData);
//		if("2001".equals(jDataObj.getString("status_code"))) {
//			preferenceUtil.setUserFirstName(jDataObj.getString("first_name"));
//			preferenceUtil.setUserLastName(jDataObj.getString("last_name"));
//			preferenceUtil.setUserEmail(jDataObj.getString("email"));
//		}
		return jDataObj.getString("status_code");
	}
	
	//API #7
	public static String getTaxiInfoData(String func_id, String id, String lat, String lon) throws Exception {
		Log.i("Param", "if" + func_id + " id" + id + " lat" + lat + " Lon" + lon);
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("lat", lat));
		postData.add((NameValuePair) new BasicNameValuePair("long", lon));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("server response for Taxi Info Data", "__________"+serverResponse);
		return parseTaxiInfoData(serverResponse);
	}

	private static String parseTaxiInfoData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		JSONArray jsonArray = jDataObj.getJSONArray("data");
		
		if (jsonArray.length() > 0) {
			ArrayList<TaxiInfo> arrayTaxiInfo = new ArrayList<TaxiInfo>();
			
			for (int currentSize = 0; currentSize < jsonArray.length(); currentSize++) {
				JSONObject jsonObjData = jsonArray.getJSONObject(currentSize);
				TaxiInfo taxiInfo = new TaxiInfo();
				
				taxiInfo.setTaxiName(jsonObjData.getString("taxi_name"));
				taxiInfo.setTaxiModel(jsonObjData.getString("taxi_model"));
				taxiInfo.setTaxiNumber(jsonObjData.getString("taxi_number"));
				if(jsonObjData.getString("taxi_logo")!= null){
					taxiInfo.setTaxiLogo(jsonObjData.getString("taxi_logo"));
				}else{
					taxiInfo.setTaxiLogo("");
				}
				
				taxiInfo.setTaxiId(jsonObjData.getString("taxi_id"));
				JSONArray jsonArrayLocation = jsonObjData.getJSONArray("current_loc");
				
				taxiInfo.setTaxiLat(Double.parseDouble(jsonArrayLocation.get(1).toString()));
				taxiInfo.setTaxiLon(Double.parseDouble(jsonArrayLocation.get(0).toString()));
				
				Log.i("TaxiName","taxi Location" + jsonArrayLocation.get(0).toString() + ", " + jsonArrayLocation.get(1).toString());
				arrayTaxiInfo.add(taxiInfo);
			}
			
			PassengerApp.getInstance().setTaxiInfoList(arrayTaxiInfo);
			
		} else {
			PassengerApp.getInstance().setTaxiInfoList(null);
			
		}
		
		Log.i("STATUS_CODE","****" + jDataObj.getString("status_code"));
		
		return jDataObj.getString("status_code");
	}
	
	
	//API #8
	public static String getBooking(String func_id, String id, String dest_long, String dest_lat, String destination_address, String src_long, String src_lat, String pickup_address, String booking_via, String payment_method, String booking_date, String booking_time, String passenger_num, String is_parcel, String taxi_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(15);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("dest_long", dest_long));
		postData.add((NameValuePair) new BasicNameValuePair("dest_lat", dest_lat));
		postData.add((NameValuePair) new BasicNameValuePair("destination_address", destination_address));
		postData.add((NameValuePair) new BasicNameValuePair("src_long", src_long));
		postData.add((NameValuePair) new BasicNameValuePair("src_lat", src_lat));
		postData.add((NameValuePair) new BasicNameValuePair("pickup_address", pickup_address));
		postData.add((NameValuePair) new BasicNameValuePair("booking_via", booking_via));
		postData.add((NameValuePair) new BasicNameValuePair("payment_method", payment_method));
		postData.add((NameValuePair) new BasicNameValuePair("booking_date", booking_date));
		postData.add((NameValuePair) new BasicNameValuePair("booking_time", booking_time));
		postData.add((NameValuePair) new BasicNameValuePair("passenger_num", passenger_num));
		postData.add((NameValuePair) new BasicNameValuePair("is_parcel", is_parcel));
		postData.add((NameValuePair) new BasicNameValuePair("taxi_id", taxi_id));
		
		Log.i("server request for booking", "________"+postData);
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("server response for booking", "__________"+serverResponse);
		return parseBooking(serverResponse);
	}

	private static String parseBooking(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		PassengerApp.getInstance().setBookingId(jDataObj.getString("booking_id"));
		return jDataObj.getString("status_code");
	}
	
	
	//API #9
	public static String getSendAndSeen(String func_id, String id, String booking_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("booking_id", booking_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("server response for SEEN_UNSEEN", "__________" + serverResponse);
		return parseSendAndSeen(serverResponse);
	}

	private static String parseSendAndSeen(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		SeenUnSeenInfo seenUnSeenInfo = new SeenUnSeenInfo();
		
		seenUnSeenInfo.setBookingId(jDataObj.getString("booking_id"));
		seenUnSeenInfo.setRejectedCount(jDataObj.getInt("rejected_count"));
		Log.i("server Count SEEN", "__________" + jDataObj.getInt("seen_count"));
		seenUnSeenInfo.setSeenCount(jDataObj.getInt("seen_count"));
		
		if(jDataObj.getInt("sent_count") > 0) {
			seenUnSeenInfo.setSentCount(jDataObj.getInt("sent_count"));
		} else {
			if(PassengerApp.getInstance().getTaxiInfoList() != null) {
				seenUnSeenInfo.setSentCount(PassengerApp.getInstance().getTaxiInfoList().size());
			} else {
				seenUnSeenInfo.setSentCount(0);
			}
		}
		
		seenUnSeenInfo.setBookingMessage(jDataObj.getString("msg"));
		seenUnSeenInfo.setIsAccepted(jDataObj.getInt("is_accepted"));
		if(jDataObj.getString("driver_first_name") != null) {
			seenUnSeenInfo.setDriverFirstName(jDataObj.getString("driver_first_name").toString().trim());
		}
		
		if(jDataObj.getString("driver_last_name") != null) {
			seenUnSeenInfo.setDriverLastName(jDataObj.getString("driver_last_name").toString().trim());
		}
		PassengerApp.getInstance().setSeenUnSeenInfo(seenUnSeenInfo);
		
		return Integer.toString(jDataObj.getInt("status_code"));
	}
	
	
	//API #10
	public static String getFavouriteDestination(String func_id, String id, String lon, String lat, String location_name, String type, String is_fav) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(7);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("long", lon));
		postData.add((NameValuePair) new BasicNameValuePair("lat", lat));
		postData.add((NameValuePair) new BasicNameValuePair("location_name", location_name));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));
		postData.add((NameValuePair) new BasicNameValuePair("is_fav", is_fav));

		for (int i = 0; i<postData.size(); i++){
			Log.e("postData"+i, ""+postData);
		}
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.e("Server Response for fav dest", ""+serverResponse);
		return parseFavouriteDestination(serverResponse);
	}

	private static String parseFavouriteDestination(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #11
	public static String getFavouriteDestinationList(String func_id, String id, String type) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("type", type));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseFavouriteDestinationList(serverResponse);
	}

	private static String parseFavouriteDestinationList(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		if(jDataObj.getString("status").equals("1")) {
			ArrayList<FavoriteInfo> favoriteInfoArray = new ArrayList<FavoriteInfo> ();
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0 ; i< dataArray.length(); i++) {
					JSONObject values = dataArray.getJSONObject(i);
					FavoriteInfo favInfo = new FavoriteInfo();
					favInfo.setCreatedAt(values.getString("created_at"));
					
					if(values.getString("location_name") != null) {
						favInfo.setLocationName(values.getString("location_name"));
					} else {
						favInfo.setLocationName(values.getString("Name not found"));
					}
					
					JSONObject locationValue = values.getJSONObject("location");
					favInfo.setType(locationValue.getString("type"));
					JSONArray locationArray = locationValue.getJSONArray("coordinates");
					if(locationArray.length() > 0) {
//						favInfo.setLocationLongitude(locationArray.getString(0));
//						favInfo.setLocationLatitude(locationArray.getString(1));
						favInfo.setLocationLatitude(locationArray.getString(0));
						favInfo.setLocationLongitude(locationArray.getString(1));
						
					}
					favInfo.setIsFav(1);
					favoriteInfoArray.add(favInfo);
				}
				PassengerApp.getInstance().setFavoriteInfoList(favoriteInfoArray);
			} else {
				PassengerApp.getInstance().setFavoriteInfoList(null);
			}
			
		} else {
			PassengerApp.getInstance().setFavoriteInfoList(null);
		}
		return jDataObj.getString("status_code");
	}
	
	//API #12
	public static String getBookingList(String func_id, String id, String flag) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("flag", flag));
		
		Log.e("Server request for booking list", "_____"+postData);
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("Server response for booking list", "_____"+serverResponse);
		return parseBookingList(serverResponse);
	}

	private static String parseBookingList(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		if(jDataObj.getString("status").equals("1")) {
			ArrayList<MyBookingInfo> bookingInfoArray = new ArrayList<MyBookingInfo>();
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0 ; i< dataArray.length(); i++) {
					JSONObject values = dataArray.getJSONObject(i);
					MyBookingInfo bookingInfo = new MyBookingInfo();
					bookingInfo.setIsParcel(values.getInt("is_parcel"));
					bookingInfo.setPassengerNumber(values.getInt("passenger_num"));
					bookingInfo.setTaxiRegNo(values.getString("taxi_number"));
					bookingInfo.setBookingId(values.getString("booking_id"));
					
					if (values.getString("destination_address") != null) {
						bookingInfo.setDestinationName(values.getString("destination_address"));
					} else {
						bookingInfo.setDestinationName("");
					}
					
					if (values.getString("pickup_address") != null) {
						bookingInfo.setPickupName(values.getString("pickup_address"));
					} else {
						bookingInfo.setPickupName("");
					}
					
					if (values.getString("booking_time") != null) {
						bookingInfo.setTime(values.getString("booking_time"));
					} else {
						
						bookingInfo.setTime("00:00:00");
					}
					
					if(values.getString("booking_date") != null) {
						bookingInfo.setDate(values.getString("booking_date"));
					} else {
						
						bookingInfo.setDate("00/00/0000");
					}
					
					if(values.getString("taxi_logo") != null) {
						bookingInfo.setTaxiLogoUrl(values.getString("taxi_logo"));
					} else {
						bookingInfo.setTaxiLogoUrl("");
					}
					
					if(values.getString("taxi_name") != null) {
						bookingInfo.setTaxi_name(values.getString("taxi_name"));
					} else {
						bookingInfo.setTaxi_name("");
					}
					
//					if(values.getString("taxi_rego_no") != null) {
//						bookingInfo.setTaxiRegNo(values.getString("taxi_rego_no"));
//					} else {
//						bookingInfo.setTaxiRegNo("");
//					}
					
					if(values.getString("total_distance") != null) {
						if(values.getString("total_distance").contains("null")){
							bookingInfo.setDistance("00 km");
						} else {
							bookingInfo.setDistance(values.getString("total_distance"));
						}
					} else {
						bookingInfo.setDistance("00 km");
					}
					
					if(values.getString("total_trip_time") != null) {
						if(values.getString("total_trip_time").contains("null")){
							bookingInfo.setDurationTime("00:00 h");
						} else {
							bookingInfo.setDurationTime(values.getString("total_trip_time"));
						}
					} else {
						bookingInfo.setDurationTime("00 h");
					}
					
					if(values.getString("taxi_id") != null) {
						bookingInfo.setTaxi_id(values.getString("taxi_id"));
					} else {
						bookingInfo.setTaxi_id("");
					}
					
					if(values.getString("taxi_model") != null) {
						bookingInfo.setTaxi_model(values.getString("taxi_model"));
					} else {
						bookingInfo.setTaxi_model("");
					}
					
					if(values.getString("taxi_number") != null) {
						bookingInfo.setTaxi_number(values.getString("taxi_number"));
					} else {
						bookingInfo.setTaxi_number("");
					}
					
					
					bookingInfo.setPrice(values.getDouble("price"));
					
					
					JSONObject pickUpObj = values.getJSONObject("pickup_loc");
					JSONArray pickUpCoordinates = pickUpObj.getJSONArray("coordinates");
					bookingInfo.setPickupLat(pickUpCoordinates.getDouble(1));
					bookingInfo.setPickupLon(pickUpCoordinates.getDouble(0));
					
					JSONObject destinationObj = values.getJSONObject("destination_loc");
					JSONArray destinationCoordinates = destinationObj.getJSONArray("coordinates");
					bookingInfo.setDestinationLat(destinationCoordinates.getDouble(1));
					bookingInfo.setDestinationLon(destinationCoordinates.getDouble(0));
					
					JSONArray currentLocationArra = values.getJSONArray("taxi_current_loc");
					bookingInfo.setTaxiCurrentLat(currentLocationArra.getDouble(1));
					bookingInfo.setTaxiCurrentLon(currentLocationArra.getDouble(0));
					bookingInfoArray.add(bookingInfo);
				}
				PassengerApp.getInstance().setMyBookingInfoList(bookingInfoArray);
			} else {
				PassengerApp.getInstance().setMyBookingInfoList(null);
			}
			
		} else {
			PassengerApp.getInstance().setMyBookingInfoList(null);
		}
		
		return jDataObj.getString("status_code");
	}
	
	//API #13
	public static String getCancelBooking(String func_id, String id, String booking_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("booking_id", booking_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseCancelBooking(serverResponse);
	}

	private static String parseCancelBooking(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #14
	public static String getFriedList(String func_id, String id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
	
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseFriedList(serverResponse);
	}

	private static String parseFriedList(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #15
	public static String getAddFriend(String func_id, String id, String friend_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("friend_id", friend_id));
	
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseAddFriend(serverResponse);
	}

	private static String parseAddFriend(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #16
	public static String getShareTrip(String func_id, String id, String booking_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("booking_id", booking_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.e("Server Response for Shared Trip",""+serverResponse);
		return parseShareTrip(serverResponse);
	}

	private static String parseShareTrip(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		if(jDataObj.getString("status").equals("1")){
			if(jDataObj.getString("status_code").equals("2001")){
				String urlLink = jDataObj.getString("url");
				PassengerApp.getInstance().setShareTripLink(urlLink);
			}else{
				PassengerApp.getInstance().setShareTripLink("");
			}
		}else{
			PassengerApp.getInstance().setShareTripLink("");
		}
		return jDataObj.getString("status_code");
	}
	
	//API #17
	public static String getCardList(String func_id, String id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseCardList(serverResponse);
	}

	private static String parseCardList(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		if(jDataObj.getString("status").equals("1")){
			ArrayList<CreditCardInfo> cireditCardInfoArray = new ArrayList<CreditCardInfo>();
			JSONArray dataArray = jDataObj.getJSONArray("data");
			if(dataArray.length() > 0) {
				for(int i = 0 ; i< dataArray.length(); i++){
					JSONObject values = dataArray.getJSONObject(i);
					CreditCardInfo creditCardInfo = new CreditCardInfo();
					creditCardInfo.setPaymentId(values.getString("payment_id"));
					creditCardInfo.setCareType(values.getString("card_type"));
					creditCardInfo.setCvv(values.getInt("cvv"));
					creditCardInfo.setStatus(values.getInt("status"));
					creditCardInfo.setExpireDate(values.getString("expire_date"));
					creditCardInfo.setIsDefault(values.getInt("is_default"));
					creditCardInfo.setCreatedAt(values.getString("created_at"));
					creditCardInfo.setCardNumber(values.getString("card_number"));
					creditCardInfo.setUpdatedAt(values.getString("updated_at"));
					creditCardInfo.setCardHolderName(values.getString("card_holder_name"));
					creditCardInfo.setZip(values.getString("zip_code"));
					creditCardInfo.setCountryId(values.getString("country_id"));
					creditCardInfo.setCountryName(values.getString("country_name"));
					cireditCardInfoArray.add(creditCardInfo);
				}
			
			PassengerApp.getInstance().setCreditCardInfoList(cireditCardInfoArray);
			}else{
				PassengerApp.getInstance().setCreditCardInfoList(null);
			}
			
		}else{
			PassengerApp.getInstance().setCreditCardInfoList(null);
		}
		return jDataObj.getString("status_code");
	}
	
	//API #18
	public static String getMakeDefault(String func_id, String id, String card_id, String is_default) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("payment_id", card_id));
		postData.add((NameValuePair) new BasicNameValuePair("is_default", is_default));
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseMakeDefult(serverResponse);
	}

	private static String parseMakeDefult(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	
	//API #19 will be called using httpmime
	
	//API #20
	public static String getLostPasswordData(String func_id, String email_address) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("email", email_address));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.e("api 20 response", "---"+serverResponse);
		return parseLostPasswordData(serverResponse);
	}

	private static String parseLostPasswordData(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	
	//API #21
	public static String getResetPassword(String func_id, String email, String activation_code, String password) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("email", email));
		postData.add((NameValuePair) new BasicNameValuePair("activation_code", activation_code));
		postData.add((NameValuePair) new BasicNameValuePair("password", password));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseResetPassword(serverResponse);
	}

	private static String parseResetPassword(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #22
	public static String getResendCode(String func_id, String id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("Rerser response for resend code", "________"+serverResponse);
		return parseResendCode(serverResponse);
	}

	private static String parseResendCode(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #23
	public static String getEmailCheck(String func_id, String email) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("email", email));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("email check", "__________"+serverResponse);
		return parseEmailCheck(serverResponse);
	}

	private static String parseEmailCheck(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #24
	public static String getCountryList(String func_id, Context context) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(1);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseCountryList(serverResponse, context);
	}

	private static String parseCountryList(String jData , Context context) throws JSONException {
		String response = "0";
		if(jData == null) {
			 response = "0";
			 return response;
		} else {
			JSONArray jDataArray = new JSONArray(jData);
				if (jDataArray.length() <= 0) {
					response = "0";
					return response;
				} else {
					response = "1";
					ArrayList<Country> countryDataArray = new ArrayList<Country>();
					if(jDataArray.length() > 0 ){
						for(int i = 0; i < jDataArray.length(); i++ ) {
							JSONObject dataObj = jDataArray.getJSONObject(i);
							Country country = new Country();
							country.setName(dataObj.getString("name"));
							country.setShortName(dataObj.getString("cca3"));
							InputStream is = null;
							try {
								is = context.getResources().getAssets().open("flag_"+dataObj.getString("cca2").toLowerCase()+".png");
							} catch (IOException e) {
								Log.w("EL", e);
							}
							country.setImgBitmap(BitmapFactory.decodeStream(is));
							
							JSONArray callingArray = dataObj.getJSONArray("callingCode");
							if( jDataArray.length() > 0) {
								country.setCallingId(callingArray.getString(0));
							}
							JSONObject idObj = dataObj.getJSONObject("_id");
							country.setId(idObj.optString("$oid"));
							
							countryDataArray.add(country);
						}
						PassengerApp.getInstance().setCountryArray(countryDataArray);
					} else {
						PassengerApp.getInstance().setCountryArray(null);
					}
					return response;
				}
				
		  }	
	}
	
	
	//API #25
	public static String getPaymentMethodList(String func_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(1);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parsePaymentMethodList(serverResponse);
	}

	private static String parsePaymentMethodList(String jData) throws JSONException {
		String response = "0";
		if(jData == null) {
			 response = "0";
			 return response;
		} else {
			JSONArray jsonArray = new JSONArray(jData);
			if (jsonArray.length() <= 0) {
				response = "0";
				return response;
			} else {
				response = "1";
				ArrayList<PaymentsInfo> list = new ArrayList<PaymentsInfo>();
				if(jsonArray != null) {
					for(int i = 0; i < jsonArray.length(); i++) {
						PaymentsInfo info = new PaymentsInfo();
						JSONObject dataObj = jsonArray.getJSONObject(i);
						info.setMethodName(dataObj.getString("method_name"));
						JSONObject idObj = dataObj.getJSONObject("_id");
						info.setId(idObj.optString("$oid"));
						list.add(info);
					}
					
					PassengerApp.getInstance().setPaymentsInfoList(list);
				} else {
					PassengerApp.getInstance().setPaymentsInfoList(null);
				}
				return response;
			}
		}
	}
	
	//API #26 
	public static String getBookingDetails(String func_id, String id, String booking_id) throws Exception  {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(1);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("booking_id", booking_id));
		Log.e("booking details parameter", "-----" + postData);
		
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.e("server respose for get booking details", "-----"+ serverResponse);
		return parseBookingDetails(serverResponse);
	}
	
	private static String parseBookingDetails(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		
		MyBookingInfo mybooking = new MyBookingInfo();
		JSONArray jArrayData = jDataObj.getJSONArray("data");
		if (jArrayData.length() >0) {
			JSONObject values = jArrayData.getJSONObject(0);
			
			mybooking.setIsParcel(values.getInt("is_parcel"));
			mybooking.setPassengerNumber(values.getInt("passenger_num"));
			//mybooking.setTaxiRegNo(values.getString("taxi_number"));
			//mybooking.setBookingId(values.getString("booking_id"));
			
			if (values.getString("destination_address") != null) {
				mybooking.setDestinationName(values.getString("destination_address"));
			} else {
				mybooking.setDestinationName("");
			}
			
			if (values.getString("pickup_address") != null) {
				mybooking.setPickupName(values.getString("pickup_address"));
			} else {
				mybooking.setPickupName("");
			}
			
			if (values.getString("booking_time") != null) {
				mybooking.setTime(values.getString("booking_time"));
			} else {
				mybooking.setTime("00:00:00");
			}
			
			if(values.getString("booking_date") != null) {
				mybooking.setDate(values.getString("booking_date"));
			} else {
				mybooking.setDate("00/00/0000");
			}
			
			if(values.getString("taxi_logo") != null) {
				mybooking.setTaxiLogoUrl(values.getString("taxi_logo"));
			} else {
				mybooking.setTaxiLogoUrl("");
			}
			
			if(values.getString("taxi_name") != null) {
				mybooking.setTaxi_name(values.getString("taxi_name"));
			} else {
				mybooking.setTaxi_name("");
			}
			
			if(values.getString("total_distance") != null) {
				if(values.getString("total_distance").contains("null")){
					mybooking.setDistance("00 km");
				} else {
					mybooking.setDistance(values.getString("total_distance"));
				}
			} else {
				mybooking.setDistance("00 km");
			}
			
			if(values.getString("total_trip_time") != null) {
				if(values.getString("total_trip_time").contains("null")){
					mybooking.setDurationTime("00:00 h");
				} else {
					mybooking.setDurationTime(values.getString("total_trip_time"));
				}
			} else {
				mybooking.setDurationTime("00 h");
			}
			
			if(values.getString("taxi_rego_no") != null) {
				mybooking.setTaxiRegNo(values.getString("taxi_rego_no"));
			} else {
				mybooking.setTaxiRegNo("");
			}
			
			if(values.getString("taxi_id") != null) {
				mybooking.setTaxi_id(values.getString("taxi_id"));
			} else {
				mybooking.setTaxi_id("");
			}
			
			if(values.getString("taxi_model") != null) {
				mybooking.setTaxi_model(values.getString("taxi_model"));
			} else {
				mybooking.setTaxi_model("");
			}
			
			if(values.getString("taxi_number") != null) {
				mybooking.setTaxi_number(values.getString("taxi_number"));
			} else {
				mybooking.setTaxi_number("");
			}
			
			mybooking.setAcceptStatus(values.getInt("status"));
			mybooking.setBookingStatus(values.getInt("booking_status"));
			
			
			JSONObject pickUpObj = values.getJSONObject("pickup_loc");
			JSONArray pickUpCoordinates = pickUpObj.getJSONArray("coordinates");
			mybooking.setPickupLat(pickUpCoordinates.getDouble(1));
			mybooking.setPickupLon(pickUpCoordinates.getDouble(0));
			
			JSONObject destinationObj = values.getJSONObject("destination_loc");
			JSONArray destinationCoordinates = destinationObj.getJSONArray("coordinates");
			mybooking.setDestinationLat(destinationCoordinates.getDouble(1));
			mybooking.setDestinationLon(destinationCoordinates.getDouble(0));
			
			JSONArray currentLocationArra = values.getJSONArray("taxi_current_loc");
			mybooking.setTaxiCurrentLat(currentLocationArra.getDouble(1));
			mybooking.setTaxiCurrentLon(currentLocationArra.getDouble(0));
			
			
			mybooking.setDriverMobileNo(values.getString("driver_mobile_no"));
			
		}else{
			
		}
		PassengerApp.getInstance().setMyBookingInfo(mybooking);
		
		
		return jDataObj.getString("status_code");
	}
	
	//API #27
	public static String getProfileDetails(String func_id, String id) throws Exception {
		Log.i("ID","**" + id);
		List<NameValuePair> postData = new ArrayList<NameValuePair>(1);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.e("Server response for profile details", "---"+serverResponse);
		return parseProfileDetails(serverResponse);
	}

	private static String parseProfileDetails(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		ProfileDetailsInfo proInfo = new ProfileDetailsInfo();
		JSONObject values = jDataObj.getJSONObject("data");
		proInfo.setFirstName(values.getString("first_name"));
		proInfo.setLastName(values.getString("last_name"));
		proInfo.setMobileVerified(values.getInt("is_mobile_verified"));
		proInfo.setMobileNo(values.getString("mobile_no"));
		proInfo.setZip(values.getString("zip"));
		proInfo.setProfilePic(values.getString("profile_pic"));
		proInfo.setEmail(values.getString("email"));
		proInfo.setCountryId(values.getString("country_id"));
		
		if (values.getString("country") != null) {
			proInfo.setCountry(values.getString("country"));
		} else {
			proInfo.setCountry("");
		}

		PassengerApp.getInstance().setProfileDetailsInfo(proInfo);
		
		return jDataObj.getString("status_code");
	}
	
	//API #29
	public static String getDeleteCardNo(String func_id, String id, String card_number) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("card_number", card_number));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseDeleteCardNo(serverResponse);
	}
	
	private static String parseDeleteCardNo(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #30
	public static String getPastBookingDetails(String func_id, String id, String booking_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("booking_id", booking_id));
		Log.i("Past_Booking_Details_Parameter", "***" + postData);
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("Past_Booking_Details_Parameter", "***" + postData);
		return parsePastBookingDetails(serverResponse);
	}
	
	private static String parsePastBookingDetails(String jData) throws JSONException {
		PastBookingDetailsInfo pastBookingDetailsInfo = new PastBookingDetailsInfo();
		
		JSONObject jDataObj = new JSONObject(jData);
		JSONArray jArrayData = jDataObj.getJSONArray("data");
		if(jArrayData != null) {
			for (int i = 0; i < jArrayData.length(); i++) {
				ArrayList<JourneyReportInfo> journeyReportInfoList = new ArrayList<JourneyReportInfo>();
				JSONObject jArrayDataObj = jArrayData.getJSONObject(i);
				
				pastBookingDetailsInfo.setTaxiId(jArrayDataObj.getString("taxi_id"));
				pastBookingDetailsInfo.setTaxiModel(jArrayDataObj.getString("taxi_model"));
				pastBookingDetailsInfo.setTaxiNumber(jArrayDataObj.getString("taxi_number"));
				
				pastBookingDetailsInfo.setPassengerName(jArrayDataObj.getString("first_name") + " " + jArrayDataObj.getString("last_name"));
				pastBookingDetailsInfo.setPassengerEmail(jArrayDataObj.getString("email"));
				pastBookingDetailsInfo.setDriverName(jArrayDataObj.getString("driver_first_name") + " " + jArrayDataObj.getString("driver_last_name"));
				pastBookingDetailsInfo.setRegNo(jArrayDataObj.getString("taxi_number"));
				pastBookingDetailsInfo.setPickUpTime(jArrayDataObj.getString("pickup_dt")); //***
				pastBookingDetailsInfo.setDropOfTime(jArrayDataObj.getString("drop_off_dt"));
				pastBookingDetailsInfo.setDistance(jArrayDataObj.getString("total_distance"));
				
				if (!jArrayDataObj.isNull("fare_price")) {
					pastBookingDetailsInfo.setFarePrice(""+jArrayDataObj.getDouble("fare_price"));
				} else {
					pastBookingDetailsInfo.setFarePrice(""+ 0);
				}
				
				if (!jArrayDataObj.isNull("extras")) {
					pastBookingDetailsInfo.setExtras("" + jArrayDataObj.getDouble("extras"));
				} else {
					pastBookingDetailsInfo.setExtras("" + 0);
				}
				
				if (!jArrayDataObj.isNull("gst")) {
					pastBookingDetailsInfo.setGST("" + jArrayDataObj.getDouble("gst"));
				} else {
					pastBookingDetailsInfo.setGST("" + 0);
				}
				
				if (!jArrayDataObj.isNull("amount")) {
					pastBookingDetailsInfo.setTotalAmount("" + jArrayDataObj.getDouble("amount"));
				} else {
					pastBookingDetailsInfo.setTotalAmount("" + 0);
				}
				
				if(jArrayDataObj.getString("total_trip_time") != null ){
					if(jArrayDataObj.getString("total_trip_time").contains("null")){
						pastBookingDetailsInfo.setTotalTripTime("00.00 h");
					} else{
						pastBookingDetailsInfo.setTotalTripTime(jArrayDataObj.getString("total_trip_time")); //
					}
					
				}else{
					pastBookingDetailsInfo.setTotalTripTime("00.00 h");
				}
				
			
				if(jArrayDataObj.getString("total_distance") !=null){
					if(jArrayDataObj.getString("total_distance").contains("null")){
						pastBookingDetailsInfo.setDistance("00 km");
					} else{
						pastBookingDetailsInfo.setDistance(jArrayDataObj.getString("total_distance")); //
					}
				} else{
					pastBookingDetailsInfo.setDistance("00 km");
				}
				
				pastBookingDetailsInfo.setPaymentDate(jArrayDataObj.getString("drop_off_dt"));
				pastBookingDetailsInfo.setPaymentType(jArrayDataObj.getString("payment_method"));
				
				pastBookingDetailsInfo.setPickupAddress(jArrayDataObj.getString("pickup_address"));
				
				JSONObject jsonPicupLocationObj = jArrayDataObj.getJSONObject("pickup_loc");
				jsonPicupLocationObj.getString("type");
				JSONArray jArrayCoordinateData = jsonPicupLocationObj.getJSONArray("coordinates");
				pastBookingDetailsInfo.setPickUpLocationLatitude((Double) jArrayCoordinateData.get(1));
				pastBookingDetailsInfo.setPickUpLocationLongitude((Double) jArrayCoordinateData.get(0));
				
				JSONObject jsonDestinationLocationObj = jArrayDataObj.getJSONObject("destination_loc");
				jsonDestinationLocationObj.getString("type");
				JSONArray jArrayDestinationCoordinateData = jsonDestinationLocationObj.getJSONArray("coordinates");
				pastBookingDetailsInfo.setDestinationLocationLatitude((Double) jArrayDestinationCoordinateData.get(1));
				pastBookingDetailsInfo.setDestinationLocationLongitude((Double) jArrayDestinationCoordinateData.get(0));
				
				pastBookingDetailsInfo.setDropOfAddress(jArrayDataObj.getString("drop_off_address_name"));
				
				JSONArray jsonJourneyCoordinateArray = jArrayDataObj.getJSONArray("journey_coordinates");
				
				if(jsonJourneyCoordinateArray != null) {
					if(jsonJourneyCoordinateArray.length() > 0) {
						for (int j = 0; j < jsonJourneyCoordinateArray.length(); j++) {
							JourneyReportInfo journeyCoordinateInfo = new JourneyReportInfo();
							//JSONObject jObj = jsonJourneyCoordinateArray.getJSONObject(j);
							JSONArray jsonJourneyArray = jsonJourneyCoordinateArray.getJSONArray(j);
							journeyCoordinateInfo.setJourneyLatitude(Double.parseDouble(jsonJourneyArray.getString(1)));
							journeyCoordinateInfo.setJourneyLongitude(Double.parseDouble(jsonJourneyArray.getString(0)));
							journeyReportInfoList.add(journeyCoordinateInfo);
						}
						pastBookingDetailsInfo.setJourneyCoordinateList(journeyReportInfoList);
					} else {
						pastBookingDetailsInfo.setJourneyCoordinateList(null);
					}
				} else {
					pastBookingDetailsInfo.setJourneyCoordinateList(null);
				}
				Log.i("API #30"," " + jArrayDataObj.getString("taxi_id") + jsonPicupLocationObj.getString("type"));
				PassengerApp.getInstance().setPastBookingDetailsInfo(pastBookingDetailsInfo);
				
			}
		} else {
			PassengerApp.getInstance().setPastBookingDetailsInfo(null);
		}
		
		return jDataObj.getString("status_code");
	}
	
	//API #31
	public static String getContactEmail(String func_id, String id, String subject, String body) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("subject", subject));
		postData.add((NameValuePair) new BasicNameValuePair("body", body));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseContactEmail(serverResponse);
	}
	
	private static String parseContactEmail(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #32
	public static String getHailedRegoCheck(String func_id, String id, String rego_id) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("rego_no", rego_id));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("Server response for reg checking ", "____________"+serverResponse);
		return parsetHailedRegoCheck(serverResponse);
	}
	
	private static String parsetHailedRegoCheck(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
	
	//API #33
	public static String getHailedBooking(String func_id, String id, String rego_no, String dest_long, String dest_lat, String destination_address, String src_long, String src_lat, String pickup_address, String booking_via, String payment_method, String booking_date, String booking_time, String passenger_num, String is_parcel) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(15);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("rego_no", rego_no));
		postData.add((NameValuePair) new BasicNameValuePair("dest_long", dest_long));
		postData.add((NameValuePair) new BasicNameValuePair("dest_lat", dest_lat));
		postData.add((NameValuePair) new BasicNameValuePair("destination_address", destination_address));
		postData.add((NameValuePair) new BasicNameValuePair("src_long", src_long));
		postData.add((NameValuePair) new BasicNameValuePair("src_lat", src_lat));
		postData.add((NameValuePair) new BasicNameValuePair("pickup_address", pickup_address));
		postData.add((NameValuePair) new BasicNameValuePair("booking_via", booking_via));
		postData.add((NameValuePair) new BasicNameValuePair("payment_method", payment_method));
		postData.add((NameValuePair) new BasicNameValuePair("booking_date", booking_date));
		postData.add((NameValuePair) new BasicNameValuePair("booking_time", booking_time));
		postData.add((NameValuePair) new BasicNameValuePair("passenger_num", passenger_num));
		postData.add((NameValuePair) new BasicNameValuePair("is_parcel", is_parcel));
		
		Log.i("Value is ", "________"+postData);
		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		Log.i("server response for hailed booking", "__________"+serverResponse);
		return parseHailedBooking(serverResponse);
	}

	private static String parseHailedBooking(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		PassengerApp.getInstance().setBookingId(jDataObj.getString("booking_id"));
		Log.e("my booking id is", "------"+PassengerApp.getInstance().getBookingId());
		return jDataObj.getString("status_code");
	}
	
	//API #34
	public static String getCardEdit(String func_id, String id, String payment_id, String cvv, String country_id, String zip_code, String expire_date, String card_holder_name) throws Exception {
		List<NameValuePair> postData = new ArrayList<NameValuePair>(8);
		postData.add((NameValuePair) new BasicNameValuePair("func_id", func_id));
		postData.add((NameValuePair) new BasicNameValuePair("id", id));
		postData.add((NameValuePair) new BasicNameValuePair("payment_id", payment_id));
		postData.add((NameValuePair) new BasicNameValuePair("cvv", cvv));
		postData.add((NameValuePair) new BasicNameValuePair("country_id", country_id));
		postData.add((NameValuePair) new BasicNameValuePair("zip_code", zip_code));
		postData.add((NameValuePair) new BasicNameValuePair("expire_date", expire_date));
		postData.add((NameValuePair) new BasicNameValuePair("card_holder_name", card_holder_name));

		String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(PassengerApp.getInstance().getBaseUrl(), postData, true);
		return parseCardEdit(serverResponse);
	}
	
	private static String parseCardEdit(String jData) throws JSONException {
		JSONObject jDataObj = new JSONObject(jData);
		return jDataObj.getString("status_code");
	}
}
