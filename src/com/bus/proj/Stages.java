package com.bus.proj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Stages extends Activity {
	/** Called when the activity is first created. */
	private TextView source,dest;
	String text_rb;
	int i=0;
	ArrayList<String> arl=new ArrayList<String>(); //get all intermediate bus stops

	String place[]=new String[2]; // store Source & Destination
	String latitude[]=new String[2];
	String longitude[]=new String[2];

	Uri uri;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stages_main);


		source=(TextView)this.findViewById(R.id.editText1);
		Intent intent=this.getIntent();
		place[0]=intent.getStringExtra("src");


		dest=(TextView)this.findViewById(R.id.editText2);
		place[1]=intent.getStringExtra("ds");

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				nextPage(v);
			}
		});

		final Button button1 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				next_gps(v);
			}
		});


	}

	//get all intermediate bus stops between Source and Destination
	public void nextPage(View view) {

		try
		{   
			String temp="";
			String str_link="http://my.metrocommute.in/Chennai/Using-Buses-or-Trains/Connecting/"+place[0]+"/with/"+place[1];
			Document doc = Jsoup.connect(str_link).get();
			Elements nodes=doc.getElementsByTag("a");
			RadioGroup radioGroup = (RadioGroup)findViewById(R.id.Stages);
			LinearLayout.LayoutParams layoutParams = new 
					RadioGroup.LayoutParams( 
							RadioGroup.LayoutParams.WRAP_CONTENT, 
							RadioGroup.LayoutParams.WRAP_CONTENT); 

			for(Element node : nodes)
			{
				if(node.attr("title").contains("Chennai Bus Routes /"))
				{
					RadioButton radioButton = new RadioButton(this);
					/* temp=temp+node.text();
    				   temp=temp+",";*/
					arl.add(node.text());
					radioButton.setText(node.text());
					radioGroup.addView(radioButton,layoutParams);
				}
				//source.setText(node.text()); 

			}
			ViewGroup line = null;
			line.addView(radioGroup);
		}
		//source.setText(temp);

		catch(Exception e){}//source.setText("Exception..."+e); }

		//Get the selected bus stop from RadioGroup
		try
		{
			RadioGroup radioGroup = (RadioGroup)findViewById(R.id.Stages);

			radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				public void onCheckedChanged(RadioGroup rg, int checkedId) {
					// for(int i=0; i<rg.getChildCount(); i++) {
					RadioButton btn = (RadioButton)findViewById(checkedId);
					// if(btn.getId() == checkedId) {
					text_rb = btn.getText().toString();
					// do something with text
					TextView txt=(TextView)findViewById(R.id.textView1);
					txt.setText(text_rb);
					//    break;
					// }
					//  }
				}
			});

		}
		catch(Exception e){}
	}
	public void next_gps(View view) {

		try
		{   

			Intent myIntent = new Intent(view.getContext(), Gps.class);
			myIntent.putExtra("text_radiob", text_rb);
			myIntent.putStringArrayListExtra("arr_list", arl);
			startActivityForResult(myIntent, 0);
		}
		catch(Exception e){}
	}
}