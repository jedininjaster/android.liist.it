package com.colliercode.liist.androidliistit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {

    ListView mListView;
    ArrayList<Post> mPosts = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.postsListView);

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
        if (id == R.id.action_fetch) {

            String url = "http://10.0.2.2:3001/posts/";
            System.out.println(url);
            new DownloadTask().execute(url);

            return true;
        } else if (id == R.id.action_posts) {

            Intent intent = new Intent(MainActivity.this, postListActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setupAdapter(){

        mListView.setAdapter(new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_1, mPosts));

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
        protected void onPostExecute(String result) {

            JSONObject mJsonObject = null;
            JSONArray mJsonArray = null;

            Log.i("Network", result);
            try {
                JSONObject obj = new JSONObject(result);
                Log.d("My App", obj.toString());
                mJsonArray = obj.getJSONArray("posts");
                for(int i = 0; i < mJsonArray.length(); i++){
                    JSONObject jsonObject = mJsonArray.getJSONObject(i);
                    String postTitle = jsonObject.getString("title");
                    String postContent = jsonObject.getString("content");
                    Post post = new Post();
                    post.mTitle = postTitle;
                    post.mContent = postContent;
                    mPosts.add(post);
                }
                setupAdapter();
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
            }
        }

        private String loadFromNetwork(String urlString) throws IOException{
            InputStream stream = null;
            String str = "";
            try{
                stream = downloadUrl(urlString);
                str = readIt(stream);
            } finally {
                if (stream != null){
                    stream.close();
                }
            }
            return str;
        }

        private InputStream downloadUrl(String urlString) throws IOException{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            return stream;
        }

        private String readIt(InputStream stream) throws IOException, UnsupportedEncodingException{
            Reader reader = new InputStreamReader(stream, "UTF-8");
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1];
            while ((reader.read(buffer)) != -1){
                sb.append(buffer);
            }
            return new String(sb);
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

