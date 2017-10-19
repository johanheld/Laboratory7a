package com.example.johan.laboratory7a;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {
	public static boolean isOnline(Context context) {
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		return (info!=null && info.isConnected());
	}
}
