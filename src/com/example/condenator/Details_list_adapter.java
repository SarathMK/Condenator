package com.example.condenator;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.text.format.DateFormat;

import java.util.Date;

public class Details_list_adapter extends ArrayAdapter<String>{
	public  Context context;
	public  String[] date;
	public  String[] present;
	public Details_list_adapter(Context context, String[] date, String[] present){
		super(context,R.layout.activity_details,date);
		this.context = context;
		this.date = date;
		this.present = present;
		
	}
	
	@Override
	public View getView(int position,View convertView, ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.activity_details, parent,false);

			TextView text_month = (TextView)rowView.findViewById(R.id.text_month);
			TextView text_day = (TextView) rowView.findViewById(R.id.text_day);
			TextView text_date = (TextView) rowView.findViewById(R.id.text_date);
			
			TextView[] text_period = new TextView[6];
			text_period[0]= (TextView) rowView.findViewById(R.id.text_period1);
			text_period[1] = (TextView)rowView.findViewById(R.id.text_period2);
			text_period[2] = (TextView)rowView.findViewById(R.id.text_period3);
			text_period[3] = (TextView)rowView.findViewById(R.id.text_period4);
			text_period[4] = (TextView)rowView.findViewById(R.id.text_period5);
			text_period[5] = (TextView)rowView.findViewById(R.id.text_period6);
		
			String date_temp = date[position].substring(3,5) + "/" +date[position].substring(0, 2) + "/" + date[position].substring(6);			
			text_month.setText((String)DateFormat.format("MMM", new Date(date_temp)));
			text_day.setText((String)DateFormat.format("EEE", new Date(date_temp)));
			text_date.setText(date_temp.substring(3,5));

		String[] temp = present[position].split(" ");
		int j=0;
		for(String i : temp){
			   if(i.equals("P")){ text_period[j].setTextColor(Color.BLACK);}
			   else if(i.equals("A")){ text_period[j].setTextColor(Color.RED);}
			   else if(i.equals("S/P")){ text_period[j].setTextColor(Color.BLUE);}
			   else if(i.equals("S/A")){ text_period[j].setTextColor(Color.RED);}
			   else if(i.equals("D")){ text_period[j].setTextColor(Color.GRAY);}
			   else if(i.equals("M")){ text_period[j].setTextColor(Color.CYAN);}
			   text_period[j].setText(i);
			   j++;
		   }
		temp = null;
		
		return rowView;
	}

}
