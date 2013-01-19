package com.example.condenator;

import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.text.format.DateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class DetailsActivity extends ListActivity {
	public String cookie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cookie = intent.getStringExtra("Cookie");
        String semester = intent.getStringExtra("Semester");
        semester = semester.replace(" ", "+");
        
        String present_date = (String)DateFormat.format("dd/MM/yyyy", new Date());
        Log.w("MyApp", "Date:" + present_date);
        DetailsConnector details = new DetailsConnector(this,cookie,semester,present_date);
        details.execute();
    }

   
    public class DetailsConnector extends AsyncTask <Void, Void, Void>{
    	public String cookie;
    	public String semester;
    	public String present_date;
    	public ArrayList<String> date;
    	public ArrayList<String> present;
    	public ProgressDialog progressBar;

    	public DetailsConnector(Context context, String cookie,String semester,String present_date){
    		progressBar = new ProgressDialog(context);
    		this.cookie = cookie;
    		this.semester = semester;
    		this.present_date = present_date;
    		
    	}
    	protected void onPreExecute(){
    		Log.w("MyApp", "OnPreExcecute in progress");
    		progressBar.setCancelable(true);
    		progressBar.setMessage("Loading ...");
    		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		progressBar.show();
    	}
    	
    	protected Void doInBackground(Void...voids){
    		run();
    		return null;
    	}
    	
    	protected void onPostExecute(Void voids){
    		progressBar.dismiss();
    		Details_list_adapter list_adapter = new Details_list_adapter(DetailsActivity.this, (String[])date.toArray(new String[date.size()]), (String[])present.toArray(new String[present.size()]));
    		DetailsActivity.this.setListAdapter(list_adapter);
    		 
    	}
    	public void run(){
    	
    	   InputStream is = null;
 		   
 		   
 		  String details_URL = "http://www.mesce.ac.in/departments/student.php?option=attendanceDetail";
 		 String details_post = "subSemester="+ semester + "&selDay1=01&selMonth1=01&selYear1=1975&selDay2="+  present_date.substring(0, 2) + "&selMonth2=" + present_date.substring(3, 5) + "&selYear2=" + present_date.substring(6);
 		 Log.w("MyApp", present_date);
 		 Log.w("MyApp", details_post);
 		  
 		   try{
 			   URL url2 = new URL(details_URL);
 			   HttpURLConnection detailsConnection = (HttpURLConnection) url2.openConnection();
 			   detailsConnection.setRequestMethod("POST");
 			   detailsConnection.setDoInput(true);
 			   detailsConnection.setDoOutput(true);
 			   detailsConnection.setRequestProperty("Cookie", cookie);
 			   
 			   DataOutputStream datastream = new DataOutputStream(detailsConnection.getOutputStream());
 			   datastream.writeBytes(details_post);
 			   datastream.flush();
 			   datastream.close();
 			  
   			   String s = readStream(detailsConnection.getInputStream());
 			 
 			   Document doc = Jsoup.parse(s);
			   Elements elements = doc.getElementsByClass("tfont");			   
			   elements.remove(0);
			   elements.remove(elements.last());
			   elements.remove(elements.last());

			   int length = elements.size();
			   Log.w("MyApp", "size = " + length + " ");
			   length /= 8;
			   
			   date = new ArrayList<String>();
			   present = new ArrayList<String>();
			   
			   for(int i= length  ; i >=0 ;i--){
				   date.add(elements.get( (i*8)).text());
				   StringBuffer buf = new StringBuffer();
				   for(int j=1; j < 7 ; j++){
					   if(elements.get( (i*8)+j).text() == ""){
						   buf.append("-- ");
					   }
					   else{
						   buf.append(elements.get( (i*8)+j).text() + " " );
					   }
				   }
				   present.add(buf.toString());
			    }
 		   }
 		   catch(Exception e){
 			   e.printStackTrace();
 			   Log.w("MyApp", "Erron on Run in summaryactivity.. exception working");
 		   }
 		   finally{
 			   if(is != null){
 				   try{
 				   		is.close();
 			   		}
 			   		catch(Exception e){
 			   			e.printStackTrace();
 			   		}
 			   }
 		   }
    	}
 	
 	
 	private String readStream(InputStream in){
 		BufferedReader reader = null;
 		StringBuffer buff= new StringBuffer();
 		try{
 			reader = new BufferedReader(new InputStreamReader(in));
 			String line = "";
 			
 			while ((line = reader.readLine()) != null){
 			    	buff.append(line);
 			}
 		}
 		catch (Exception e){
 			e.printStackTrace();
 		}
 		finally 
 		{
 			if (reader != null) 
 			{
 				try{ 
 					reader.close();}
 			       	catch (Exception e) {e.printStackTrace();}
 			   	}
 			}
 		return buff.toString();
 		}
 	
    }
    
}
