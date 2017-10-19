package com.example.johan.laboratory7a;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LoadBitmap extends Thread
{
    private BitmapListener listener;
    private String url;
    private Activity activity;

    public LoadBitmap(BitmapListener listener, String url, Activity activity)
    {
        if (listener != null)
        {
            this.listener = listener;
            this.url = url;
            this.activity = activity;
            start();
        }
    }

    public void run()
    {
        Bitmap result = null;
        URL url2;
        URLConnection connection;
        InputStream input = null;

        try
        {
            url2 = new URL(url);
            connection = url2.openConnection();
            input = connection.getInputStream();
            result = BitmapFactory.decodeStream(input);
            activity.runOnUiThread(new PlaceOnUi(result));

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public interface BitmapListener
    {
        public void bitmapLoaded(Bitmap bitmap);
    }

    private class PlaceOnUi implements Runnable
    {
        private Bitmap bitmap;
        public PlaceOnUi(Bitmap bitmap)
        {
            this.bitmap = bitmap;
        }

        @Override
        public void run()
        {
            listener.bitmapLoaded(bitmap);
        }
    }
}
