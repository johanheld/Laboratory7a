package com.example.johan.laboratory7a;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.johan.laboratory7a.LoadJson.JsonListener;

public class JsonTable extends Fragment {
	private TableLayout table;
	private String url, type, encoding;
	private ViewGroup view;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = (ViewGroup)inflater.inflate(R.layout.jsontable, container, false);
		table = (TableLayout)view.findViewById(R.id.table);
		return this.view;
	}
	
	public void setUrlTypeEncoding(String url, String type, String encoding) {
		this.url = url;
		this.type = type;
		this.encoding = encoding;
	}

	public void onResume() {
		super.onResume();
		if(url!=null) {
			new LoadJson(new JL(), url, type, encoding);
		}
	}
	
	private class JL implements JsonListener {

		private void putRow(LayoutInflater inflater, String date, String type, String amount, int background, int textColor) {
			TextView tvDate, tvType, tvAmount;
			View view = inflater.inflate(R.layout.tablerow, null);
			tvDate = (TextView)view.findViewById(R.id.tvDate);
			tvType = (TextView)view.findViewById(R.id.tvType);
			tvAmount = (TextView)view.findViewById(R.id.tvAmount);
			tvDate.setText(date);
			tvDate.setBackgroundColor(background);
			tvDate.setTextColor(textColor);
			tvType.setText(type);
			tvType.setBackgroundColor(background);
			tvType.setTextColor(textColor);
			tvAmount.setText(amount);
			tvAmount.setBackgroundColor(background);
			tvAmount.setTextColor(textColor);
			table.addView(view);
			
		}
		
		public void jsonLoaded(ArrayList<Transaction> list) {
			Activity activity = JsonTable.this.getActivity();
			LayoutInflater inflater = activity.getLayoutInflater();
			putRow(inflater,"Datum","Post","Belopp",Color.BLACK,Color.WHITE);
			for(Transaction tr : list) {
//				String twoDec = String.format("%1.2f", Double.parseDouble(tr.getAmount()));
				putRow(inflater,tr.getDate(),tr.getType(),tr.getAmount(),Color.LTGRAY,Color.BLACK);
			}
		}
	}
	
}
