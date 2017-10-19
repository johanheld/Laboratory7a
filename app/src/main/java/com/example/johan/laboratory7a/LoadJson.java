package com.example.johan.laboratory7a;

import android.os.AsyncTask;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadJson extends AsyncTask<String, Integer, ArrayList<Transaction>> {
	private JsonListener listener;

	public LoadJson(JsonListener listener, String url, String type, String encoding) {
		if (listener != null) {
			this.listener = listener;
			execute(url, type, encoding);
		}
	}

	@Override
	protected ArrayList<Transaction> doInBackground(String... params) {
		ArrayList<Transaction> result = new ArrayList<Transaction>();
		try {
			if(params[1]!=null)
				params[0] += "?type="+params[1];
			URL url = new URL(params[0]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream input = connection.getInputStream();
			if(!params[1].equals("Reader")){
				String str = new Scanner(input,params[2]).useDelimiter("\\A").next();
				JSONObject json = new JSONObject(str);
				JSONArray array = json.getJSONArray(params[1]);
				for(int i=0; i<array.length(); i++) {
					JSONObject transaction = array.getJSONObject(i);
					result.add(new Transaction(transaction.getString("datum"),transaction.getString("typ"),transaction.getString("belopp")));
				}
			}
			else this.readTransactions(new JsonReader(new InputStreamReader(input)), result);

		} catch (Exception e) { //MalformedURLException, IOException, ...
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Transaction> result) {
		if(listener!=null)
			listener.jsonLoaded(result);
	}

	private Transaction readTransaction(JsonReader reader) throws IOException {
		String name, date = "", type = "", amount = "";
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();
			if (name.equals("nm"))
				date = reader.nextString();
			else if (name.equals("cty"))
				type = reader.nextString();
			else
				amount = reader.nextString();
		}
		reader.endObject();
		return new Transaction(date,type,amount);
	}
	
	private void readTransactions(JsonReader reader, ArrayList<Transaction> list) throws IOException {
		reader.beginArray();
		while (reader.hasNext()) {
			list.add( readTransaction(reader) );
		}
		reader.endArray();
	}


	public interface JsonListener {
		public void jsonLoaded(ArrayList<Transaction> list);
	}
}
