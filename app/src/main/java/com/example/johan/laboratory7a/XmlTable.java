package com.example.johan.laboratory7a;

import java.util.ArrayList;

import com.example.johan.laboratory7a.LoadXml.XmlListener;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class XmlTable extends Fragment {
	private ListView lvTable;
	private String url, encoding;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = (ViewGroup) inflater.inflate(R.layout.xmltable, container,
				false);
		lvTable = (ListView) view.findViewById(R.id.lvTable);
		return view;
	}

	public void setUrlAndEncoding(String url, String encoding) {
		this.url = url;
		this.encoding = encoding;
	}

	public void onResume() {
		super.onResume();
		if (url != null) {
			new LoadXml(new XL(), url, encoding);
		}
	}

	private class XL implements XmlListener {

		public void xmlLoaded(ArrayList<Transaction> list) {
			if(list!=null) {
			    list.add(0, new Transaction("Datum", "Post", "Belopp"));
			    lvTable.setAdapter(new TransactionAdapter(getActivity(), list));
			}
		}
	}

	private class TransactionAdapter extends ArrayAdapter<Transaction> {
		private LayoutInflater inflater;

		public TransactionAdapter(Context context, ArrayList<Transaction> list) {
			super(context, R.layout.listviewrow, R.id.tvDate, list);
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		private void setRow(View view, Transaction tr, boolean image,
				int textColor, int background) {
			TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
			TextView tvType = (TextView) view.findViewById(R.id.tvType);
			TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
			ImageView ivFeeling = (ImageView) view.findViewById(R.id.ivFeeling);
			tvDate.setText(tr.getDate());
			tvDate.setBackgroundColor(background);
			tvDate.setTextColor(textColor);
			tvType.setText(tr.getType());
			tvType.setBackgroundColor(background);
			tvType.setTextColor(textColor);
			tvAmount.setBackgroundColor(background);
			tvAmount.setTextColor(textColor);
			ivFeeling.setBackgroundColor(background);
			if (image) {
				double nbr = Double.parseDouble(tr.getAmount());
				tvAmount.setText(String.format("%1.2f", nbr));
				if (nbr >= 1000)
					ivFeeling.setImageResource(R.drawable.sad);
				else if (nbr > 100)
					ivFeeling.setImageResource(R.drawable.ok);
				else
					ivFeeling.setImageResource(R.drawable.happy);
			} else {
				tvAmount.setText(tr.getAmount());
				ivFeeling.setImageResource(R.drawable.empty);
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = inflater.inflate(R.layout.listviewrow, parent, false);
			Transaction tr = getItem(position);
			if (position == 0) {
				setRow(view, tr, false, Color.WHITE, Color.DKGRAY);
			} else {
				setRow(view, tr, true, Color.BLACK, Color.CYAN);

			}
			return view;
		}
	}
}
