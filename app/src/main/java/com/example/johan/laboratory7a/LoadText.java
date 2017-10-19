package com.example.johan.laboratory7a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class LoadText
{
    private TextListener listener;
    private String urlString;
    private String encoding;
    private Activity activity;
    private Thread thread;


    public LoadText(TextListener listener, String url, String encoding, Activity activity)
    {
        if (listener != null)
        {
            this.listener = listener;
            this.urlString = url;
            this.encoding = encoding;
            this.activity = activity;
//            execute(url, encoding);
            thread = new Thread(new TextLoader());
            thread.start();
        }
    }

    private class TextLoader implements Runnable
    {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;

        @Override
        public void run()
        {
            try
            {
                URL url = new URL(urlString);
                URLConnection connection = url.openConnection();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
                String txt;
                while ((txt = reader.readLine()) != null)
                    result.append(txt + "\n");

                String res = result.toString();
                activity.runOnUiThread(new PlaceOnUi(res));
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    reader.close();
                } catch (Exception e)
                {
                }
            }
        }
    }

    public interface TextListener
    {
        public void textLoaded(String str);
    }

    private class PlaceOnUi implements Runnable
    {
        private String str;
        public PlaceOnUi(String str)
        {
            this.str = str;
        }

        @Override
        public void run()
        {
            listener.textLoaded(str);
        }
    }
}
