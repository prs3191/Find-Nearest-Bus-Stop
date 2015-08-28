package com.bus.proj;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bus.proj.*;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Bus extends Activity {
	/** Called when the activity is first created. */
	private AutoCompleteTextView source,dest,disp;
	String places[]=new String [278];
	int i=0;
	int SHOW_NAME=1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_bus);

		// find_places();
		//String places[]={"abc","bcd","cdef","def","efg","fgh","ghi"};

		Log.d("b4 jsoup_connect","b4 jsoup_connect: ");

		try{
			new HttpTask().execute();
		}
		catch(Exception e){}
		//get all places for AutocompleteTextView
		//		try
		//		{   
		//			// String temp="";
		//			//	source=(AutoCompleteTextView)this.findViewById(R.id.autoCompleteTextView1);
		//			String str_link="http://my.metrocommute.in/Chennai/Using-Buses-or-Trains";
		//			Document doc = Jsoup.connect(str_link).get();
		//			Log.d("jsoup_connect","jsoup_connect: "+doc);
		//			Elements nodes=doc.getElementsByTag("option");
		//
		//			// source.setText("Size is:"+nodes.size());
		//			for(Element node : nodes)
		//			{
		//				//if(node.attr("value")!=null)
		//				// {
		//
		//				places[i] =node.text();
		//				i++;
		//
		//				//  }
		//
		//				//source.setText(node.text()); 
		//			}
		//		}
		//		//source.setText(temp);
		//
		//		catch(Exception e){
		//			Log.d("jsoup_connect","jsoup_connect exception "+e); 
		//			}
		//
		//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		//				android.R.layout.simple_dropdown_item_1line, places);
		//
		//		AutoCompleteTextView textView1 = (AutoCompleteTextView)
		//				findViewById(R.id.autoCompleteTextView1);    //Source
		//		textView1.setAdapter(adapter);
		//
		//		// ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
		//		//       android.R.layout.simple_dropdown_item_1line, places);
		//		AutoCompleteTextView textView2 = (AutoCompleteTextView)
		//				findViewById(R.id.autoCompleteTextView2);    //Destination
		//		textView2.setAdapter(adapter);


		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				nextpage(view);
			}

		});
	}

	public final class HttpTask extends AsyncTask<URL , Boolean /* Progress */, String /* Result */>{

		@Override
		protected String doInBackground(URL... params) {
			try
			{   
				// String temp="";
				//	source=(AutoCompleteTextView)this.findViewById(R.id.autoCompleteTextView1);
				String str_link="http://my.metrocommute.in/Chennai/Using-Buses-or-Trains";
				Document doc = Jsoup.connect(str_link).get();
				//Log.d("jsoup_connect","jsoup_connect: "+doc);
				Elements nodes=doc.getElementsByTag("option");

				// source.setText("Size is:"+nodes.size());
				for(Element node : nodes)
				{
					//if(node.attr("value")!=null)
					// {
					Log.d("Element node","node: "+node.text());
					if (node.text() != null){
						places[i] =node.text();
						i++;
					}

					//  }

					//source.setText(node.text()); 
				}

				for(i=0;i<places.length;i++){
					if(places[i]==null)
						Log.d("Null node","node "+i+": "+places[i]);
				}
			}
			//source.setText(temp);

			catch(Exception e){
				Log.d("jsoup_connect","jsoup_connect exception "+e); 
			}
			return places.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			publishProgress(false);

			try {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(Bus.this,
						android.R.layout.simple_dropdown_item_1line, 
						places);

				AutoCompleteTextView textView1 = (AutoCompleteTextView)
						findViewById(R.id.autoCompleteTextView1);    //Source
						textView1.setAdapter(adapter);

						// ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
						//       android.R.layout.simple_dropdown_item_1line, places);
						AutoCompleteTextView textView2 = (AutoCompleteTextView)
								findViewById(R.id.autoCompleteTextView2);    //Destination
						textView2.setAdapter(adapter);
			}
			catch(Exception e){
				Log.d("jsoup_Onpost execute"," "+e);
			}
		}
	}




	public void nextpage(View view){
		source=(AutoCompleteTextView)this.findViewById(R.id.autoCompleteTextView1);
		dest=(AutoCompleteTextView)this.findViewById(R.id.autoCompleteTextView2);

		Log.d("Source",""+source);
		Log.d("dest",""+dest);

		String sr=source.getText().toString();
		String d=dest.getText().toString();

		Intent i=new Intent(Bus.this,Stages.class);
		i.putExtra("src",sr);
		i.putExtra("ds", d);

		//startActivity(i);
		startActivityForResult(i, SHOW_NAME);                
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SHOW_NAME)
			if (resultCode == Activity.RESULT_OK) {



			}//if result_ok
	}//onactivityresult

	//TODO: Fill In Methods Etc.
}//class

