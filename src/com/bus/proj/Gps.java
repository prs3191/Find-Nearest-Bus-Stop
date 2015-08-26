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
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		System.out.println("Location_Man:"+locationManager);
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
		
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				nextPage(v);
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


			String str="http://maps.googleapis.com/maps/api/geocode/json?latlng="+  latitude+","+longitude+"&sensor=false";

			String temp="";
			URL url = new URL(str);
			BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((str = r.readLine()) != null)
				//str=r.readLine();
			{
				temp=temp+str;

			}

			try{
				JSONObject mainObj= new JSONObject(temp);
				JSONArray jArray = mainObj.optJSONArray("results");
				JSONObject temp2 = jArray.optJSONObject(0);

				loc.setText(temp2.get("formatted_address").toString());

			}
			catch(Exception e){
				loc.setText("user location json exception...");
				}


		}

		catch(Exception e){loc.setText("reverse geocode exception...");}


	}
	
	//Geocoding to get co-cordinates (Latitude/Longitude) of selected radio button
	public void distance(View view) 
	{
		//geocode(text_rb);
		try
		{        	      


			String str="http://maps.googleapis.com/maps/api/geocode/json?address="+text_rb+",+Chennai&sensor=false";

			String temp="";
			URL url = new URL(str.replace(" ", "%20"));
			BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((str = r.readLine()) != null)

			{
				temp=temp+str;

			}

			try{
				dist=(TextView)this.findViewById(R.id.textView1);

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
			
			//calculate distance between Current Location & Selected Radio Button/Bus stop
			try
			{
				Location locationA = new Location("point A");

				locationA.setLatitude(Double.valueOf(latitudes[0]));
				locationA.setLongitude(Double.valueOf(longitudes[0]));

				Location locationB = new Location("point B");

				locationB.setLatitude(Double.valueOf(latitudes[1]));
				locationB.setLongitude(Double.valueOf(longitudes[1]));

				distance = locationA.distanceTo(locationB);

			}
			catch(Exception e)
			{  
				dist.setText("Cannot find the location...");

			}   

		}
		catch(Exception e){loc.setText("geocode error....");}
		dist.setText("Distance is:"+distance);
		distance=1.79769313486231570e+308d;
	}
	
	//Geocoding to get co-cordinates (Latitude/Longitude) of intermediate bus stops
	//to get nearest bus stop from User's current location
	public void find_near(View view)
	{
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

				//temp="";
				url=new URL(str.replace(" ", "%20"));
				r = new BufferedReader(new InputStreamReader(url.openStream()),8*1024);
				while ((str = r.readLine()) != null)

				{
					temp +=str;

				}

				try{
					dist=(TextView)this.findViewById(R.id.textView1);

					JSONObject mainObj= new JSONObject(temp);
					JSONArray jArray = mainObj.optJSONArray("results");

					JSONObject temp2 = jArray.optJSONObject(0);
					JSONObject loc = temp2.optJSONObject("geometry").optJSONObject("location");
					latitudes[1]=loc.getString("lat");
					longitudes[1]=loc.getString("lng");


				}
				catch(Exception e){loc.setText("json exception...");}
				try
				{


					Location locationA = new Location("point A");

					locationA.setLatitude(Double.valueOf(latitudes[0]));
					locationA.setLongitude(Double.valueOf(longitudes[0]));

					Location locationB = new Location("point B");


					locationB.setLatitude(Double.valueOf(latitudes[1]));
					locationB.setLongitude(Double.valueOf(longitudes[1]));

					distance = locationA.distanceTo(locationB);
					//	temp1=temp1+min_place;
					//	temp1=temp1+",";
					// 	dist.setText(temp1);
					//min_distance=Math.min(min_distance, distance);
					//min_place=place;
					min(min_distance,distance,place,latitudes,longitudes);
				}
				catch(Exception e)
				{  
					dist.setText("Cannot find the location...place:"+place);
					distance=999999999E100;

				}   

			}
			catch(Exception e){loc.setText("geocode error....");}


		}


		min_dist=(TextView)this.findViewById(R.id.textView2);
		min_dist.setText("Nearest place is:"+min_place+"\nDistance is:"+min_distance+"\nlat:"+final_lat+"\nlong:"+final_long);

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
		
		System.out.println("Onlocationchanged:lat:"+user_lat+"  Long:"+user_long);
		
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


}