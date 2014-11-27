package com.colliercode.liist.androidliistit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = "http://10.0.2.2:3001/api/posts/";
        System.out.println(url);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            try{
                return loadFromNetwork(urls[0]);
            } catch (IOException e){
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("Network", result);
        }

        private String loadFromNetwork(String urlString) throws IOException{
            InputStream stream = null;
            String str = "";

            try{
                stream = downloadUrl(urlString);
                str = readIt(stream, 500);
            } finally {
                if (stream != null){
                    stream.close();
                }
            }
        }

        private InputStream downloadUrl(String urlString) {

            //use stringbuilder

        }
    }
}

//
//DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
//HttpGet httpGet = new HttpGet(url);
//// Depends on your web service
////        httpGet.setHeader("Content-type", "application/json");
//
//InputStream inputStream = null;
//String result = null;
//try {
//        HttpResponse response = httpclient.execute(httpGet);
//        HttpEntity entity = response.getEntity();
//
//        inputStream = entity.getContent();
//        // json is UTF-8 by default
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//        StringBuilder sb = new StringBuilder();
//
//        String line = null;
//        while ((line = reader.readLine()) != null)
//        {
//        sb.append(line + "\n");
//        System.out.println(line);
//        }
//        result = sb.toString();
//        } catch (Exception e) {
//        // Oops
//        }
//        finally {
//        try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
//        System.out.println(result);
//        }


//class HttpGuy extends AsyncTask {
//
//    String url = "http://10.0.2.2:3001/api/posts/";
//
//    OkHttpClient client = new OkHttpClient();
//
//    public void run() throws IOException {
//
//        System.out.println(url);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
//    }
//
//    @Override
//    protected Object doInBackground(Object[] params) {
//        try {
//            run();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}

