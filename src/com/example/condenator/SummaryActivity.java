package com.example.condenator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SummaryActivity extends Activity {
	public String UserName;
	public String PassWord;
	public String cookie;
	public String semester;
	 
    	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        UserName = intent.getStringExtra("username");
        PassWord = intent.getStringExtra("password");
        NetworkConnector NetConnect = new NetworkConnector(this,UserName,PassWord);
        NetConnect.execute();      
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_summary, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	Intent intent = new Intent(this,DetailsActivity.class);
    	intent.putExtra("Cookie", cookie);
    	intent.putExtra("Semester", semester);
    	startActivity(intent);
    	return true;
    }

    
 // Class that deals with network connections   
    
    public class NetworkConnector extends AsyncTask <Void,Void,Void>{
    	
    	public String USER_NAME;
    	public String PASS_WORD;
    	public String cookie;
    	public boolean ValidUser;
      	public ProgressDialog progressBar;
    	
    	public String Name;
    	public String Department;
    	public String Hours_Attended;
    	public String Total_Hours;
    	public String Percentage;
    	public String Admission_no;
    	public String Roll_no;
    	public String Univ_regno;
    	public String Semester;
    	
    	public NetworkConnector(Context c,String user,String pass){
    		USER_NAME=user;
    		PASS_WORD=pass;
    		progressBar = new ProgressDialog(c);
    	}
    	
    	protected void onPreExecute()
    	{
    		
    		Log.w("MyApp", "OnPreExcecute in progress");
    		progressBar.setCancelable(true);
    		progressBar.setMessage("Loading ...");
    		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		progressBar.show();
    		SummaryActivity.this.setContentView(R.layout.activity_summary);
    		    		
    	}
    	
    	protected Void doInBackground(Void... temp){
    		
    		Log.w("MyApp", "DoinBackground is in progress");
    		run();
    		SummaryActivity.this.cookie = cookie;
    		SummaryActivity.this.semester = Semester;
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void temp){
    		progressBar.dismiss();
    		Log.w("MyApp", "OnPOSTEXECUTE IN PROGRESS");
    		if(ValidUser)
    		{
    			SummaryActivity.this.setContentView(R.layout.activity_summary);
    			TextView Name_box 			=	 (TextView)findViewById(R.id.Name_box);
    			TextView Percentage_box 	= 	 (TextView)findViewById(R.id.Percentage_box);
    			TextView HoursAttanded_box 	= 	 (TextView)findViewById(R.id.HoursAttanded_box);
    			TextView TotalHours_box 	= 	 (TextView)findViewById(R.id.TotalHours_box);
    			TextView Department_box 	= 	 (TextView)findViewById(R.id.Department_box);
    			TextView Semester_box	 	=	 (TextView)findViewById(R.id.Semester_box);
    			TextView AdmsnNo_box 		=    (TextView)findViewById(R.id.AdmsnNo_box);
    			TextView RollNo_box 		=	 (TextView)findViewById(R.id.RollNo_box);
    			TextView UnivNo_box 		=	 (TextView)findViewById(R.id.UnivNo_box);
    			
    			if(Float.parseFloat(Percentage) < 75.0){
    				Percentage_box.setTextColor(Color.RED);
    			}
    			
    			Name_box.setText(Name);
    			Percentage_box.setText(Percentage+"%");
    			HoursAttanded_box.setText(Hours_Attended);
    			TotalHours_box.setText(Total_Hours);
    			Department_box.setText(Department);
    			Semester_box.setText(Semester);
    			AdmsnNo_box.setText(Admission_no);
    			RollNo_box.setText(Roll_no);
    			UnivNo_box.setText(Univ_regno);
    			
    			
    			Log.w("MyApp", "percentage(integer)" +  Float.parseFloat(Percentage));
    		}
    		else
    		{
    			new AlertDialog.Builder(SummaryActivity.this).setTitle("Error").setMessage("Invalid Username").setNegativeButton("Close", new DialogInterface.OnClickListener(){
					
					@Override
					public void onClick(DialogInterface dialog, int which){
						Log.w("MyApp", "Invalid User.. alerting...");
						SummaryActivity.this.finish();
					}
				}).show();
    			
    		}
    		
    		
    	}
    	
    	
    	public void run(){
    		   String login_URL="http://www.mesce.ac.in/MainLoginValidation.php";		   
    		   String Summary_URL = "http://www.mesce.ac.in/departments/student.php?option=attendanceSummary";
    		   String Post_Content="txtLogin="+ USER_NAME + "&txtPass=" + PASS_WORD;
    		   InputStream is = null;
    		  
    		   Log.w("MyApp", "Network functin is in progress");
    		   try{
    			   URL url= new URL(login_URL);
    			   HttpURLConnection Login_conn = (HttpURLConnection)url.openConnection();
    			   Login_conn.setRequestMethod("POST");
    			   Login_conn.setDoOutput(true);
    			   Login_conn.setDoInput(true);
    			   		  
    			   DataOutputStream dataOutputStream = new DataOutputStream(Login_conn.getOutputStream());
    			   dataOutputStream.writeBytes(Post_Content);
    			   dataOutputStream.flush();
    			   dataOutputStream.close();
    			   cookie = Login_conn.getHeaderField("Set-Cookie");
    			   Log.w("MyApp", "COOKIE = " + cookie);
    			   
    			   URL sum_url= new URL(Summary_URL);
    			   HttpURLConnection Summary_conn = (HttpURLConnection) sum_url.openConnection();
    			   Summary_conn.setRequestMethod("GET");
    			   Summary_conn.setRequestProperty("Accept","text/html");
    			   Summary_conn.setRequestProperty("Cookie", cookie);
    			   
    			   if(Summary_conn.getContentLength() == 100)
    			   {	// For valid User, getContentLength() will return -1
    				    // For invalid user it will return 100 
    				   Log.w("MyApp", "WRONG USERNAME........");
    				   ValidUser = false;
    				   return;
    			   }
    			   is= Summary_conn.getInputStream();
    			   
    			   Document doc = Jsoup.parseBodyFragment(readStream(is));
    			   Elements name_element = doc.getElementsByClass("head1big");
    			   Elements dtls_element = doc.getElementsByClass("head1");
    			   dtls_element.last().text("END");
    			  
    			   
    			   for (Node child : name_element.first().childNodes()){
    			      if (child instanceof TextNode){
    			    	  Department = ((TextNode) child).text();
    			      }
    			   }
    			   for (Node child : name_element.last().childNodes()){
    				   if (child instanceof TextNode){
    					   Name = ((TextNode) child).text();
    				   }
    			   }
    			   Admission_no = dtls_element.get(1).text();
    			   Roll_no = dtls_element.get(2).text();
    			   Univ_regno = dtls_element.get(3).text();
    			   
    			   		   			   
    			   int i=9;
    			   while(!dtls_element.get(i).text().equals("END")){
    				   Semester = dtls_element.get(i).childNode(0).attr("value");
    				   Hours_Attended = dtls_element.get(i+1).text();
    				   Total_Hours = dtls_element.get(i+2).text();
    				   Percentage = dtls_element.get(i+3).text();i=i+4;
    			   }
    			   
    			   Log.w("MyApp", "PERSONAL DETAILS.....");
    			   Log.w("MyApp", Name);
    			   Log.w("MyApp", Department);
    			   Log.w("MyApp", Admission_no);
    			   Log.w("MyApp", Roll_no);
    			   Log.w("MyApp", Univ_regno);
    			   Log.w("MyApp", Semester);
    			   Log.w("MyApp", "No of hours : " + Hours_Attended);
    			   Log.w("MyApp", Total_Hours);
    			   Log.w("MyApp", Percentage);
    			   Log.w("MyApp", "Finished " +i);   
    			  
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
    		   ValidUser = true;
    	}
    	
    	
    	private String readStream(InputStream in){
    		BufferedReader reader = null;
    		StringBuffer buff= new StringBuffer();
    		try{
    			reader = new BufferedReader(new InputStreamReader(in));
    			String line = "";
    			int line_number=0;
    			while ((line = reader.readLine()) != null){
    				++line_number;
    			    // Relivent Data is in between line 123 and 136
    			    if(line_number >=120 && line_number <=136){
    			    	buff.append(line);
    			    }
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