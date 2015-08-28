package com.bus.proj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Gps extends Activity implements LocationListener {

	//protected Button retrieveLocationButton;
	double latitude,longitude,final_lat,final_long,rb_lat,rb_long,user_lat,user_long;
	double distance,min_distance=1.79769313486231570e+308d;
	TextView loc,selected_loc,dist,min_dist;
	String text_rb,min_place;
	String latitudes[]=new String [2];
	String longitudes[]=new String[2];
	Uri uri;
	ArrayList<String> arl=new ArrayList<String>();
	int i=0,j=0,flag=0;
	private Location location;

	private LocationManager locationManager=null;
	private LocationListener locationListener=null;

	boolean isGPSEnabled = false;
	// flag for network status
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;

	// The minimum distance to change updates in metters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10
	// metters
	// The minimum time beetwen updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1
	// minute

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_main);

		Intent intent=this.getIntent();
		text_rb=intent.getStringExtra("text_radiob"); //selected radio button
		arl=intent.getStringArrayListExtra("arr_list"); //all intermediate bus stop/ radio button values
		// currentloc_rb[0]=text_rb;
		selected_loc=(TextView)this.findViewById(R.id.editText2);
		selected_loc.setText(text_rb);


		//		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//		System.out.println("Location_Man:"+locationManager);
		//		
		//		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
		//			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
		//			if (locationManager!= null){
		//				location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		//			}
		//		}
		//		


		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(location==null){

					getlocation();
				}
				else{
					nextPage(v);
				}

			}
		});

		final Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				distance(v);
			}
		});

		final Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				find_near(v);
			}
		});


		final Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				show_route_rb(v);
			}
		});

		final Button button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				show_route_near(v);
			}
		});


	}

	public void getlocation(){
		try {
			Log.d("Gps.java","getting location");
			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);
			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled && !isNetworkEnabled) {
				// location service disabled
			} else {
				this.canGetLocation = true;
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {

					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Gps.java", "GPS Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						// updateGPSCoordinates();
					}
				}
				// First get location from Network Provider
				if (isNetworkEnabled) {

					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("Gps.java", "Network");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							// updateGPSCoordinates();
						}
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			Log.e("Error : Location",
					"Impossible to connect to LocationManager", e);
		}

	}

	//Get User's Current Location 
	public void nextPage(View view) {

		try
		{   
			loc=(TextView)this.findViewById(R.id.editText1);

			/* Use the LocationManager class to obtain GPS locations */
			// Acquire a reference to the system Location Manager
			//LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

			// Define a listener that responds to location updates
			/*LocationListener locationListener = new LocationListener()  {
				public void onLocationChanged(Location location) {
					// Called when a new location is found by the network location provider.

					latitude =  (double) location.getLatitude();
					longitude= (double) location.getLongitude();
					latitudes[0]=Double.toString(latitude);
					longitudes[0]=Double.toString(longitude);
					user_lat=latitude;
					user_long=longitude;

					// loc.setText(str);  

				}

				public void onStatusChanged(String provider, int status, Bundle extras) {}

				public void onProviderEnabled(String provider) {}

				public void onProviderDisabled(String provider) {}
			};*/

			// Register the listener with the Location Manager to receive location updates
			//	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

			//	latitude=(double) locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
			//	longitude=(double) locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		//Reverse Geocoding to get Address from User's current location/co-ordinates(Latitude/Longitude)
		try
		{        	      

			new current_loc().execute();
			//			String str="http://maps.googleapis.com/maps/api/geocode/json?latlng="+  latitude+","+longitude+"&sensor=false";
			//
			//			String temp="";
			//			URL url = new URL(str);
			//			BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
			//			while ((str = r.readLine()) != null)
			//				//str=r.readLine();
			//			{
			//				temp=temp+str;
			//
			//			}
			//
			//			try{
			//				JSONObject mainObj= new JSONObject(temp);
			//				JSONArray jArray = mainObj.optJSONArray("results");
			//				JSONObject temp2 = jArray.optJSONObject(0);
			//
			//				loc.setText(temp2.get("formatted_address").toString());
			//
			//			}
			//			catch(Exception e){
			//				loc.setText("user location json exception...");
			//				}


		}

		catch(Exception e){Log.d("Gps.java","call current_loc: "+e);}


	}

	//Geocoding to get co-cordinates (Latitude/Longitude) of selected radio button
	public void distance(View view) 
	{
		//geocode(text_rb);
		try
		{        	      
			new dist_current_rb().execute();

			//			String str="http://maps.googleapis.com/maps/api/geocode/json?address="+text_rb+",+Chennai&sensor=false";
			//
			//			String temp="";
			//			URL url = new URL(str.replace(" ", "%20"));
			//			BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
			//			while ((str = r.readLine()) != null)
			//
			//			{
			//				temp=temp+str;
			//
			//			}
			//
			//			try{
			//				dist=(TextView)this.findViewById(R.id.textView1);
			//
			//				JSONObject mainObj= new JSONObject(temp);
			//				JSONArray jArray = mainObj.optJSONArray("results");
			//
			//
			//				JSONObject temp2 = jArray.optJSONObject(0);
			//				JSONObject loc = temp2.optJSONObject("geometry").optJSONObject("location");
			//				latitudes[1]=loc.getString("lat");
			//				longitudes[1]=loc.getString("lng");
			//
			//				rb_lat=Double.valueOf(latitudes[1]);
			//				rb_long=Double.valueOf(longitudes[1]);
			//
			//
			//			}
			//			catch(Exception e){loc.setText("json exception...");}
			//			
			//calculate distance between Current Location & Selected Radio Button/Bus stop
			//			try
			//			{
			//				Location locationA = new Location("point A");
			//
			//				locationA.setLatitude(Double.valueOf(latitudes[0]));
			//				locationA.setLongitude(Double.valueOf(longitudes[0]));
			//
			//				Location locationB = new Location("point B");
			//
			//				locationB.setLatitude(Double.valueOf(latitudes[1]));
			//				locationB.setLongitude(Double.valueOf(longitudes[1]));
			//
			//				distance = locationA.distanceTo(locationB);
			//
			//			}
			//			catch(Exception e)
			//			{  
			//				dist.setText("Cannot find the location...");
			//
			//			}   

		}
		catch(Exception e){Log.d("Gps.java","call dist_current_rb: "+e);}
		//		dist.setText("Distance is: "+distance);
		//		distance=1.79769313486231570e+308d;
	}

	//Geocoding to get co-cordinates (Latitude/Longitude) of intermediate bus stops
	//to get nearest bus stop from User's current location
	public void find_near(View view)
	{

		new dist_current_rg().execute();

		//		int len=arl.size();
		//		
		//		String place;
		//		String str;
		//		String temp1="";
		//		URL url;
		//		BufferedReader r;
		//		
		//		String temp;
		//		for(i=0;i<len;i++)
		//		{
		//			place="";
		//			place=arl.get(i);
		//
		//			
		//			try
		//			{        	      
		//				text_rb="";temp="";
		//				text_rb=place;
		//				str="";
		//				str="http://maps.googleapis.com/maps/api/geocode/json?address="+text_rb+",+Chennai&sensor=false";
		//
		//				
		//				url=new URL(str.replace(" ", "%20"));
		//				r = new BufferedReader(new InputStreamReader(url.openStream()),8*1024);
		//				while ((str = r.readLine()) != null)
		//
		//				{
		//					temp +=str;
		//
		//				}
		//
		//				try{
		//					dist=(TextView)this.findViewById(R.id.textView1);
		//
		//					JSONObject mainObj= new JSONObject(temp);
		//					JSONArray jArray = mainObj.optJSONArray("results");
		//
		//					JSONObject temp2 = jArray.optJSONObject(0);
		//					JSONObject loc = temp2.optJSONObject("geometry").optJSONObject("location");
		//					latitudes[1]=loc.getString("lat");
		//					longitudes[1]=loc.getString("lng");
		//
		//
		//				}
		//				catch(Exception e){loc.setText("json exception...");}
		//				try
		//				{
		//
		//
		//					Location locationA = new Location("point A");
		//
		//					locationA.setLatitude(Double.valueOf(latitudes[0]));
		//					locationA.setLongitude(Double.valueOf(longitudes[0]));
		//
		//					Location locationB = new Location("point B");
		//
		//
		//					locationB.setLatitude(Double.valueOf(latitudes[1]));
		//					locationB.setLongitude(Double.valueOf(longitudes[1]));
		//
		//					distance = locationA.distanceTo(locationB);
		//					
		//					min(min_distance,distance,place,latitudes,longitudes);
		//				}
		//				catch(Exception e)
		//				{  
		//					dist.setText("Cannot find the location...place:"+place);
		//					distance=999999999E100;
		//
		//				}   
		//
		//			}
		//			catch(Exception e){loc.setText("geocode error....");}
		//
		//
		//		}
		//
		//
		//		min_dist=(TextView)this.findViewById(R.id.textView2);
		//		min_dist.setText("Nearest place is:"+min_place+"\nDistance is:"+min_distance+"\nlat:"+final_lat+"\nlong:"+final_long);

	}

	//Show Map Route between User's Current Location and selected radio button
	public void show_route_rb(View view)
	{
		uri = Uri.parse("http://maps.google.com/maps?&saddr="+user_lat+","+user_long+"&daddr="+rb_lat+","+rb_long);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		startActivity(intent);
		finish();

	}

	//Show Map Route between User's Current Location and nearest bus stop
	public void show_route_near(View view)
	{
		uri = Uri.parse("http://maps.google.com/maps?&saddr="+user_lat+","+user_long+"&daddr="+final_lat+","+final_long);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		startActivity(intent);
		finish();

	}

	public void min(double mi_dist,double dist,String place,String latitudes[],String longitudes[])
	{
		if(dist < mi_dist)
		{
			min_distance=dist;
			min_place=place;
			final_lat=Double.valueOf(latitudes[1]);
			final_long=Double.valueOf(longitudes[1]);

		}
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		latitude =  (double) location.getLatitude();
		longitude= (double) location.getLongitude();
		latitudes[0]=Double.toString(latitude);
		longitudes[0]=Double.toString(longitude);
		user_lat=latitude;
		user_long=longitude;

		Log.d("Gps.java","Onlocationchanged:lat:"+user_lat+"  Long:"+user_long);

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		System.out.println("GPS is enabled");
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		System.out.println("GPS is Disabled");
	}


	public final class current_loc extends AsyncTask<URL , Boolean /* Progress */, String /* Result */>{

		String temp="";
		@Override
		protected String doInBackground(URL... params) {
			try{

				String str="http://maps.googleapis.com/maps/api/geocode/json?latlng="+  latitude+","+longitude+"&sensor=false";


				URL url = new URL(str);
				BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
				while ((str = r.readLine()) != null)
					//str=r.readLine();
				{
					temp=temp+str;

				}



			}
			catch(Exception e){
				Log.d("Gps.java","current_loc: "+e);
			}
			return  null;
		}
		@Override
		protected void onPostExecute(String result) {
			publishProgress(false);

			try {

				try{
					JSONObject mainObj= new JSONObject(temp);
					JSONArray jArray = mainObj.optJSONArray("results");
					JSONObject temp2 = jArray.optJSONObject(0);

					loc.setText(temp2.get("formatted_address").toString());

				}
				catch(Exception e){
					loc.setText("user location json exception...");
					Log.d("Gps.java","user curr location json: "+e);
				}
			}
			catch(Exception e){
				Log.d("Gps.java","current_loc postexec: "+e);
			}
		}

	}




	public final class dist_current_rb extends AsyncTask<URL , Boolean /* Progress */, String /* Result */>{

		String temp="";
		@Override
		protected String doInBackground(URL... params) {
			try{

				String str="http://maps.googleapis.com/maps/api/geocode/json?address="+text_rb+",+Chennai&sensor=false";


				URL url = new URL(str.replace(" ", "%20"));
				BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
				while ((str = r.readLine()) != null)

				{
					temp=temp+str;

				}



			}
			catch(Exception e){
				Log.d("Gps.java","dist_current_rb: "+e);
			}
			return  null;
		}
		@Override
		protected void onPostExecute(String result) {
			publishProgress(false);

			try{
				dist=(TextView)Gps.this.findViewById(R.id.textView1);

				JSONObject mainObj= new JSONObject(temp);
				JSONArray jArray = mainObj.optJSONArray("results");


				JSONObject temp2 = jArray.optJSONObject(0);
				JSONObject loc = temp2.optJSONObject("geometry").optJSONObject("location");
				latitudes[1]=loc.getString("lat");
				longitudes[1]=loc.getString("lng");

				rb_lat=Double.valueOf(latitudes[1]);
				rb_long=Double.valueOf(longitudes[1]);


			}
			catch(Exception e){loc.setText("json exception...");}
			

			try {

				Location locationA = new Location("point A");

				locationA.setLatitude(Double.valueOf(latitudes[0]));
				locationA.setLongitude(Double.valueOf(longitudes[0]));

				Location locationB = new Location("point B");

				locationB.setLatitude(Double.valueOf(latitudes[1]));
				locationB.setLongitude(Double.valueOf(longitudes[1]));

				distance = locationA.distanceTo(locationB);
				String dist_round=String.format("%.2f", distance);
				dist.setText("Distance is: "+dist_round+" meters");
				distance=1.79769313486231570e+308d;

			}
			catch(Exception e){
				Log.d("Gps.java","dist_current_rb postexec: "+e);
			}
		}

	}

	//calc nearest dist between current loc and intermediate bus stops
	public final class dist_current_rg extends AsyncTask<URL , Boolean /* Progress */, String /* Result */>{

		@Override
		protected String doInBackground(URL... params) {
			try{

				int len=arl.size();
				//int len=2;
				String place;
				String str;
				String temp1="";
				URL url;
				BufferedReader r;
				//StringBuilder temp = new StringBuilder();
				String temp;
				for(i=0;i<len;i++)
				{
					place="";
					place=arl.get(i);

					//geocode(place);
					try
					{        	      
						text_rb="";temp="";
						text_rb=place;
						str="";
						str="http://maps.googleapis.com/maps/api/geocode/json?address="+text_rb+",+Chennai&sensor=false";

						url=new URL(str.replace(" ", "%20"));
						r = new BufferedReader(new InputStreamReader(url.openStream()),8*1024);
						while ((str = r.readLine()) != null)

						{
							temp +=str;

						}

						try{
							dist=(TextView)Gps.this.findViewById(R.id.textView1);

							JSONObject mainObj= new JSONObject(temp);
							JSONArray jArray = mainObj.optJSONArray("results");

							JSONObject temp2 = jArray.optJSONObject(0);
							JSONObject loc = temp2.optJSONObject("geometry").optJSONObject("location");
							latitudes[1]=loc.getString("lat");
							longitudes[1]=loc.getString("lng");
						}
						catch(Exception e){loc.setText("json exception...");}

						try{
							Location locationA = new Location("point A");

							locationA.setLatitude(Double.valueOf(latitudes[0]));
							locationA.setLongitude(Double.valueOf(longitudes[0]));

							Location locationB = new Location("point B");


							locationB.setLatitude(Double.valueOf(latitudes[1]));
							locationB.setLongitude(Double.valueOf(longitudes[1]));

							distance = locationA.distanceTo(locationB);

							min(min_distance,distance,place,latitudes,longitudes);
						}
						catch(Exception e){
							Log.d("Gps.java","try block dist of locA&locB:"+e);
							distance=999999999E100;
						}

					}
					catch(Exception e){
						Log.d("Gps.java","rev geocode for intermediate bus stops: "+e);
					}
				}
			}
			catch(Exception e){
				Log.d("Gps.java","dist_current_rg: "+e);
			}
			return  null;
		}
		@Override
		protected void onPostExecute(String result) {
			publishProgress(false);

			try {
				min_dist=(TextView)Gps.this.findViewById(R.id.textView2);
				String min_dist_round=String.format("%.2f", min_distance);
				min_dist.setText("Nearest bus stop is: "+min_place+"\nDistance from current location is:"+min_dist_round+" meters"/*+"\nlat:"+final_lat+"\nlong:"+final_long*/);

			}
			catch(Exception e){
				Log.d("Gps.java","dist_current_rg postexec: "+e);
			}
		}

	}


}