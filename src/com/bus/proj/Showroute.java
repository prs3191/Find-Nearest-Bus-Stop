package com.bus.proj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;*/
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Showroute extends Activity {
	/** Called when the activity is first created. */
	private TextView source,dest;
	int i=0;

	String place[]=new String[2];
	String latitude[]=new String[2];
	String longitude[]=new String[2];

	Uri uri;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showroute);


		source=(TextView)this.findViewById(R.id.editText1);
		Intent intent=this.getIntent();
		place[0]=intent.getStringExtra("src");


		dest=(TextView)this.findViewById(R.id.editText2);
		place[1]=intent.getStringExtra("ds");

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click      
				nextPage(v);
			}
		});
	}
	public void nextPage(View view) {

		try
		{        	      

			while(i<2)
			{
				String str="http://maps.googleapis.com/maps/api/geocode/json?address="+place[i]+"&sensor=false";

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
					JSONObject loc = temp2.optJSONObject("geometry").optJSONObject("location");
					latitude[i]=loc.getString("lat");
					longitude[i]=loc.getString("lng");

					// source.setText(temp);
				}
				catch(Exception e){source.setText("json exception...");}
				i++;
			}
			// source.setText(longitude[1]);

			uri = Uri.parse("http://maps.google.com/maps?&saddr="+latitude[0]+","+longitude[0]+"&daddr="+latitude[1]+","+longitude[1]);
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			startActivity(intent);
			finish();

		}
		catch(Exception e)
		{
			source.setText("Exception..."+e.getMessage());
		}
		//startActivity(startNewActivityOpen);
	}
}