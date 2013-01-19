package com.example.condenator;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.CheckBox;


public class MainActivity extends Activity {
	
	public String UserName;
	public String PassWord;
	public SharedPreferences settings;
	public CheckBox remember_me; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("MmsgyApp", "MainActivity onCreate is starting...");
        settings = getPreferences(MODE_PRIVATE);
        EditText textbox = (EditText)findViewById(R.id.enter_username);
        remember_me = (CheckBox)findViewById(R.id.chkbox_username);
        
        	if(settings.getBoolean("remember_me",false ))
        	{
        		textbox.setText(settings.getString("username", ""));
        		Log.w("MyApp", "UserName : " + settings.getString("username", ""));
        		remember_me.setChecked(true);
        	}
        	else
        	{
        		textbox.setText("");
        		remember_me.setChecked(false);
        	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	AlertDialog.Builder about = new AlertDialog.Builder(this);
    	about.setNeutralButton("OK", null);
    	
    	TextView text_about = new TextView(this);
    	text_about.setGravity(Gravity.CENTER_HORIZONTAL);
    	text_about.setTextColor(Color.RED);
    	text_about.setTextSize(1, 25);
    	text_about.setText("SMK");
    	text_about.append("\nS7 ECE 09-13");
    	
    	about.setView(text_about);
    	about.setTitle("About");
    	about.show();
    	
    	return true;
    }

    
    @Override
    public void onPause()
    {
    	super.onPause();
    	Log.w("MyApp", "Application is paused");
    	if(remember_me.isChecked())
    	{
    		settings.edit().putString("username", UserName).commit();
    		settings.edit().putBoolean("remember_me", true).commit();
    	}
    	else
    	{
    		settings.edit().putBoolean("remember_me", false).commit();
    	}
    }
    
    
    public void logIn(View view)
    {	
    	EditText textbox = (EditText)findViewById(R.id.enter_username);
    	
    	UserName = textbox.getText().toString();
    	UserName.toLowerCase();
    	
    	try
    	{
    		int i=0,j;
    		while(UserName.charAt(i) != '.')
    		{
    			++i;
    		}
    		++i;
    		j=i;
    		while(UserName.charAt(j) != '.')
    		{
    			++j;
    		}
    		PassWord = UserName.substring(i,j);
    		
    		if(NetworkManager())
    		{
    			Log.w("MyApp", "AsyncTask executed");
    			
    			Intent intent = new Intent(this,SummaryActivity.class);
    			intent.putExtra("username", UserName);
    			intent.putExtra("password", PassWord);
        		startActivity(intent);
    		}
    	}	
    	catch(Exception e)
    	{
    		if(UserName.length() == 0)
    		{
    			Toast.makeText(this, "Username is Empty", Toast.LENGTH_SHORT).show();
    		}
    		else
    		{
    			new AlertDialog.Builder(this).setTitle("Error").setMessage("Invalid Username").setNeutralButton("Close", null).show();
    			e.printStackTrace();
    		}
    	}
    }
    

    public boolean NetworkManager()
    {
    	ConnectivityManager ConnMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo networkInfo = ConnMgr.getActiveNetworkInfo();
    	if(networkInfo == null || !networkInfo.isConnected())
    	{
    		new AlertDialog.Builder(this).setTitle("No connection").setMessage("No internet connetction").setNegativeButton("Close", null);
    		return false;
    		
    	}
    	else
    	{
    		return true;
    	}
    }
    
}