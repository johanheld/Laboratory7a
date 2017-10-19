package com.example.johan.laboratory7a;

import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LoadXml extends AsyncTask<String,Integer,ArrayList<Transaction>> {
	private XmlListener listener;

	public LoadXml(XmlListener listener, String url, String encoding) {
		if (listener != null) {
			this.listener = listener;
			execute(url, encoding);
		}
	}

	@Override
	protected ArrayList<Transaction> doInBackground(String... params) {
		InputStream input=null;
		ArrayList<Transaction> result = null;
		try {
			URL url = new URL(params[0]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			input = connection.getInputStream();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new InputStreamReader(input,params[1]));
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			result = readTransactions(parser);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch(Exception e) {}
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Transaction> result) {
		if(listener!=null)
			listener.xmlLoaded(result);
	}
	
	private ArrayList<Transaction> readTransactions(XmlPullParser parser) throws IOException, XmlPullParserException {
		ArrayList<Transaction> list = new ArrayList<Transaction>();
		int eventType = parser.getEventType();
		Transaction transaction;
		String name, date, type, amount;
		while(eventType != XmlPullParser.END_DOCUMENT) {
			if(eventType==XmlPullParser.START_TAG) {
				name = parser.getName();
				if(name.equals("utgift")) {
				    date = parser.getAttributeValue(null, "datum");
			        type = parser.getAttributeValue(null, "typ");
			        amount = parser.getAttributeValue(null, "belopp");
					list.add(new Transaction(date,type,amount));
				}
			}
			eventType = parser.next();
		}
		return list;
	}


	public interface XmlListener {
		public void xmlLoaded(ArrayList<Transaction> list);
	}
}
